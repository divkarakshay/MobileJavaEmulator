package com.mobileemulator.j2me.emulation;

import android.content.Context;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import javax.microedition.midlet.MIDlet;

public class AndroidGameCanvas extends View {
    private AndroidPlatformGraphics graphics;
    private MIDlet midi;
    private int emulatorKeys = 0;
    private boolean[] keyStates = new boolean[12];

    public AndroidGameCanvas(Context context, MIDlet midi, int width, int height) {
        super(context);
        this.midi = midi;
        this.graphics = new AndroidPlatformGraphics(width, height);
    }

    public AndroidPlatformGraphics getGraphics() {
        return graphics;
    }

    public void processKeyEvent(int keyCode, boolean pressed) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                if (pressed) emulatorKeys |= Mobile.KEY_UP; else emulatorKeys &= ~Mobile.KEY_UP;
                keyStates[0] = pressed;
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (pressed) emulatorKeys |= Mobile.KEY_DOWN; else emulatorKeys &= ~Mobile.KEY_DOWN;
                keyStates[1] = pressed;
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                if (pressed) emulatorKeys |= Mobile.KEY_LEFT; else emulatorKeys &= ~Mobile.KEY_LEFT;
                keyStates[2] = pressed;
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if (pressed) emulatorKeys |= Mobile.KEY_RIGHT; else emulatorKeys &= ~Mobile.KEY_RIGHT;
                keyStates[3] = pressed;
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                if (pressed) emulatorKeys |= Mobile.KEY_FIRE; else emulatorKeys &= ~Mobile.KEY_FIRE;
                keyStates[4] = pressed;
                break;
            case KeyEvent.KEYCODE_BACK:
                if (pressed) emulatorKeys |= Mobile.KEY_SOFT_LEFT; else emulatorKeys &= ~Mobile.KEY_SOFT_LEFT;
                keyStates[5] = pressed;
                break;
        }
    }

    public boolean isKeyPressed(int keyCode) {
        if (keyCode >= 0 && keyCode < keyStates.length) return keyStates[keyCode];
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();
        int w = getWidth();
        int h = getHeight();
        if (w == 0 || h == 0) return super.onTouchEvent(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (y > h * 3 / 4) {
                    emulatorKeys |= Mobile.KEY_FIRE;
                    keyStates[4] = true;
                }
                if (x < w / 3) {
                    emulatorKeys |= Mobile.KEY_SOFT_LEFT;
                    keyStates[5] = true;
                }
                if (x > w * 2 / 3) {
                    emulatorKeys |= Mobile.KEY_SOFT_RIGHT;
                    keyStates[6] = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                emulatorKeys &= ~Mobile.KEY_FIRE;
                keyStates[4] = false;
                emulatorKeys &= ~Mobile.KEY_SOFT_LEFT;
                keyStates[5] = false;
                emulatorKeys &= ~Mobile.KEY_SOFT_RIGHT;
                keyStates[6] = false;
                break;
        }
        return true;
    }

    public void resetKeys() {
        emulatorKeys = 0;
        for (int i = 0; i < keyStates.length; i++) keyStates[i] = false;
    }

    public int getEmulatorKeys() { return emulatorKeys; }
}