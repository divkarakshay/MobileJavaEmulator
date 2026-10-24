package javax.microedition.lcdui;
public class StringItem extends Item {
    private String label, text;
    private int mode;
    public StringItem(String label, String text) { super(0); this.label=label; this.text=text; }
    public StringItem(String label, String text, int appearanceMode) { super(0); this.label=label; this.text=text; }
    public String getLabel(){ return label; }
    public String getText(){ return text; }
    public void setText(String t){ text=t; }
    public int getAppearanceMode(){ return mode; }
}
