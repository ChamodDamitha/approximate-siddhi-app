import org.wso2.siddhi.core.SiddhiAppRuntime;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.stream.output.StreamCallback;

public class SiddhiHandler {
    private SiddhiAppRuntime siddhiAppRuntime;

    private PerformanceTester distinctCountTester;
    private PerformanceTester countTester;

    public SiddhiHandler(int RECORD_WINDOW) {
        countTester = new PerformanceTester("Count Test", RECORD_WINDOW);
        distinctCountTester = new PerformanceTester("Distinct Count Test", RECORD_WINDOW);
        initExecutionPlan();
    }

    public void initExecutionPlan() {
        String definition = "@config(async = 'true') " +
                "define stream " + Constants.INPUT_STREAM_NAME + " (ip string, timestamp long);";

        String query = getExactDistinctCountQuery() + "\n" + getApproximateCountQuery();

        SiddhiManager siddhiManager = new SiddhiManager();


        siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(definition + query);

//      Distinct count execution plan
        siddhiAppRuntime.addCallback(Constants.DISTINCT_COUNT_OUTPUT_STREAM_NAME, new StreamCallback() {
            @Override
            public void receive(Event[] events) {
//                EventPrinter.print(events);
                for (Event e : events) {
//                    System.out.println("ip : " + e.getData()[0] + ", distinct count : " + e.getData()[1]
//                            + ", timestamp : " + e.getData()[2]);
                    distinctCountTester.addEvent((Long) e.getData()[2]);
                }
            }
        });

//        Count execution plan
//        siddhiAppRuntime.addCallback(Constants.COUNT_OUTPUT_STREAM_NAME, new StreamCallback() {
//            @Override
//            public void receive(Event[] events) {
////                EventPrinter.print(events);
//                for (Event e : events) {
//                    System.out.println("ip : " + e.getData()[0] + ", count : " + e.getData()[1]
//                            + ", timestamp : " + e.getData()[2]);
//                    countTester.addEvent((Long)e.getData()[2]);
//                }
//            }
//        });

        siddhiAppRuntime.start();
    }

    public SiddhiAppRuntime getSiddhiAppRuntime() {
        return siddhiAppRuntime;
    }


    private String getApproximateDistinctCountQuery() {
        return "@info(name = 'query1') " +
                "from " + Constants.INPUT_STREAM_NAME + "#approximate:distinctCountEver(ip)  " +
                "select ip, distinctCountEver, timestamp " +
                "insert into " + Constants.DISTINCT_COUNT_OUTPUT_STREAM_NAME + " ;";
    }

    private String getExactDistinctCountQuery() {
        return "@info(name = 'query2') " +
                "from " + Constants.INPUT_STREAM_NAME + " " +
                "select ip, distinctCount(ip) as distinctCountEver, timestamp " +
                "insert into " + Constants.DISTINCT_COUNT_OUTPUT_STREAM_NAME + " ;";
    }

    private String getApproximateCountQuery() {
        return "@info(name = 'query3') " +
                "from " + Constants.INPUT_STREAM_NAME + "#approximate:count(ip) " +
                "select ip, count, timestamp " +
                "insert into " + Constants.COUNT_OUTPUT_STREAM_NAME + " ;";
    }
}
