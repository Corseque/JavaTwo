package lesson6;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class EchoClient extends JFrame{

    private static final int POS_X = 1200;
    private static final int POS_Y = 300;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 800;
    private static final String NULL_TEXT = "";
    private static final String USER_NAME = "Olga";

    // Список компонентов формы
    private final JPanel panelTop = new JPanel(new BorderLayout());
    private final JTextArea chatArea = new JTextArea();
    private final JScrollPane scrollChatArea = new JScrollPane(chatArea);

    private final JPanel panelBottom = new JPanel(new BorderLayout());
    private final JTextField msgToChat = new JTextField();
    private final JButton enterChatMsgBtn = new JButton("Send");

    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public EchoClient() {
        initClientUI();
    }

    protected void openConnection(String serverAddress, String serverPort) throws IOException {
        try {
            int port = Integer.parseInt(serverPort);
            socket = new Socket(serverAddress, port);
        } catch (ClassCastException e) {
            System.out.println("Введен некорректный порт.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        dataInputStream = new DataInputStream(socket.getInputStream());
        dataOutputStream = new DataOutputStream(socket.getOutputStream());
        new Thread(() -> {
            try {
                String serverMessage;
                while (true) {
                    serverMessage = dataInputStream.readUTF();
                    if (serverMessage.equals("/end")) {
                        System.out.println(USER_NAME + "disconnected from Echo server");
//                        JOptionPane.showConfirmDialog(this, "Connection lost. Try to connect again?", "Connection lost", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                        closeConnection();
                        this.setVisible(false);
                        new ConnectionWindow();
                        break;
                    }
                    chatArea.append("Echo: " + serverMessage + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void sendMessage() {
        if (msgToChat.getText().trim().isEmpty()) {
            return;
        }
        try {
            dataOutputStream.writeUTF(msgToChat.getText());
            chatArea.append(USER_NAME + ": " + msgToChat.getText() + "\n");
            msgToChat.setText(NULL_TEXT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        try {
            dataOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            dataInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initClientUI() {
        this.setTitle("Client GUI");
        this.setBounds(POS_X, POS_Y, WIDTH, HEIGHT);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setEditable(false);
        scrollChatArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        panelTop.add(scrollChatArea, BorderLayout.CENTER);
        panelBottom.add(msgToChat, BorderLayout.CENTER);
        panelBottom.add(enterChatMsgBtn, BorderLayout.EAST);
        this.add(panelTop, BorderLayout.CENTER);
        this.add(panelBottom, BorderLayout.SOUTH);
        this.setVisible(true);

        msgToChat.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }
        });

        enterChatMsgBtn.addActionListener(e -> sendMessage());
    }

}
