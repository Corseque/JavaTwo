package lesson7.server;

import lesson7.constants.Constants;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MyServer {
    /**
     * Сервер аутефикации
     */
    private AuthService authService;
    /**
     * Активные клиенты
     */
    private List<ClientHandler> clients = new ArrayList<>();

    private File chatLog;

    public File getChatLog() {
        return chatLog;
    }

    public AuthService getAuthService() {
        return authService;
    }

    public MyServer() {
        chatLog = new File("src/main/java/lesson7/server", "chat_log.txt");
        try {
            if (!chatLog.exists()) {
                chatLog.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (ServerSocket serverSocket = new ServerSocket(Constants.SERVER_PORT)) {
            authService = new SQLiteAuthService();
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

    public synchronized List<ClientHandler> getClients() {
        return clients;
    }

    public synchronized void last100MsgFromLog(String loginMsg, ClientHandler client) {
        int messageCount = 101;
            List<String> msgFromChatLog = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(chatLog))) {
                for (String line; (line = br.readLine()) != null; ) {
                    msgFromChatLog.add(line);
                    System.out.println(line);
                    if (line.equals(loginMsg)) {
                        for (int i = (msgFromChatLog.size() - messageCount) < 0 ? 0 : msgFromChatLog.size() - messageCount; i < msgFromChatLog.size(); i++) {
                            client.sendMessage(msgFromChatLog.get(i));
                            System.out.println(msgFromChatLog.get(i) + "\n");
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public synchronized String writeToServerLog(String message) {
        try (FileWriter writeToChatLogFile = new FileWriter(chatLog, true);) {
            writeToChatLogFile.write(message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }

    public synchronized void broadcastMessage(String message) {
        clients.forEach(client -> client.sendMessage(message));
        writeToServerLog(message);
    }

    public synchronized void privateMessage(String pmSenderNick, String pmRecipientNick, String privateMessage) {
        for (ClientHandler client : clients) {
            if (client.getNickname().equals(pmSenderNick)) {
                client.sendMessage(privateMessage);
            }
            if (client.getNickname().equals(pmRecipientNick)) {
                client.sendMessage(privateMessage);
            }
            writeToServerLog(Constants.PRIVATE_MESSAGE_COMMAND + " " + pmRecipientNick + " " + privateMessage);
        }
    }

    public synchronized void subscribe(ClientHandler client) {
        clients.add(client);
//        if (canReadLog == false) {
//            canReadLog = true;
//            readFromLog();
//        }
    }

    public synchronized void unsubscribe(ClientHandler client) {
        clients.remove(client);
//        if (clients.isEmpty() && canReadLog == true) {
//            canReadLog = false;
//        }
    }

    public boolean isNickBusy(String nick) {
        for (ClientHandler client : clients) {
            if (client.getNickname().equals(nick)) return true;
        }
        return false;
    }

    public synchronized String getActiveClients() {
        StringBuilder sb = new StringBuilder(Constants.CLIENT_LIST_COMMAND).append(" ");
        sb.append(clients.stream()
                .map(client -> client.getNickname())
                .collect(Collectors.joining(" "))
        );
//        for (ClientHandler client : clients) {
//            sb.append(client.getNickname()).append(" ");
//        }
        return sb.toString();
    }

//    public synchronized String[] getActiveClientsArr() {
//        StringBuilder sb = new StringBuilder();
//        sb.append(clients.stream()
//                .map(client -> client.getNickname())
//                .collect(Collectors.joining(" "))
//        );
//        String[] activeClients = sb.toString().split(" ");
//        return activeClients;
//    }
}
