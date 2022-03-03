import com.fasterxml.jackson.databind.JsonNode;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.values.PCollection;

import java.util.ArrayList;

public class Example2 {
    public static void main(String[] args) {

        PipelineOptions pipeLineOptions = PipelineOptionsFactory.create();
        Pipeline pipeline = Pipeline.create(pipeLineOptions);

        Parser parser = new Parser();
        String config = parser.readFile(args[0]);
        JsonNode steps = parser.getStepsAsJsonNode(config);
        ArrayList<Step> actions = parser.getStepsAsStep(steps);

        PCollection<String> result = null;

        for (Step action : actions) {
            if (action.getFrom() != "") {
                result = from(pipeline, action);
            } else if (action.getTo() != "") {
                to(result, action);
            } else {
                //TODO
            }
        }

        pipeline.run().waitUntilFinish();
    }

    public static PCollection<String> from(Pipeline p, Step step) {
        String[] id = step.getId().split("\\.");
        String method = id[1].substring(0, 1).toLowerCase() + id[1].substring(1);

        Source source = new Source(p, step.getType(), step.getFrom());
        return source.run(method);
    }

    public static void to(PCollection<String> output, Step step) {
        String[] id = step.getId().split("\\.");
        String method = id[1].substring(0, 1).toLowerCase() + id[1].substring(1);
        Sink sink = new Sink(output, step.getType(), step.getTo());
        sink.run(method);
    }

    public static PCollection<String> standard(Pipeline p, Step step) {
        return null;
    }
}
