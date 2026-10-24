package com.mobileemulator.j2me.emulation;

public class Mobile {
    public static final int KEY_UP = 1;
    public static final int KEY_DOWN = 2;
    public static final int KEY_LEFT = 3;
    public static final int KEY_RIGHT = 4;
    public static final int KEY_FIRE = 5;
    public static final int KEY_SOFT_LEFT = 6;
    public static final int KEY_SOFT_RIGHT = 7;
    public static final int KEY_STAR = 8;
    public static final int KEY_POUND = 9;
    public static final int KEY_0 = 10;
    public static final int KEY_1 = 11;
    public static final int KEY_2 = 12;
    public static final int KEY_3 = 13;
    public static final int KEY_4 = 14;
    public static final int KEY_5 = 15;
    public static final int KEY_6 = 16;
    public static final int KEY_7 = 17;
    public static final int KEY_8 = 18;
    public static final int KEY_9 = 19;

    private static Mobile instance;
    private int[] keys = new int[20];
    private boolean[] keysPressed = new boolean[20];
    private int dpi = 160;
    private String deviceProfile = "nokia";

    public static Mobile getInstance() {
        if (instance == null) instance = new Mobile();
        return instance;
    }

    public void setKey(int keyCode, boolean pressed) {
        if (keyCode >= 0 && keyCode < keys.length) {
            keys[keyCode] = pressed ? 1 : 0;
            keysPressed[keyCode] = pressed;
        }
    }

    public int getKey(int keyCode) {
        return keys[keyCode];
    }

    public boolean isKeyPressed(int keyCode) {
        return keysPressed[keyCode];
    }

    public void resetKeys() {
        for (int i = 0; i < keysPressed.length; i++) {
            keysPressed[i] = false;
        }
    }

    public int getDPI() { return dpi; }
    public void setDPI(int dpi) { this.dpi = dpi; }
    public String getDeviceProfile() { return deviceProfile; }
    public void setDeviceProfile(String profile) { this.deviceProfile = profile; }

    public int getKeyCode(int emulatorKey) {
        switch (emulatorKey) {
            case KEY_UP: return 38;   // DPAD_UP
            case KEY_DOWN: return 40; // DPAD_DOWN
            case KEY_LEFT: return 37; // DPAD_LEFT
            case KEY_RIGHT: return 39; // DPAD_RIGHT
            case KEY_FIRE: return 66; // ENTER
            case KEY_SOFT_LEFT: return 187; // SOFT_LEFT
            case KEY_SOFT_RIGHT: return 188; // SOFT_RIGHT
            case KEY_STAR: return 52; // *
            case KEY_POUND: return 53; // #
            case KEY_0: return 79;
            case KEY_1: return 81;
            case KEY_2: return 82;
            case KEY_3: return 83;
            case KEY_4: return 75;
            case KEY_5: return 76;
            case KEY_6: return 77;
            case KEY_7: return 78;
            case KEY_8: return 32;
            case KEY_9: return 33;
            default: return 0;
        }
    }
}