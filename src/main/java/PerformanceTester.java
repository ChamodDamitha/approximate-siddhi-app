public class PerformanceTester {
    private String testName;

    private int totalNoOfEventsArrived;
    private long totalTimeElapsed;
    private long startTime;
    private long totalLatency;

    private double averageThroughput;
    private double averageLatency;

    public PerformanceTester(String testName) {
        this.totalNoOfEventsArrived = 0;
        this.totalTimeElapsed = 0;
        this.totalLatency = 0;

        this.testName = testName;
    }

    public void addEvent(long eventTimestamp) {
        long currentTime = System.currentTimeMillis();

        if (totalNoOfEventsArrived == 0) {
            startTime = eventTimestamp;
        }

        totalNoOfEventsArrived++;
        totalTimeElapsed = (currentTime - startTime);
        totalLatency += (currentTime - eventTimestamp);

        averageLatency = (totalLatency / totalNoOfEventsArrived);
        averageThroughput = 1000 * totalNoOfEventsArrived / totalTimeElapsed;

        printLogs();
    }

    public int getTotalNoOfEventsArrived() {
        return totalNoOfEventsArrived;
    }

    public long getTotalTimeElapsed() {
        return totalTimeElapsed;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getTotalLatency() {
        return totalLatency;
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
        System.out.println(testName + ": No of events arrived : " + totalNoOfEventsArrived);
        System.out.println(testName + ": total time elapsed : " + totalTimeElapsed);
    }
}
