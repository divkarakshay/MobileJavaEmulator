package javax.microedition.lcdui;

public class Graphics {
    public static final int HCENTER = 1;
    public static final int VCENTER = 2;
    public static final int LEFT = 4;
    public static final int RIGHT = 8;
    public static final int TOP = 16;
    public static final int BOTTOM = 32;
    public static final int BASELINE = 64;
    public static final int H_CENTER = HCENTER;
    public static final int V_CENTER = VCENTER;
    public static final int H_RESERVED = 0;
    public static final int V_RESERVED = 0;
    public static final int TRANS_NONE = 0;
    public static final int TRANS_MIRROR = 1;
    public static final int TRANS_MIRROR_ROT180 = 1;
    public static final int TRANS_MIRROR_ROT90 = 2;
    public static final int TRANS_MIRROR_ROT270 = 3;
    public static final int TRANS_ROT90 = 4;
    public static final int TRANS_ROT180 = 5;
    public static final int TRANS_ROT270 = 6;
    public static final int TRANS_SUB = 1;
    public static final int TRANS_REPLACE = 2;
    public static final int TRANS_AND = 3;
    public static final int TRANS_OR = 4;
    public static final int TRANS_XOR = 5;
    public static final int TRANS_SET = 6;
    public static final int SOLID = 0;
    public static final int DOTTED = 1;

    private int color = 0;
    private int stroke = SOLID;
    private Font font = Font.getDefaultFont();
    private int clipX, clipY, clipW=320, clipH=240;
    private int transX, transY;

    public void setColor(int color) { this.color=color; }
    public void setColor(int r,int g,int b) { this.color=(r<<16)|(g<<8)|b; }
    public int getColor() { return color; }
    public void setGrayScale(int g) { setColor(g,g,g); }
    public int getGrayScale() { return (color>>16)&0xff; }
    public void setStrokeStyle(int s) { stroke=s; }
    public int getStrokeStyle() { return stroke; }
    public void setFont(Font f) { font=f; }
    public Font getFont() { return font; }
    public void setClip(int x, int y, int w, int h) { clipX=x; clipY=y; clipW=w; clipH=h; }
    public void clipRect(int x,int y,int w,int h) { setClip(x,y,w,h); }
    public int getClipX() { return clipX; }
    public int getClipY() { return clipY; }
    public int getClipWidth() { return clipW; }
    public int getClipHeight() { return clipH; }
    public void translate(int x,int y) { transX+=x; transY+=y; }
    public int getTranslateX() { return transX; }
    public int getTranslateY() { return transY; }
    public void setGray(int gray) {}
    public void setTransparency(int trans) {}
    public int getTransparency() { return 0; }
    public void fillRect(int x, int y, int w, int h) {}
    public void drawRect(int x,int y,int w,int h) {}
    public void drawRoundRect(int x,int y,int w,int h,int aw,int ah) {}
    public void fillRoundRect(int x,int y,int w,int h,int aw,int ah) {}
    public void drawArc(int x,int y,int w,int h,int sa,int aa) {}
    public void fillArc(int x,int y,int w,int h,int sa,int aa) {}
    public void drawLine(int x1,int y1,int x2,int y2) {}
    public void drawRGB(int[] rgbData,int offset,int scanlength,int x,int y,int width,int height,boolean processAlpha) {}
    public void drawRGB(int x, int y, int rgb) {}
    public void drawRegion(Image src,int x_src,int y_src,int width,int height,int transform,int x_dest,int y_dest,int anchor) {}
    public void drawImage(Image img,int x,int y,int anchor) {}
    public void drawString(String str,int x,int y,int anchor) {}
    public void drawSubstring(String str,int offset,int len,int x,int y,int anchor) {}
    public void drawChar(char c,int x,int y,int anchor) {}
    public void drawChars(char[] data,int offset,int length,int x,int y,int anchor) {}
    public void fillTriangle(int x1,int y1,int x2,int y2,int x3,int y3) {}
    public void copyArea(int x_src,int y_src,int w,int h,int x_dest,int y_dest,int anchor) {}
    public int getDisplayColor(int color) { return color; }
    public int getRGB(int x, int y) { return 0; }
    public void setRGB(int x, int y, int rgb) {}
    public int getDisplayWidth() { return clipW; }
    public int getScreenHeight() { return clipH; }
}