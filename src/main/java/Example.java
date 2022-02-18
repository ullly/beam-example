import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.*;
import org.apache.beam.sdk.transforms.join.CoGbkResult;
import org.apache.beam.sdk.transforms.join.CoGroupByKey;
import org.apache.beam.sdk.transforms.join.KeyedPCollectionTuple;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.TupleTag;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Example {
    public static void main(String[] args) {

        PipelineOptions pipeLineOptions = PipelineOptionsFactory.create();
        Pipeline pipeline = Pipeline.create(pipeLineOptions);

        PCollection<String> input = pipeline.apply(TextIO.read()
                .from("/Users/ultan-work/Files/beam/beam-example/input.txt"));

        PCollection<String> tradeAttributes = input.apply(Filter.by((SerializableFunction<String, Boolean>) input1
                -> input1.contains("trade_attributes")));

        PCollection<String> riskMeasure = input.apply(Filter.by((SerializableFunction<String, Boolean>) input2
                -> input2.contains("risk_measure")));

        tradeAttributes = tradeAttributes.apply(ParDo.of(new DoFn<String, String>() {
                    @ProcessElement
                    public void processElement(ProcessContext c) {

                        ObjectMapper jacksonObjMapper = new ObjectMapper();
                        JsonNode jsonNode = null;
                        try {
                            jsonNode = jacksonObjMapper.readValue(c.element(), JsonNode.class);
                            jsonNode = jsonNode.get("trade_attributes").get("trades");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        for (JsonNode element : jsonNode) {
                            c.output(element.toString());
                        }
                    }
                })
        );

        riskMeasure = riskMeasure.apply(ParDo.of(new DoFn<String, String>() {
                    @ProcessElement
                    public void processElement(ProcessContext c) {

                        ObjectMapper jacksonObjMapper = new ObjectMapper();
                        JsonNode jsonNode = null;
                        try {
                            jsonNode = jacksonObjMapper.readValue(c.element(), JsonNode.class);
                            jsonNode = jsonNode.get("risk_measure").get("trades");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        for (JsonNode element : jsonNode) {
                            c.output(element.toString());
                        }
                    }
                })
        );

        PCollection<KV<String, String>> keyedAttributes =
                tradeAttributes.apply(WithKeys.of(new SerializableFunction<String, String>() {
                    @Override
                    public String apply(String input) {

                        ObjectMapper jacksonObjMapper = new ObjectMapper();
                        JsonNode jsonNode = null;
                        try {
                            jsonNode = jacksonObjMapper.readValue(input, JsonNode.class);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }

                        return jsonNode.get("trade_key").asText();
                    }
                }));

        PCollection<KV<String, String>> keyedMeasure =
                riskMeasure.apply(WithKeys.of(new SerializableFunction<String, String>() {
                    @Override
                    public String apply(String input) {

                        ObjectMapper jacksonObjMapper = new ObjectMapper();
                        JsonNode jsonNode = null;
                        try {
                            jsonNode = jacksonObjMapper.readValue(input, JsonNode.class);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }

                        return jsonNode.get("trade_key").asText();
                    }
                }));

        final TupleTag<String> attributesTuple = new TupleTag<>();
        final TupleTag<String> measureTuple = new TupleTag<>();

        PCollection<KV<String, CoGbkResult>> result =
                KeyedPCollectionTuple.of(attributesTuple, keyedAttributes).and(measureTuple, keyedMeasure)
                        .apply(CoGroupByKey.create());

        PCollection<String> output = result.apply(ParDo.of(new DoFn<KV<String, CoGbkResult>, String>() {
            @ProcessElement
            public void processElement(ProcessContext c) {

                KV<String, CoGbkResult> e = c.element();

                ObjectMapper jacksonObjMapper = new ObjectMapper();
                JsonNode jsonNode = null;
                try {
                    jsonNode = jacksonObjMapper.readValue(e.getValue().toString(), JsonNode.class);
                } catch (JsonProcessingException f) {
                    f.printStackTrace();
                }

                String formattedResult = "";
                for (JsonNode element : jsonNode.get(1)) {
                    String measureType = element.get("value").asText().substring(0, 4).toUpperCase();
                    JsonNode node = jsonNode.get(0).get(0);
                    ((ObjectNode) node).set(measureType, element.get("value"));
                    ((ObjectNode) node).put("external_lookup",request());
                    formattedResult = node.toString();
                }

                c.output(formattedResult);
            }
        }));

        output.apply(TextIO.write().to("/Users/ultan-work/Files/beam/beam-example/output")
                .withHeader("\"trades\" : [").withFooter("]").withSuffix(".txt").withoutSharding());

        pipeline.run().waitUntilFinish();
    }

    public static String request() {

        StringBuilder content = new StringBuilder();
        String inputLine;

        try {
            URL url = new URL("http://localhost:8080/\n");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return content.toString();
    }
}
