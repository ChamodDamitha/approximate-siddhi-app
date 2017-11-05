public class PerformanceTester {
    private String testName;

    private int recordWindow;

    private int eventCountTotal;
    private long timeSpentTotal;
    private long veryFirstTime;

    private double averageThroughputTotal;
    private double averageLatencyTotal;


    private int eventCountWindow;
    private long timeSpentWindow;
    private long veryFirstTimeWindow;

    private double averageThroughputWindow;
    private double averageLatencyWindow;

    public PerformanceTester(String testName, int recordWindow) {
        this.eventCountTotal = 0;
        this.timeSpentTotal = 0;
        this.eventCountWindow = 0;
        this.timeSpentWindow = 0;


        this.testName = testName;
        this.recordWindow = recordWindow;
    }

    public synchronized void addEvent(long eventTimestamp) {
        long currentTime = System.currentTimeMillis();

        if (eventCountTotal == 0) {
            veryFirstTime = eventTimestamp;
        }

        if (eventCountWindow == 0) {
            veryFirstTimeWindow = eventTimestamp;
        }

        eventCountWindow++;
        eventCountTotal++;
        timeSpentWindow += (currentTime - eventTimestamp);
        timeSpentTotal += (currentTime - eventTimestamp);

        averageThroughputTotal = ((eventCountTotal * 1000) / (currentTime - veryFirstTime));
        averageLatencyTotal = ((timeSpentTotal * 1.0) / eventCountTotal);

        if (eventCountTotal % recordWindow == 0) {

            averageThroughputWindow = ((eventCountWindow * 1000) / (currentTime - veryFirstTimeWindow));
            averageLatencyWindow = ((timeSpentWindow * 1.0) / eventCountWindow);

            printLogs();

            eventCountWindow = 0;
            timeSpentWindow = 0;
        }

    }


    private void printLogs() {
        System.out.println(testName + " > Total > Event count : " + eventCountTotal +
                ", Avg latency : " + averageLatencyTotal
                + ", Avg Throughput : " + averageThroughputTotal + ", Time spent : " + timeSpentTotal);

        System.out.println(testName + " > Window > Event count : " + eventCountWindow +
                ", Avg latency : " + averageLatencyWindow
                + ", Avg Throughput : " + averageThroughputWindow + ", Time spent : " + timeSpentWindow);

    }
}
