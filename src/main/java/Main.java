

public class Main {
    public static void main(String[] args) {

        SiddhiHandler siddhiHandler = new SiddhiHandler();
        EventInputHandler eventInputHandler = new EventInputHandler(
                ClassLoader.getSystemResource("log20161231/log20161231.csv").getPath());

        try {
            eventInputHandler.sendEvents(1000000,1000000, siddhiHandler.getSiddhiAppRuntime());
        } catch (ParameterException e) {
            e.printStackTrace();
        }


    }
}
