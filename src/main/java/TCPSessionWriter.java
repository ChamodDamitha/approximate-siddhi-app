import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by chamod on 8/28/17.
 */
public class TCPSessionWriter extends Thread {

    private Socket connectionSocket;
    private EventHandler eventHandler;

    public TCPSessionWriter(Socket connectionSocket, EventHandler eventHandler) {
        this.connectionSocket = connectionSocket;
        this.eventHandler = eventHandler;
    }

    @Override
    public void run() {
        BufferedReader inFromClient = null;
        try {
            inFromClient = new BufferedReader(new InputStreamReader(TCPSessionWriter.this.connectionSocket.getInputStream()));

            String clientSentence = null;

            clientSentence = inFromClient.readLine();

            String[] msgStr = clientSentence.trim().split(",");

            String ip = msgStr[0];
            long eventTimestamp = Long.valueOf(msgStr[1]);
            eventHandler.sendEvent(new Object[]{ip, eventTimestamp});

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
