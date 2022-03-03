import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Parser {

    public ArrayList<Step> getStepsAsStep(JsonNode steps) {
        ObjectMapper objectMapper = new ObjectMapper();
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

    public JsonNode getStepsAsJsonNode(String config) {
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

    public String readFile(String path) {
        String result = null;
        try {
            result = new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
