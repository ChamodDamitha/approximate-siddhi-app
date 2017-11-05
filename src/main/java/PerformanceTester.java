public class PerformanceTester {
    private String testName;

    private int eventCountTotal;
    private long totalTimeSpent;
    private long timeSpent;
    private long veryFirstTime;

    private double averageThroughput;
    private double averageLatency;

    public PerformanceTester(String testName) {
        this.eventCountTotal = 0;
        this.totalTimeSpent = 0;
        this.testName = testName;
    }

    public synchronized void addEvent(long eventTimestamp) {
        long currentTime = System.currentTimeMillis();

        if (eventCountTotal == 0) {
            veryFirstTime = currentTime;
        }

        eventCountTotal++;
        timeSpent += (currentTime - eventTimestamp);
        totalTimeSpent += timeSpent;

        averageThroughput = ((eventCountTotal * 1000) / (currentTime - veryFirstTime));
        averageLatency = ((totalTimeSpent * 1.0) / eventCountTotal);

        printLogs();

    }

    public double getAverageThroughput() {
        return averageThroughput;
    }

    public double getAverageLatency() {
        return averageLatency;
    }

    private void printLogs() {
        System.out.println(testName + ": Avg latency : " + averageLatency);
        System.out.println(testName + ": Avg throughput : " + averageThroughput);
        System.out.println(testName + ": No of events arrived : " + eventCountTotal);
        System.out.println(testName + ": total time elapsed : " + totalTimeSpent);
    }
}
