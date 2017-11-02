

public class Main {
    public static void main(String[] args) {

        SiddhiHandler siddhiHandler = new SiddhiHandler();
        EventInputHandler eventInputHandler = new EventInputHandler(
                ClassLoader.getSystemResource("log20161231/log20161231.csv").getPath());

        eventInputHandler.sendEvents(100000, siddhiHandler.getSiddhiAppRuntime());


    }
}
