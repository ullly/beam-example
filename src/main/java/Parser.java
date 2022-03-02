import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Parser {
    public static void main(String[] args) {

        String config = readFile(args[0]);
        JsonNode steps = getStepsAsJsonNode(config);
        ArrayList<Step> actions = getStepsAsStep(steps);

        for(int i = 0; i < actions.size(); i++) {
            System.out.println(actions.get(i).getId());
        }
    }

    private static ArrayList<Step> getStepsAsStep(JsonNode steps) {
        ObjectMapper objectMapper = new ObjectMapper();
        int length = 0;
        ArrayList<Step> actions = new ArrayList<>();

        for (JsonNode step : steps) {
            try {
                Step action = objectMapper.readValue(step.toString(), Step.class);
                actions.add(action);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        return actions;
    }

    public static JsonNode getStepsAsJsonNode(String config) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode steps = null;
        try {
            JsonNode jsonNode = objectMapper.readValue(config, JsonNode.class);
            steps = jsonNode.get("steps ");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return steps;
    }

    public static String readFile(String path) {
        String result = null;
        try {
            result = new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
