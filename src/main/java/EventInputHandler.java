import org.wso2.siddhi.core.SiddhiAppRuntime;
import org.wso2.siddhi.core.stream.input.InputHandler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class EventInputHandler {
    private static String sCurrentLine = null;
    private static BufferedReader br = null;
    private static FileReader fr = null;

    public EventInputHandler(String FILENAME) {
        initFileRead(FILENAME);
    }


    //  file read for event generation
    private void initFileRead(String FILENAME) {
        try {
            fr = new FileReader(FILENAME);
            br = new BufferedReader(fr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getNextIPAddress() {
        try {
            if ((sCurrentLine = br.readLine()) != null) {
                String[] strArr = sCurrentLine.trim().split(",");
                return strArr[0];
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (sCurrentLine == null) {
                try {
                    if (br != null) {
                        br.close();
                    }
                    if (fr != null) {
                        fr.close();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }

    public void sendEvents(int noOfEvents, SiddhiAppRuntime siddhiAppRuntime) {
        InputHandler inputHandler = siddhiAppRuntime.getInputHandler(Constants.INPUT_STREAM_NAME);

        try {
            for (int i = 0; i < noOfEvents; i++) {
                inputHandler.send(new Object[]{getNextIPAddress()});
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        siddhiAppRuntime.shutdown();
    }
}
