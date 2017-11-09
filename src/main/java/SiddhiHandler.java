import org.wso2.siddhi.core.SiddhiAppRuntime;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.stream.output.StreamCallback;

public class SiddhiHandler {
    private SiddhiAppRuntime siddhiAppRuntime;

    private PerformanceTester distinctCountTester;
    private PerformanceTester countTester;

    public SiddhiHandler(boolean isApproximateMethod, int RECORD_WINDOW) {
        if (isApproximateMethod) {
            countTester = new PerformanceTester("Approximate Count Test", RECORD_WINDOW);
            distinctCountTester = new PerformanceTester("Approximate Distinct Count Test", RECORD_WINDOW);
        } else {
            countTester = new PerformanceTester("Exact Count Test", RECORD_WINDOW);
            distinctCountTester = new PerformanceTester("Exact Distinct Count Test", RECORD_WINDOW);
        }
        initExecutionPlan(isApproximateMethod);
    }

    public void initExecutionPlan(boolean isApproximateMethod) {
        String definition = "@config(async = 'true') " +
                "define stream " + Constants.INPUT_STREAM_NAME + " (ip String, timestamp long);";

        String query;

        if (isApproximateMethod) {
            query = getApproximateCountQuery() + "\n" + getApproximateDistinctCountQuery();
        } else {
            query = getExactCountQuery() + "\n" + getExactDistinctCountQuery();
        }
        SiddhiManager siddhiManager = new SiddhiManager();

        siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(definition + query);

//      Distinct count execution plan
        siddhiAppRuntime.addCallback(Constants.DISTINCT_COUNT_OUTPUT_STREAM_NAME, new StreamCallback() {
            @Override
            public void receive(Event[] events) {
                for (Event e : events) {
                    distinctCountTester.addEvent((Long) e.getData()[2]);
                }
            }
        });

//        Count execution plan
        siddhiAppRuntime.addCallback(Constants.COUNT_OUTPUT_STREAM_NAME, new StreamCallback() {
            @Override
            public void receive(Event[] events) {
                for (Event e : events) {
                    countTester.addEvent((Long) e.getData()[2]);
                }
            }
        });

        siddhiAppRuntime.start();
    }

    public SiddhiAppRuntime getSiddhiAppRuntime() {
        return siddhiAppRuntime;
    }

    private String getApproximateDistinctCountQuery() {
        return "@info(name = 'query1') " +
                "from " + Constants.INPUT_STREAM_NAME + "#window.length(7500000)" +
                "#approximate:distinctCount(ip,0.1,0.95)  " +
                "select ip, distinctCount, timestamp " +
                "insert into " + Constants.DISTINCT_COUNT_OUTPUT_STREAM_NAME + " ;";
    }

    private String getExactDistinctCountQuery() {
        return "@info(name = 'query2') " +
                "from " + Constants.INPUT_STREAM_NAME + "#window.length(7500000) " +
                "select ip, distinctCount(ip) as distinctCount, timestamp " +
                "insert into " + Constants.DISTINCT_COUNT_OUTPUT_STREAM_NAME + " ;";
    }

    private String getApproximateCountQuery() {
        return "@info(name = 'query3') " +
                "from " + Constants.INPUT_STREAM_NAME + "#window.length(15000000)" +
                "#approximate:count(ip, 0.0001, 0.9) " +
                "select ip, count, timestamp " +
                "insert into " + Constants.COUNT_OUTPUT_STREAM_NAME + " ;";
    }

    private String getExactCountQuery() {
        return "@info(name = 'query3') " +
                "from " + Constants.INPUT_STREAM_NAME + "#window.length(15000000)" +
                "#exact:count(ip) " +
                "select ip, count, timestamp " +
                "insert into " + Constants.COUNT_OUTPUT_STREAM_NAME + " ;";
    }
}
