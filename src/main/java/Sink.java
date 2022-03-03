import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.values.PCollection;

public class Sink {
    private final PCollection<String> output;
    private final String type;
    private final String to;

    public Sink(PCollection<String> output, String type, String to) {
        this.output = output;
        this.type = type;
        this.to = to;
    }

    public void run(String method) {
        switch (method) {
            case "coalescedTradeDate":
                coalescedTradeDate();
            default:
        }
    }

    private void coalescedTradeDate() {
        switch (type) {
            default:
                output.apply(TextIO.write().to(to)
                        .withHeader("\"trades\" : [").withFooter("]").withoutSharding());
        }
    }
}
