package javax.microedition.lcdui;

import java.util.Vector;

public class Form extends Displayable {
    private String title;
    private Vector items = new Vector();
    private Ticker ticker;

    public Form(String title) {
        super(title);
        this.title = title;
    }

    public void addComponent(Item item) { items.addElement(item); }

    public int append(Item item) { items.addElement(item); return items.size()-1; }
    public int append(String str) { StringItem si = new StringItem(null, str); items.addElement(si); return items.size()-1; }
    public int append(Image img) { ImageItem ii = new ImageItem(null, img, 0, null); items.addElement(ii); return items.size()-1; }

    public void set(int idx, Item item) { items.setElementAt(item, idx); }
    public void insert(int idx, Item item) { items.insertElementAt(item, idx); }

    public void setTicker(Ticker t) { this.ticker = t; }

    public int getContentWidth() { return 0; }
    public int getContentHeight() { return 0; }

    public void delete(int elementno) { items.removeElementAt(elementno); }
    public void deleteAll() { items.removeAllElements(); }
    public int getNumItems() { return items.size(); }
    public Item get(int elementno) { return (Item) items.elementAt(elementno); }
    public int size() { return items.size(); }
}