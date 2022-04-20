package lesson6;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class EchoConnectionWindow extends JFrame {

    private static final int POS_X = 300;
    private static final int POS_Y = 400;
    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;

    private final JPanel initPanel = new JPanel(new GridLayout(2,2));
    private final JLabel serverAddress = new JLabel("Enter server address: ");
    private final JLabel serverPort = new JLabel("Enter server port: ");
    private final JTextField serverAddressField = new JTextField("localhost");
    private final JTextField serverPortField = new JTextField("8089");
    private final JButton connectionBtn = new JButton("Connect");

    public EchoConnectionWindow(){
        this.setTitle("Connect to Echo chat");
        this.setBounds(POS_X, POS_Y, WIDTH, HEIGHT);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initPanel.add(serverAddress);
        initPanel.add(serverAddressField);
        initPanel.add(serverPort);
        initPanel.add(serverPortField);
        this.add(initPanel, BorderLayout.CENTER);
        this.add(connectionBtn, BorderLayout.SOUTH);
        this.pack();
        this.setVisible(true);

        connectionBtn.addActionListener(e -> {
            EchoClient echoClient = new EchoClient();
            try {
                echoClient.openConnection(serverAddressField.getText(), serverPortField.getText());
            } catch (IOException ex) {
                System.out.println("Проблема с подключением к серверу " + serverAddressField.getText() + " на порту " + serverPortField.getText());
                ex.printStackTrace();
            }
            this.setVisible(false);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EchoConnectionWindow::new);
    }

}
