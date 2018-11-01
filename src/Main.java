/**
 * Created by Boris on 01.02.17.
 */

import core.Client;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class Main {
    public static void main(String args[]) throws IOException, ParseException {
        String host = System.getenv("WORLD_NAME");
        if (host == null) {
            host = "192.168.43.123";
        }

        String solutionId = System.getenv("SOLUTION_ID");
        if (solutionId == null) {
            solutionId = "-1";
        }
        Client client = new Client(host, 8000, solutionId);
        client.connect();
    }
}
