package core;

import core.API.ModifiedAPI;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ModifiedClient {
    private String host;
    private Integer port;
    private String solutionId;
    private JSONParser parser;
    private SocketChannel clientSocket;
    private Class strategyClass;

    public ModifiedClient(String host, Integer port, String solutionId, Class strategyClass) {

        this.host = host;
        this.port = port;
        this.solutionId = solutionId;
        this.parser = new JSONParser();
        this.strategyClass = strategyClass;
    }

    private JSONObject parseString(String jsonString) {
        try {
            return (JSONObject) parser.parse(jsonString);
        } catch (ParseException e) {
            return null;
        }
    }

    public void connect() throws IOException {
        this.clientSocket = SocketChannel.open(new InetSocketAddress(this.host, port));
        JSONObject obj = new JSONObject();
        obj.put("solution_id", solutionId);
        String dumpMessage = this.dumpMessage(obj);
        ByteBuffer sendData = ByteBuffer.wrap(dumpMessage.getBytes());
        // ByteBuffer sendData = ByteBuffer.wrap(dumpMessage);
        clientSocket.write(sendData);

        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.socket().getInputStream()));
        JSONObject message = parseString(reader.readLine());
        if (message == null) {
            System.exit(1);
        }
        if ((message.get("message")).equals("beginning")) {
            strategyLoop(reader);
        }
    }

    private void strategyLoop(BufferedReader reader) throws IOException {
        ModifiedAPI api = new ModifiedAPI(strategyClass);
        while (true) {
            JSONObject object = parseString(reader.readLine());
            if (object == null || (object.get("message") != null && object.get("message").equals("down"))) {
                break;
            }
            clientSocket.write(ByteBuffer.wrap(this.dumpMessage(api.turn(object)).getBytes()));
        }
    }

    private String dumpMessage(JSONAware object) {
        return object.toJSONString() + "\n";
    }
}
