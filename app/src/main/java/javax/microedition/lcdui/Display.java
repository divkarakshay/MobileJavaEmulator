package javax.microedition.lcdui;

import javax.microedition.midlet.MIDlet;

public class Display {
    private MIDlet midlet;
    private Displayable current;

    public Display(MIDlet midlet) {
        this.midlet = midlet;
    }

    public static Display getDisplay(MIDlet m) {
        return new Display(m);
    }

    // Compatibility overload for Obfuscated games that may pass Object
    public static Display getDisplay(Object o) {
        if (o instanceof MIDlet) return new Display((MIDlet)o);
        return new Display(null);
    }

    public static Displayable lastDisplayable;
    public void setCurrent(Displayable d) { 
        this.current = d; 
        lastDisplayable = d;
        android.util.Log.i("Display","setCurrent: "+(d!=null?d.getClass().getName():"null")+" title="+ (d!=null?d.getTitle():"null"));
        try {
            com.mobileemulator.j2me.emulation.EmulatorActivity inst = com.mobileemulator.j2me.emulation.EmulatorActivity.instance;
            if (inst != null) {
                android.util.Log.i("Display","Forwarding to EmulatorActivity J2MEView");
                inst.runOnUiThread(() -> inst.showDisplayable(d));
            }
        } catch (Exception e) { android.util.Log.w("Display","forward failed",e); }
    }
    public void setCurrent(Alert alert, Displayable next) { this.current = alert; android.util.Log.i("Display","setCurrent Alert"); }
    public Displayable getCurrent() { return current; }

    public void setCurrent(Canvas c) { this.current = c; } // legacy

    public int getHeight() { return 240; }
    public int getWidth() { return 320; }
    public int getBestImageWidth(int type) { return 320; }
    public int getBestImageHeight(int type) { return 240; }
    public int numColors() { return 65536; }
    public boolean isColor() { return true; }
    public void callSerially(Runnable r) { r.run(); }
    public boolean vibrate(int duration) { return false; }
    public boolean flashBacklight(int duration) { return false; }
}