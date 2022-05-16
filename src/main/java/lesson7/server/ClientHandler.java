package lesson7.server;

import lesson7.constants.Constants;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

/**
 * Обработчик для конкретного клиента
 */
public class ClientHandler {

    private MyServer server;
    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private File userLog;

    private List<String> msgFromChatLog = new ArrayList<>();

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
            server.getExecutorService().execute(() -> {
                try {
                    authentication();
                    readMessage();
                } catch (IOException exception) {
                    exception.printStackTrace();
                } finally {
                    closeConnection();
                }
            });
//            new Thread(() -> {
//                try {
//                    authentication();
//                    readMessage();
//                } catch (IOException exception) {
//                    exception.printStackTrace();
//                } finally {
//                    closeConnection();
//                }
//            }).start();
        } catch (IOException exception) {
            throw new RuntimeException("Проблема при создании обработчика.");
        }
    }

    /**
     * Предаём "/auth login pass" в строку
     *
     * @throws IOException
     */
    private void authentication() throws IOException {
        while (true) {
            String str = dataInputStream.readUTF();
            if (str.startsWith(Constants.AUTH_COMMAND)) {
                String[] tokens = str.split("\\s+"); // - \\s+ - обозначение >=1 пробелов, делени на массив из слов строки
                Optional<String> nick = server.getAuthService().getNickByLoginAndPass(tokens[1], tokens[2]);
                if (nick.isPresent()) {
                    //авторизовались
                    if (!server.isNickBusy(nick.get())) {
                        this.nickname = nick.get();

                        userLog = new File("src/main/java/lesson7/client", nickname + "_user_log.txt");
                        if (!userLog.exists()) {
                            userLog.createNewFile();
                        }
                        //sendMessage(server.getLastTenMsgFromChatLog());
                        sendMessage(server.writeToServerLog(Constants.AUTH_OK_COMMAND + " " + nick.get() + " in " + currentTime()));
                        server.last100MsgFromLog(Constants.AUTH_OK_COMMAND + " " + nick.get() + " in " + currentTime(), this);
                        server.broadcastMessage(currentTime() + " " + nickname + " вошёл в чат."); //отправка всем участникам информации о вошедшем пользователе
                        server.subscribe(this); //добавить пользователя для рассылки информации от сервера
                        server.broadcastMessage(server.getActiveClients());
                        return;
                    } else {
                        sendMessage(currentTime() + "Server: Nickname '" + nick.get() + "' уже вошел в чат.");
                    }
                } else {
                    sendMessage(currentTime() + "Server: Неверный логин/пароль");
                }
            }
        }
    }

    private String currentTime() {
        return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
    }

    private void writeToUserLog(String message) {
        try (FileWriter writeToUserLogFile = new FileWriter(userLog, true);) {
            writeToUserLogFile.write(message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try {
            writeToUserLog(message);
            dataOutputStream.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readMessage() throws IOException {
        while (true) {
            String messageFromClient = dataInputStream.readUTF();
            if (messageFromClient.startsWith(Constants.CLIENT_LIST_COMMAND)) {
                sendMessage(server.getActiveClients());
            } else if (messageFromClient.startsWith(Constants.PRIVATE_MESSAGE_COMMAND)) {
                String[] tokens = messageFromClient.split("\\s+", 3);
                server.privateMessage(nickname, tokens[1], currentTime() + " " + nickname + ": " + tokens[2]);
            } else if (messageFromClient.equals(Constants.END_COMMAND)) {
                break;
            } else {
                server.broadcastMessage(currentTime() + " " + nickname + ": " + messageFromClient);
                //System.out.println("Сообщение от " + nickname + ": " + messageFromClient);
            }
        }
    }

    private void closeConnection() {
        server.unsubscribe(this);
        if (!nickname.isEmpty()) {
            server.broadcastMessage(currentTime() + " " + nickname + " вышел из чата");
        }
        server.broadcastMessage(server.getActiveClients());
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
