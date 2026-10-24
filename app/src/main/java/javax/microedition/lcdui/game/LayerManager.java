package javax.microedition.lcdui.game;
import javax.microedition.lcdui.Graphics;
import java.util.Vector;
public class LayerManager {
    private Vector layers=new Vector();
    public LayerManager(){}
    public void append(Layer l){ layers.addElement(l); }
    public void insert(Layer l,int idx){ layers.insertElementAt(l, idx); }
    public void remove(Layer l){ layers.removeElement(l); }
    public Layer getLayerAt(int idx){ return (Layer)layers.elementAt(idx); }
    public int getSize(){ return layers.size(); }
    public void paint(Graphics g,int x,int y){}
    public void setViewWindow(int x,int y,int w,int h){}
}
