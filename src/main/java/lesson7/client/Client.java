package lesson7.client;

import lesson7.constants.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class Client extends JFrame{

    private static final int POS_X = 1200;
    private static final int POS_Y = 300;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 800;
    private static final String NULL_TEXT = "";

    //Панель авторизации
    private final JPanel loginPanel = new JPanel(new FlowLayout());
    private final JTextField loginField = new JTextField(10);
    private final JTextField passField = new JTextField(10);
    private final JButton loginBtn = new JButton("Login");

    // Список компонентов формы
    private final JPanel panelTop = new JPanel(new BorderLayout());
    private final JTextArea chatArea = new JTextArea();
    private final JScrollPane scrollChatArea = new JScrollPane(chatArea);
    private final DefaultListModel listModel = new DefaultListModel();
    private final JList<String> activeUsersList = new JList<>(listModel);
    private final JScrollPane scrollNicknameList = new JScrollPane(activeUsersList);


    private final JPanel panelBottom = new JPanel(new BorderLayout());
    private final JTextField msgToChat = new JTextField();
    private final JButton enterChatMsgBtn = new JButton("Send");

    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private String nickname;

    public Client() {
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
                    if (serverMessage.equals(Constants.END_COMMAND)) {
                        break;
                    } else if (serverMessage.startsWith(Constants.AUTH_OK_COMMAND)) {
                        String[] tokens = serverMessage.split("\\s+");
                        nickname = tokens[1];
                        chatArea.append("Успешно авторизован как " + nickname + "\n");
                    } else if (serverMessage.startsWith(Constants.CLIENT_LIST_COMMAND)) {
                        String[] tokens = serverMessage.split("\\s+");
                        listModel.clear();
                        Arrays.stream(tokens)
                                .skip(1)
                                .forEach(token -> this.listModel.addElement(token));
//                        for (int i = 1; i < tokens.length; i++) {
//                            this.listModel.addElement(tokens[i]);
//                        }
                    } else {
                        chatArea.append(serverMessage + "\n");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(nickname + " disconnected from Echo server");
//                        JOptionPane.showConfirmDialog(this, "Connection lost. Try to connect again?", "Connection lost", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            closeConnection();
            this.setVisible(false);
            new ConnectionWindow();
        }).start();
    }

    private void sendMessage() {
        if (msgToChat.getText().trim().isEmpty()) {
            return;
        }
        try {
            dataOutputStream.writeUTF(msgToChat.getText());
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
        scrollChatArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollNicknameList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        panelTop.add(scrollChatArea, BorderLayout.CENTER);
        panelTop.add(scrollNicknameList, BorderLayout.EAST);
        panelBottom.add(msgToChat, BorderLayout.CENTER);
        panelBottom.add(enterChatMsgBtn, BorderLayout.EAST);

        loginPanel.add(loginField);
        loginPanel.add(passField);
        loginPanel.add(loginBtn);

        this.add(panelTop, BorderLayout.CENTER);
        this.add(panelBottom, BorderLayout.SOUTH);
        this.add(loginPanel, BorderLayout.NORTH);

        this.setVisible(true);

        loginBtn.addActionListener(e -> {
            try {
                dataOutputStream.writeUTF(Constants.AUTH_COMMAND + " " + loginField.getText() + " " + passField.getText());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        msgToChat.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }
        });

        enterChatMsgBtn.addActionListener(e -> sendMessage());

        activeUsersList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JList list = (JList) e.getSource();
                if (e.getClickCount() == 2) {
                    msgToChat.setText(Constants.PRIVATE_MESSAGE_COMMAND + ": " + list.getSelectedValue() + " ");
                    msgToChat.requestFocus();
                }
            }
        });
    }

}
