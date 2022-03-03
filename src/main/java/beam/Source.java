package beam;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.values.PCollection;

public class Source {
    private final Pipeline pipeline;
    private final String type;
    private final String from;

    public Source(Pipeline pipeline, String type, String from) {
            this.pipeline = pipeline;
            this.type = type;
            this.from = from;
    }

    public PCollection<String> run(String method) {
        switch (method) {
            case "rangeMessagesInput":
                return rangeMessagesInput();
            default:
                return null;
        }
    }

    private PCollection<String> rangeMessagesInput() {
        switch (type) {
            default:
                return pipeline.apply(TextIO.read().from(from));
        }
    }
}
