package javax.microedition.lcdui;
public class ImageItem extends Item {
    public static final int LAYOUT_DEFAULT=0, LAYOUT_LEFT=1, LAYOUT_RIGHT=2, LAYOUT_CENTER=3, LAYOUT_NEWLINE_BEFORE=0x100, LAYOUT_NEWLINE_AFTER=0x200;
    private String label;
    private Image img;
    private int layout;
    private String altText;
    private int appearanceMode;
    public ImageItem(String label, Image img, int layout, String altText) { super(0); this.label=label; this.img=img; this.layout=layout; this.altText=altText; }
    public ImageItem(String label, Image img, int layout, String altText, int appearanceMode) { super(0); this.label=label; this.img=img; this.layout=layout; this.altText=altText; this.appearanceMode=appearanceMode; }
    public String getLabel(){ return label; }
    public Image getImage(){ return img; }
    public void setImage(Image i){ img=i; }
    public int getLayout(){ return layout; }
    public void setLayout(int l){ layout=l; }
    public String getAltText(){ return altText; }
    public void setAltText(String t){ altText=t; }
}
