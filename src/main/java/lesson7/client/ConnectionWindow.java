package lesson7.client;

import lesson7.constants.Constants;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ConnectionWindow extends JFrame {

    private static final int POS_X = 300;
    private static final int POS_Y = 400;
    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;

    private final JPanel initPanel = new JPanel(new GridLayout(2,2));
    private final JLabel serverAddressLabel = new JLabel("Enter server address:");
    private final JLabel serverPortLabel = new JLabel("Enter server port:");
    private final JTextField serverAddressField = new JTextField(Constants.SERVER_ADDRESS);
    private final JTextField serverPortField = new JTextField(Integer.toString(Constants.SERVER_PORT));
    private final JButton connectionBtn = new JButton("Connect");

    public ConnectionWindow(){
        this.setTitle("Connect to Echo chat");
        this.setBounds(POS_X, POS_Y, WIDTH, HEIGHT);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initPanel.add(serverAddressLabel);
        initPanel.add(serverAddressField);
        initPanel.add(serverPortLabel);
        initPanel.add(serverPortField);
        this.add(initPanel, BorderLayout.CENTER);
        this.add(connectionBtn, BorderLayout.SOUTH);
        this.pack();
        this.setVisible(true);

        connectionBtn.addActionListener(e -> {
            Client client = new Client();
            try {
                client.openConnection(serverAddressField.getText(), serverPortField.getText());
            } catch (IOException ex) {
                System.out.println("Проблема с подключением к серверу " + serverAddressField.getText() + " на порту " + serverPortField.getText());
                ex.printStackTrace();
            }
            this.setVisible(false);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ConnectionWindow::new);
    }

}
