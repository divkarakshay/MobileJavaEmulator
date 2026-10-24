package javax.microedition.lcdui;

public class Font {
    public static final int SIZE_SMALL = 8;
    public static final int SIZE_MEDIUM = 0;
    public static final int SIZE_LARGE = 16;
    public static final int SIZE_UNKNOWN = -1;
    public static final int SIZE_FULL = 0;
    public static final int STYLE_PLAIN = 0;
    public static final int STYLE_BOLD = 1;
    public static final int STYLE_ITALIC = 2;
    public static final int STYLE_UNDERLINED = 4;
    public static final int FACE_SYSTEM = 0;
    public static final int FACE_MONOSPACE = 32;
    public static final int FACE_PROPORTIONAL = 64;
    public static final int FONT_STATIC_TEXT = 0;
    public static final int FONT_INPUT_TEXT = 1;

    public static final Font DEFAULT_FONT = new Font("default", 0, 0);

    private String name;
    private int style;
    private int size;
    private int face;

    public Font(String name, int style, int size) {
        this.name = name;
        this.style = style;
        this.size = size;
    }

    public static Font getDefaultFont() { return DEFAULT_FONT; }
    public static Font getFont(int face, int style, int size) { return new Font("face"+face, style, size); }

    public int getStyle() { return style; }
    public int getSize() { return size; }
    public int getFace() { return face; }
    public boolean isPlain() { return style==STYLE_PLAIN; }
    public boolean isBold() { return (style&STYLE_BOLD)!=0; }
    public boolean isItalic() { return (style&STYLE_ITALIC)!=0; }
    public boolean isUnderlined() { return (style&STYLE_UNDERLINED)!=0; }
    public int getHeight() { return 12; }
    public int getBaselinePosition() { return 10; }
    public int charWidth(char ch) { return 8; }
    public int charsWidth(char[] ch, int off, int len) { return len*8; }
    public int stringWidth(String str) { return str==null?0:str.length()*8; }
    public int substringWidth(String str,int off,int len) { return len*8; }
    public int getWidth() { return 8; }
    public int across(String text) { return text.length() * 8; }
}