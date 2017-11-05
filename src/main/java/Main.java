

public class Main {
    public static void main(String[] args) {

        SiddhiHandler siddhiHandler = new SiddhiHandler();
        EventHandler eventHandler = new EventHandler(siddhiHandler.getSiddhiAppRuntime());
        TCPServer tcpServer = new TCPServer(1234,eventHandler);

        tcpServer.start();

    }
}
