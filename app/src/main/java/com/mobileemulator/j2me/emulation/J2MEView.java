package com.mobileemulator.j2me.emulation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;

public class J2MEView extends View {
    private Displayable current;
    private Bitmap bitmap;
    private android.graphics.Canvas androidCanvas;
    private AndroidPlatformGraphics g;

    public J2MEView(Context ctx) {
        super(ctx);
        bitmap = Bitmap.createBitmap(240, 320, Bitmap.Config.ARGB_8888);
        androidCanvas = new Canvas(bitmap);
        g = new AndroidPlatformGraphics(240, 320);
        g.setCanvas(androidCanvas);
        g.setFrameBuffer(bitmap);
    }

    public void setDisplayable(Displayable d) {
        this.current = d;
        invalidate();
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmap != null) {
            // Clear
            androidCanvas.drawColor(0xFF000000);
            // Try to call Displayable's paint via reflection if it's a Canvas
            if (current instanceof javax.microedition.lcdui.Canvas) {
                try {
                    javax.microedition.lcdui.Canvas c = (javax.microedition.lcdui.Canvas) current;
                    // Use reflection to call protected paint(Graphics)
                    java.lang.reflect.Method m = javax.microedition.lcdui.Canvas.class.getDeclaredMethod("paint", Graphics.class);
                    m.setAccessible(true);
                    m.invoke(c, g);
                    // Also try to handle GameCanvas flush
                } catch (Exception e) {
                    android.util.Log.w("J2MEView","paint failed: "+e, e);
                    Throwable cause = e.getCause();
                    if (cause != null) android.util.Log.w("J2MEView","cause: "+cause, cause);
                    // Fallback draw text
                    android.graphics.Paint p = new android.graphics.Paint();
                    p.setColor(0xFFFFFFFF);
                    p.setTextSize(20);
                    androidCanvas.drawText("J2ME "+current.getClass().getName(), 10, 30, p);
                    androidCanvas.drawText(""+current.getTitle(), 10, 60, p);
                    androidCanvas.drawText("paint err: "+e.getMessage(), 10, 90, p);
                }
            } else if (current != null) {
                android.graphics.Paint p = new android.graphics.Paint();
                p.setColor(0xFFFFFFFF);
                p.setTextSize(18);
                androidCanvas.drawText("Displayable: "+current.getClass().getName(), 10, 30, p);
            }
            canvas.drawBitmap(bitmap, 0, 0, null);
        }
    }

    public void refresh() { invalidate(); }
}