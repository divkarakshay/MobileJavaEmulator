package javax.microedition.lcdui;
public class ChoiceGroup extends Item implements Choice {
    private String label;
    private int choiceType;
    private java.util.Vector elements=new java.util.Vector();
    public ChoiceGroup(String label, int choiceType){ super(0); this.label=label; this.choiceType=choiceType; }
    public ChoiceGroup(String label, int choiceType, String[] stringElements, Image[] imageElements){ super(0); this.label=label; this.choiceType=choiceType; for(int i=0;i<stringElements.length;i++) elements.addElement(stringElements[i]); }
    public int append(String str, Image img){ elements.addElement(str); return elements.size()-1; }
    public void insert(int idx, String str, Image img){ elements.insertElementAt(str, idx); }
    public void delete(int idx){ elements.removeElementAt(idx); }
    public void deleteAll(){ elements.removeAllElements(); }
    public int size(){ return elements.size(); }
    public String getString(int idx){ return (String)elements.elementAt(idx); }
    public Image getImage(int idx){ return null; }
    public void set(int idx, String str, Image img){ elements.setElementAt(str, idx); }
    public boolean isSelected(int idx){ return false; }
    public void setSelectedIndex(int idx, boolean sel){}
    public int getSelectedIndex(){ return -1; }
    public int getSelectedFlags(boolean[] arr){ return 0; }
    public void setSelectedFlags(boolean[] arr){}
    public void setFitPolicy(int p){}
    public int getFitPolicy(){ return 0; }
    public void setFont(int idx, Font f){}
    public Font getFont(int idx){ return null; }
}
