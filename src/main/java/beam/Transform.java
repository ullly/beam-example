package beam;

import org.apache.beam.sdk.transforms.Filter;
import org.apache.beam.sdk.transforms.SerializableFunction;
import org.apache.beam.sdk.values.PCollection;

public class Transform {
    private PCollection<String> result;
    private final String input;
    private final String type;
    private final String of;

    public Transform(PCollection<String> result, String input, String type, String of) {
        this.result = result;
        this.input = input;
        this.type = type;
        this.of = of;
    }

    public PCollection<String> run(String method) {
        switch (method) {
            case "filterTradeJSONs":
                return filterTradeJSONs();
            default:
                return null;
        }
    }

    private PCollection<String> filterTradeJSONs() {
        switch (type) {
            case "Filter":
                result = result.apply(Filter.by((SerializableFunction<String, Boolean>) input1
                        -> input1.contains("risk_measure")));
            default:
        }
        return result;
    }
}
