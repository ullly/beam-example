package parser;

public class Step {
    private String id = "";
    private String input = "";
    private String type = "";
    private String of = "";
    private String from = "";
    private String to = "";

    public String getId() { return this.id; }
    public void setId(String id){ this.id = id;}

    public String getInput() { return this.input; }
    public void setInput(String input){ this.input = input;}

    public String getType() { return this.type; }
    public void setType(String type){ this.type = type;}

    public String getOf() { return this.of; }
    public void setOf(String of){ this.of = of;}

    public String getFrom() { return this.from; }
    public void setFrom(String from){ this.from = from;}

    public String getTo() { return this.to; }
    public void setTo(String to){ this.to = to;}
}
