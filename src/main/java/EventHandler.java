import org.wso2.siddhi.core.SiddhiAppRuntime;
import org.wso2.siddhi.core.stream.input.InputHandler;

public class EventHandler {
    private static InputHandler inputHandler;

    public EventHandler(SiddhiAppRuntime siddhiAppRuntime) {
        inputHandler = siddhiAppRuntime.getInputHandler(Constants.INPUT_STREAM_NAME);
    }


    public synchronized void sendEvent(Object[] event) {
        try {
            inputHandler.send(event);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
