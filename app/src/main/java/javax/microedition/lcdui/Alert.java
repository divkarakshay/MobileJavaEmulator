package javax.microedition.lcdui;

public class Alert extends Displayable {
    private String text;
    private Gauge gauge;

    public Alert(String title) {
        super(title);
    }

    public void setString(String s) { this.text = s; }

    public void setGauge(Gauge g, int maxValue, int posMode) { this.gauge = g; }

    public Gauge getGauge() { return gauge; }
}