package javax.microedition.lcdui;

public class Command {
    private String label;
    private int id;
    private int mode;

    public Command(String label, int mode, int id) {
        this.label = label;
        this.mode = mode;
        this.id = id;
    }

    public String getLabel() { return label; }
    public int getID() { return id; }
}