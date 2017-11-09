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
        System.out.println("New TCP Writing Session Started...");
        try {
            BufferedReader inFromClient = new BufferedReader(
                    new InputStreamReader(TCPSessionWriter.this.connectionSocket.getInputStream()));

            String clientSentence;
            String[] msgStr;
            String ip;
//            long eventTimestamp;
            while ((clientSentence = inFromClient.readLine()) != null) {
                msgStr = clientSentence.trim().split(",");

                ip = msgStr[0];
//                eventTimestamp = Long.valueOf(msgStr[1]);
                eventHandler.sendEvent(new Object[]{ip, System.currentTimeMillis()});
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
