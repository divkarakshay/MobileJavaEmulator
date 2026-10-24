package javax.microedition.lcdui;

public class TextField extends Item {
    public static final int ANY = 0;
    public static final int EMAILADDR = 1;
    public static final int NUMERIC = 2;
    public static final int PHONENUMBER = 3;
    public static final int URL = 4;
    public static final int DECIMAL = 5;
    public static final int PASSWORD = 0x10000;
    public static final int UNEDITABLE = 0x20000;
    public static final int SENSITIVE = 0x40000;
    public static final int NON_PREDICTIVE = 0x80000;
    public static final int INITIAL_CAPS_WORD = 0x100000;
    public static final int INITIAL_CAPS_SENTENCE = 0x200000;
    public static final int CONSTRAINT_MASK = 0xFFFFFF;

    private String text;
    private int maxSize;
    private int constraints;

    public TextField(String label, String text, int maxSize, int constraints) {
        super(0);
        this.text = text;
        this.maxSize = maxSize;
        this.constraints = constraints;
    }

    public String getString() { return text; }
    public void setString(String s) { text=s; }
    public int getMaxSize() { return maxSize; }
    public int setMaxSize(int s) { maxSize=s; return s; }
    public int getConstraints() { return constraints; }
    public void setConstraints(int c) { constraints=c; }
    public int size() { return text==null?0:text.length(); }
    public void insert(String src,int pos){}
    public void delete(int off,int len){}
    public int getCaretPosition() { return 0; }
}