

public class Main {
    public static void main(String[] args) {
        final int RECORD_WINDOW = 10000;

        SiddhiHandler siddhiHandler = new SiddhiHandler(RECORD_WINDOW);
        EventHandler eventHandler = new EventHandler(siddhiHandler.getSiddhiAppRuntime());
        TCPServer tcpServer = new TCPServer(1234,eventHandler);

        tcpServer.start();

    }
}
