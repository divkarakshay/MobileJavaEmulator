package javax.microedition.lcdui;

public class Canvas extends Displayable {
    public static final int UP = 1;
    public static final int DOWN = 6;
    public static final int LEFT = 2;
    public static final int RIGHT = 5;
    public static final int FIRE = 8;
    public static final int GAME_A = 9;
    public static final int GAME_B = 10;
    public static final int GAME_C = 11;
    public static final int GAME_D = 12;
    public static final int KEY_NUM0 = 48;
    public static final int KEY_NUM1 = 49;
    public static final int KEY_NUM2 = 50;
    public static final int KEY_NUM3 = 51;
    public static final int KEY_NUM4 = 52;
    public static final int KEY_NUM5 = 53;
    public static final int KEY_NUM6 = 54;
    public static final int KEY_NUM7 = 55;
    public static final int KEY_NUM8 = 56;
    public static final int KEY_NUM9 = 57;
    public static final int KEY_STAR = 42;
    public static final int KEY_POUND = 35;

    private GameCanvas gameCanvas;

    public Canvas() {
        super(null);
    }

    public void setGameCanvas(GameCanvas gc) { this.gameCanvas = gc; }

    public int getWidth() { return 240; }
    public int getHeight() { return 320; }
    public boolean isDoubleBuffered() { return true; }
    public boolean hasPointerEvents() { return false; }
    public boolean hasPointerMotionEvents() { return false; }
    public boolean hasRepeatEvents() { return false; }
    public boolean isShown() { return true; }

    public void setFullScreenMode(boolean mode) {}
    public void setScreenMode(int mode) {}
    public int getScreenMode() { return 0; }
    public void setListener(Runnable r) {}
    public void repaint() {
        try {
            com.mobileemulator.j2me.emulation.EmulatorActivity inst = com.mobileemulator.j2me.emulation.EmulatorActivity.instance;
            if (inst != null) inst.runOnUiThread(() -> {
                try {
                    java.lang.reflect.Field f = inst.getClass().getDeclaredField("j2meView");
                    f.setAccessible(true);
                    Object jv = f.get(inst);
                    if (jv instanceof android.view.View) ((android.view.View)jv).invalidate();
                } catch (Exception e) {}
            });
        } catch (Exception e) {}
    }
    public void repaint(int x, int y, int w, int h) { repaint(); }
    public void serviceRepaints() {}
    public void showNotify() {}
    public void hideNotify() {}

    protected void keyPressed(int keyCode) {}
    protected void keyReleased(int keyCode) {}
    protected void keyRepeated(int keyCode) {}
    protected void pointerPressed(int x,int y) {}
    protected void pointerReleased(int x,int y) {}
    protected void pointerDragged(int x,int y) {}
    protected void paint(Graphics g) {}
    protected void sizeChanged(int w,int h) {}

    public int getGameAction(int keyCode) {
        if (keyCode==-1||keyCode==38) return UP;
        if (keyCode==-2||keyCode==40) return DOWN;
        if (keyCode==-3||keyCode==37) return LEFT;
        if (keyCode==-4||keyCode==39) return RIGHT;
        if (keyCode==-5||keyCode==10) return FIRE;
        return 0;
    }
    public int getKeyCode(int gameAction) {
        switch(gameAction){
            case UP: return -1;
            case DOWN: return -2;
            case LEFT: return -3;
            case RIGHT: return -4;
            case FIRE: return -5;
            default: return 0;
        }
    }
    public String getKeyName(int keyCode) { return "Key"+keyCode; }

    public void setBacklight(int level) {}
    public int getBacklight() { return 0; }
    public void setCommandListener(CommandListener l) {}
}