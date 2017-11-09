import java.io.*;
import java.nio.charset.StandardCharsets;

public class PerformanceTester {
    private String testName;

    private int recordWindow;

    private long eventCountTotal;
    private long timeSpentTotal;
    private long veryFirstTime;
    private long actualTotalTime;

    private double averageThroughputTotal;
    private double averageLatencyTotal;


    private long eventCountWindow;
    private long timeSpentWindow;
    private long veryFirstTimeWindow;

    private double averageThroughputWindow;
    private double averageLatencyWindow;

    private Writer fstream;

    public PerformanceTester(String testName, int recordWindow) {
        this.eventCountTotal = 0;
        this.timeSpentTotal = 0;
        this.eventCountWindow = 0;
        this.timeSpentWindow = 0;


        this.testName = testName;
        this.recordWindow = recordWindow;

        try {
            fstream = new OutputStreamWriter(new FileOutputStream(new File(testName + "-results.csv")
                    .getAbsoluteFile()), StandardCharsets.UTF_8);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            fstream.write("Number of events received in this window,Total number of events received," +
                    "Throughput in this window (events/second), Entire throughput for the run (events/second), " +
                    "Average latency per event in this window(ms), Entire Average latency per event (ms)," +
                    " Total elapsed time(s)");
            fstream.write("\r\n");
            fstream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        actualTotalTime = currentTime - veryFirstTime;

        if (eventCountTotal % recordWindow == 0) {

            averageThroughputWindow = ((eventCountWindow * 1000) / (currentTime - veryFirstTimeWindow));
            averageLatencyWindow = ((timeSpentWindow * 1.0) / eventCountWindow);

            writeLogs();

            eventCountWindow = 0;
            timeSpentWindow = 0;
        }

    }


    private void writeLogs() {
        System.out.println(testName + " > Total > Event count : " + eventCountTotal +
                ", Avg latency : " + averageLatencyTotal
                + ", Avg Throughput : " + averageThroughputTotal + ", Time spent : " + (actualTotalTime / 1000.0));
//
//        System.out.println(testName + " > Window > Event count : " + eventCountWindow +
//                ", Avg latency : " + averageLatencyWindow
//                + ", Avg Throughput : " + averageThroughputWindow + ", Time spent : " + timeSpentWindow);

        try {
            fstream.write(eventCountWindow + "," + eventCountTotal + "," +
                    averageThroughputWindow + "," + averageThroughputTotal + "," +
                    averageLatencyWindow + "," + averageLatencyTotal + "," +
                    (actualTotalTime / 1000.0));
            fstream.write("\r\n");
            fstream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
