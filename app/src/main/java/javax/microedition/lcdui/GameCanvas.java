package javax.microedition.lcdui;

public class GameCanvas extends Canvas {
    public static final int UP_PRESSED = 1;
    public static final int DOWN_PRESSED = 2;
    public static final int LEFT_PRESSED = 4;
    public static final int RIGHT_PRESSED = 8;
    public static final int FIRE_PRESSED = 16;
    public static final int GAME_A_PRESSED = 32;
    public static final int GAME_B_PRESSED = 64;
    public static final int GAME_C_PRESSED = 128;
    public static final int GAME_D_PRESSED = 256;

    protected Graphics graphics;

    public GameCanvas(boolean suppressKeyEvents) {
        super();
    }

    public Graphics getGraphics() {
        return graphics;
    }

    public int getKeyStates() {
        return 0;
    }

    public void flushGraphics() {}
    public void flushGraphics(int x, int y, int width, int height) {}
}