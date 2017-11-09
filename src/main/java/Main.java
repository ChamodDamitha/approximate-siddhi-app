
public class Main {

    public static void main(String[] args) {
//        System.setProperty("recordWindow", "1000000");
//        System.setProperty("isApproximate", "True");

        boolean isAprroximate = Boolean.parseBoolean(System.getProperty("isApproximate"));
        int RECORD_WINDOW = Integer.parseInt(System.getProperty("recordWindow"));

        SiddhiHandler siddhiHandler = new SiddhiHandler(isAprroximate,RECORD_WINDOW);
        EventHandler eventHandler = new EventHandler(siddhiHandler.getSiddhiAppRuntime());
        TCPServer tcpServer = new TCPServer(1234, eventHandler);

        tcpServer.start();

    }
}
