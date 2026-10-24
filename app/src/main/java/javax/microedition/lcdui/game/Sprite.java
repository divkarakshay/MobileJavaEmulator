package javax.microedition.lcdui.game;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Graphics;
public class Sprite extends Layer {
    public static final int TRANS_NONE=0, TRANS_MIRROR=1, TRANS_MIRROR_ROT180=2, TRANS_MIRROR_ROT90=3, TRANS_MIRROR_ROT270=4, TRANS_ROT90=5, TRANS_ROT180=6, TRANS_ROT270=7;
    private Image img;
    private int frameWidth, frameHeight;
    private int frame;
    public Sprite(Image img){ super(img.getWidth(), img.getHeight()); this.img=img; this.frameWidth=img.getWidth(); this.frameHeight=img.getHeight(); }
    public Sprite(Image img,int w,int h){ super(w,h); this.img=img; this.frameWidth=w; this.frameHeight=h; }
    public void setImage(Image img,int w,int h){ this.img=img; this.frameWidth=w; this.frameHeight=h; }
    public void setTransform(int t){}
    public void setFrame(int f){ frame=f; }
    public int getFrame(){ return frame; }
    public int getFrameSequenceLength(){ return 1; }
    public void nextFrame(){}
    public void prevFrame(){}
    public void setFrameSequence(int[] seq){}
    public void setRefPixelPosition(int x,int y){}
    public int getRefPixelX(){ return x; }
    public int getRefPixelY(){ return y; }
    public boolean collidesWith(Sprite s,boolean pixel){ return false; }
    public boolean collidesWith(TiledLayer t,boolean pixel){ return false; }
    public boolean collidesWith(Image img,int x,int y,boolean pixel){ return false; }
    public void defineReferencePixel(int x,int y){}
    public void defineCollisionRectangle(int x,int y,int w,int h){}
    public void setVisible(boolean v){ super.setVisible(v); }
    @Override public void paint(Graphics g){ if (img!=null) g.drawImage(img, x, y, 0); }
}
