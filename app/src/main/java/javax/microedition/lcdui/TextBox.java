package javax.microedition.lcdui;

public class TextBox extends Displayable {
    private String text;
    private int mode;

    public TextBox(String title, String text, int maxSize, int constraint) {
        super(title);
        this.text = text;
        this.mode = maxSize;
    }

    public String getString() { return text; }

    public void setString(String s) { this.text = s; }
}