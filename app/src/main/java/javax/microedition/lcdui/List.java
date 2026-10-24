package javax.microedition.lcdui;

public class List extends Form {
    public List(String title, int type, int[] stringIds, int[] stringImages) {
        super(title);
    }

    public int getSelectedIndex() { return -1; }

    public void setSelectedIndex(int index, boolean append) {}
}