package javax.microedition.lcdui;

public class Gauge {
    private String text;
    private boolean mode; // true = determinate, false = indeterminate
    private int value;
    private int maxValue;

    public Gauge(String text, boolean mode, int maxValue) {
        this.text = text;
        this.mode = mode;
        this.maxValue = maxValue;
        this.value = 0;
    }

    public void setValue(int value) { this.value = value; }

    public int getValue() { return value; }
}