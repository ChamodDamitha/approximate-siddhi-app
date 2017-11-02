import org.wso2.siddhi.core.SiddhiAppRuntime;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.stream.output.StreamCallback;

public class SiddhiHandler {
    private SiddhiAppRuntime siddhiAppRuntime;

    public SiddhiHandler() {
        initExecutionPlan();
    }

    public void initExecutionPlan() {
        String definition = "@config(async = 'true') " +
                "define stream " + Constants.INPUT_STREAM_NAME + " (ip string);";

        String query = getApproximateDistinctCountQuery();

        SiddhiManager siddhiManager = new SiddhiManager();


        siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(definition + query);

        siddhiAppRuntime.addCallback(Constants.DISTINCT_COUNT_OUTPUT_STREAM_NAME, new StreamCallback() {
            @Override
            public void receive(Event[] events) {
//                EventPrinter.print(events);
                for (Event e : events) {
                    System.out.println(e.getData()[0] + ", " + e.getData()[1]);
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
                "from " + Constants.INPUT_STREAM_NAME + "#approximate:distinctCountEver(ip)  " +
                "select ip, distinctCountEver " +
                "insert into " + Constants.DISTINCT_COUNT_OUTPUT_STREAM_NAME + " ;";
    }

    private String getExactDistinctCountQuery() {
        return "@info(name = 'query1') " +
                "from " + Constants.INPUT_STREAM_NAME + " " +
                "select ip, distinctCount(ip) as distinctCountEver " +
                "insert into " + Constants.DISTINCT_COUNT_OUTPUT_STREAM_NAME + " ;";
    }
}
