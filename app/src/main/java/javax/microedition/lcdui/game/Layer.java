package javax.microedition.lcdui.game;
public abstract class Layer {
    protected int x, y, width, height;
    protected boolean visible = true;
    public Layer(int w,int h){ width=w; height=h; }
    public int getX(){ return x; }
    public int getY(){ return y; }
    public int getWidth(){ return width; }
    public int getHeight(){ return height; }
    public void setPosition(int x,int y){ this.x=x; this.y=y; }
    public void move(int dx,int dy){ x+=dx; y+=dy; }
    public boolean isVisible(){ return visible; }
    public void setVisible(boolean v){ visible=v; }
    public abstract void paint(javax.microedition.lcdui.Graphics g);
}
