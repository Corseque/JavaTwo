package lesson4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class ClientGUI extends JFrame {

    private static final int POS_X = 1200;
    private static final int POS_Y = 300;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 800;
    private static final String DEFAULT_TEXT = "Поле для ввода сообщения";
    private static final String NULL_TEXT = "";
    private static final String USER_NAME = "Olga";

    // Список компонентов формы
    private final JPanel panelTop = new JPanel(new BorderLayout());
    private final JTextArea chatArea = new JTextArea();
    private final JScrollPane scrollChatArea = new JScrollPane(chatArea);

    private final JPanel panelBottom = new JPanel(new BorderLayout());
    private final JTextField msgToChat = new JTextField();
    private final JButton enterChatMsgBtn = new JButton("Отправить");

    public ClientGUI() {
        this.setTitle("Client GUI");
        this.setBounds(POS_X, POS_Y, WIDTH, HEIGHT);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setEditable(false);
        scrollChatArea.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        msgToChat.setText(DEFAULT_TEXT);

        panelTop.add(scrollChatArea, BorderLayout.CENTER);
        panelBottom.add(msgToChat, BorderLayout.CENTER);
        panelBottom.add(enterChatMsgBtn, BorderLayout.EAST);
        this.add(panelTop, BorderLayout.CENTER);
        this.add(panelBottom, BorderLayout.SOUTH);
        this.setVisible(true);

        msgToChat.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                msgToChat.setText(NULL_TEXT);
            }
        });

        msgToChat.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    chatArea.append(USER_NAME + ": " + msgToChat.getText() + "\n");
                    msgToChat.setText(NULL_TEXT);
                }
            }
        });

        enterChatMsgBtn.addActionListener(e -> {
            if (!msgToChat.getText().equals(DEFAULT_TEXT) && !msgToChat.getText().equals(NULL_TEXT)) {
                chatArea.append(USER_NAME + ": " + msgToChat.getText() + "\n");
                msgToChat.setText(DEFAULT_TEXT);
            }
        });

        chatArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (msgToChat.getText().equals(NULL_TEXT)) {
                    msgToChat.setText(DEFAULT_TEXT);
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ClientGUI();
        });
    }
}
