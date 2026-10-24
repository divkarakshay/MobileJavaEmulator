package javax.microedition.lcdui;

import java.util.Vector;

public class Displayable {
    private String title;
    private Ticker ticker;
    private Vector commands = new Vector();
    private CommandListener listener;

    public Displayable(String title) {
        this.title = title;
    }

    public String getTitle() { return title; }
    public void setTitle(String s) { this.title = s; }
    public void setTicker(Ticker t) { this.ticker = t; }
    public Ticker getTicker() { return ticker; }
    public boolean isShown() { return false; }
    public int getWidth() { return 240; }
    public int getHeight() { return 320; }

    public void addCommand(Command cmd) { commands.addElement(cmd); }
    public void removeCommand(Command cmd) { commands.removeElement(cmd); }
    public void setCommandListener(CommandListener l) { listener = l; }
    public CommandListener getCommandListener() { return listener; }
    public void removeCommandListener() { listener=null; }
}