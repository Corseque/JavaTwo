package lesson7.server;

import lesson7.constants.Constants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Обработчик для конкретного клиента
 */
public class ClientHandler {

    private MyServer server;
    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public ClientHandler(MyServer server, Socket socket) {
        try {
            this.server = server;
            this.socket = socket;
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
            new Thread(() -> {
                try {
                    authentication();
                    readMessage();
                } catch (IOException exception) {
                    exception.printStackTrace();
                } finally {
                    closeConnection();
                }
            }).start();
        } catch (IOException exception) {
            throw new RuntimeException("Проблема при создании обработчика.");
        }
    }

    /**
     * Предаём "/auth login pass" в строку
     * @throws IOException
     */
    private void authentication() throws IOException {
        while (true) {
            String str = dataInputStream.readUTF();
            if (str.startsWith(Constants.AUTH_COMMAND)) {
                String[] tokens = str.split("\\s+"); // - \\s+ - обозначение >=1 пробелов, делени на массив из слов строки
                String nick = server.getAuthService().getNickByLoginAndPass(tokens[1], tokens[2]);
                if (nick != null) {
                    //авторизовались
                    if (!server.isNickBusy(nick)) {
                        this.nickname = nick;
                        sendMessage("Server: " + Constants.AUTH_OK_COMMAND + " " + nick);
                        server.broadcastMessage(nick + " вошёл в чат."); //отправка всем участникам информации о вошедшем пользователе
                        server.subscribe(this); //добавить пользователя для рассылки информации от сервера
                        return;
                    } else {
                        sendMessage("Nickname '" + nick + "' уже вошел в чат.");
                    }
                } else {
                    sendMessage("Server: Неверный логин/пароль");
                }
            }
        }
    }

    public void sendMessage(String message) {
        try {
            dataOutputStream.writeUTF(message);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void readMessage() throws IOException {
        while (true) {
            String messageFromClient = dataInputStream.readUTF();
            if (messageFromClient.startsWith(Constants.PRIVATE_MESSAGE_COMMAND)) {
                String[] tokens = messageFromClient.split("\\s+", 3);
                server.privateMessage(nickname, tokens[1], nickname + ": " + tokens[2]);
            } else {
                System.out.println("Сообщение от " + nickname + ": " + messageFromClient);
                if (messageFromClient.equals(Constants.END_COMMAND)) {
                    break;
                }
                server.broadcastMessage(nickname + ": " + messageFromClient);
            }
        }
    }

    private void closeConnection() {
        server.unsubscribe(this);
        server.broadcastMessage(nickname + " вышел из чата");
        try {
            dataInputStream.close();
        } catch (IOException exception) {
            //ignore
        }
        try {
            dataOutputStream.close();
        } catch (IOException exception) {
            //ignore
        }
        try {
            socket.close();
        } catch (IOException exception) {
            //ignore
        }
    }
}
