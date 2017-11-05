import java.io.*;
import java.net.*;

public class TCPServer extends Thread{
    private Socket connectionSocket;
    private int port;
    private EventHandler eventHandler;

    public TCPServer(int port, EventHandler eventHandler) {
        this.port = port;
        this.eventHandler = eventHandler;
    }

    @Override
    public void run() {
        try {
            System.out.println("TCP SERVER started................");
            ServerSocket welcomeSocket = new ServerSocket(TCPServer.this.port);
            while (true) {
                connectionSocket = welcomeSocket.accept();
                new TCPSessionWriter(connectionSocket,eventHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
