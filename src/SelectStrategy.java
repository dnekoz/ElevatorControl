import core.API.ModifiedAPI;
import core.Client;
import core.ModifiedClient;
import core.Strategy;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SelectStrategy {
    public static void main(String args[]) throws IOException, ParseException {

        Map<String, String> argsMap = makeMap(args);
        Class strategyClass = Strategy.class;

        if (argsMap.containsKey("strategy")){
            String strategyName = argsMap.get("strategy");
            try {
                strategyClass =  Class.forName(strategyName);
            } catch (ClassNotFoundException e) {
                strategyClass = Strategy.class;
            }
        }

        String host = System.getenv("WORLD_NAME");
        if (host == null) {
            host = "192.168.43.123";
        }

        String solutionId = System.getenv("SOLUTION_ID");
        if (solutionId == null) {
            solutionId = "-1";
        }

        ModifiedClient client = new ModifiedClient(host, 8000, solutionId, strategyClass);
        client.connect();
    }

    private static Map<String, String> makeMap(String[] args) {
        Map<String, String> map = new HashMap<>();
        for (String arg : args) {
            if (arg.contains("=")) {
                //works only if the key doesn't have any '='
                map.put(arg.substring(0, arg.indexOf('=')),
                        arg.substring(arg.indexOf('=') + 1));
            }
        }
        return map;
    }
}
