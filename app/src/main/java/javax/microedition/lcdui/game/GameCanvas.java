package javax.microedition.lcdui.game;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
public abstract class GameCanvas extends Canvas {
    public static final int UP_PRESSED=1, DOWN_PRESSED=2, LEFT_PRESSED=4, RIGHT_PRESSED=8, FIRE_PRESSED=16, GAME_A_PRESSED=32, GAME_B_PRESSED=64, GAME_C_PRESSED=128, GAME_D_PRESSED=256;
    protected Graphics graphics;
    public GameCanvas(boolean suppressKeyEvents){ super(); }
    public Graphics getGraphics(){ return new Graphics(); }
    public int getKeyStates(){ return 0; }
    public void flushGraphics(){}
    public void flushGraphics(int x,int y,int w,int h){}
}
