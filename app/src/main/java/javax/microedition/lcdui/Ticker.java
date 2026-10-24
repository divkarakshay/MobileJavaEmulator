package javax.microedition.lcdui;

public class Ticker {
    private String ticker;

    public Ticker(String ticker) {
        this.ticker = ticker;
    }

    public void setString(String s) {
        this.ticker = s;
    }
}