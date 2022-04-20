package lesson6;

import javax.swing.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class EchoServer extends JFrame{

    private static final int POS_X = 300;
    private static final int POS_Y = 300;
    private static final int WIDTH = 300;
    private static final int HEIGHT = 75;
    private final JButton startServer = new JButton("Start server");
    private final JButton stopServer = new JButton("Stop server");
    static private final int SERVER_PORT = 8089;
    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public EchoServer() {
        this.setTitle("Server GUI");
        this.setBounds(POS_X, POS_Y, WIDTH, HEIGHT);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(new FlowLayout());
        this.add(startServer);
        this.add(stopServer);
        this.setVisible(true);

        startServer.addActionListener(e -> openServer());

        stopServer.addActionListener(e -> closeServer());
    }

    private void closeServer() {
        try {
            dataOutputStream.writeUTF("Echo: Echo server disconnected.");
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
        System.out.println("Echo server disconnected");
    }

    private void openServer() {
//        Socket socket = null;
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)){
            System.out.println("Waiting for input connection...");
            socket = serverSocket.accept(); //Waiting for input connection of chat users (thread is blocked)
            System.out.println("User is connected");
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            Thread thread1 = new Thread(() -> {
                String userMessage;
                while (true) {
                    try {
                        userMessage = dataInputStream.readUTF();
                        if (userMessage.equals("/end")) {
                            System.out.println("User disconnected from Echo server");
                            dataOutputStream.writeUTF("User disconnected from Echo server.");
                            break;
                        }
                        System.out.println("User send " + userMessage);
                        dataOutputStream.writeUTF(userMessage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread1.start();
            Thread thread2 = new Thread(() -> {
                Scanner serverInput = new Scanner(System.in);
                String serverMessage;
                while (true) {
                    try {
                        serverMessage = serverInput.nextLine();
                        if (serverMessage.equals("/endEcho")) {
                            System.out.println("Echo server was shut down.");
                            dataOutputStream.writeUTF("Echo server was shut down.");
                            closeServer();
                            break;
                        }
                        if (!serverMessage.trim().isEmpty()) {
                            System.out.println("Server message '" + serverMessage + "' was send to chat.");
                            dataOutputStream.writeUTF(serverMessage);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread2.start();
        } catch (IOException e) {
            e.printStackTrace();
        } //finally {serverSocket.close()}
    }
//
//    public synchronized String getServerMessage() {
//        Scanner serverInput = new Scanner(System.in);
//        while (!serverInput.hasNextLine()) {
//            try {
//                wait();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        return serverInput.nextLine();
//    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(EchoServer::new);
    }
}
