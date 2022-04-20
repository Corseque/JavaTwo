package lesson7.server;

import lesson7.constants.Constants;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MyServer {
    /**
     * Сервер аутефикации
     */
    private AuthService authService;
    /**
     * Активные клиенты
     */
    private List<ClientHandler> clients = new ArrayList<>();;

    public AuthService getAuthService() {
        return authService;
    }

    public MyServer() {
        try (ServerSocket serverSocket = new ServerSocket(Constants.SERVER_PORT)) {
            authService = new BaseAuthService();
            authService.start();

            while (true) {
                System.out.println("Сервер ожидает подключения.");
                Socket socket = serverSocket.accept();
                System.out.println("Клиент подключился.");
                new ClientHandler(this, socket);
            }
        } catch (IOException exception) {
            System.out.println("Ошибка в работе сервера.");
            exception.printStackTrace();
        } finally {
            if (authService != null) {
                authService.stop();
            }
        }
    }

    public List<ClientHandler> getClients() {
        return clients;
    }

    public synchronized void broadcastMessage(String message) {
        clients.forEach(client -> client.sendMessage(message));
//        for (ClientHandler client : clients) {
//            client.sendMessage(message);
//        }
    }

    public synchronized void privateMessage(String pmSenderNick, String pmRecipientNick, String privateMessage) {
        for (ClientHandler client:clients) {
            if (client.getNickname().equals(pmSenderNick)) {
                client.sendMessage(privateMessage);
            }
            if (client.getNickname().equals(pmRecipientNick)) {
                client.sendMessage(privateMessage);
            }
        }
    }

    public synchronized void subscribe(ClientHandler client) {
        clients.add(client);
    }

    public synchronized void unsubscribe(ClientHandler client) {
        clients.remove(client);
    }

    public boolean isNickBusy(String nick) {
        for (ClientHandler client:clients) {
            if (client.getNickname().equals(nick)) return true;
        }
        return false;
    }
}
