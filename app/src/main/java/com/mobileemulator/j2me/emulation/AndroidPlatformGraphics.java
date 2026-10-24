package com.mobileemulator.j2me.emulation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class AndroidPlatformGraphics extends javax.microedition.lcdui.Graphics {
    private Canvas canvas;
    private Bitmap frameBuffer;
    private int width, height;
    private int rgbDefault;
    private int clipX, clipY, iClipW, iClipHeight;

    public AndroidPlatformGraphics(int width, int height) {
        this.width = width;
        this.height = height;
        this.rgbDefault = Color.WHITE;
        this.clipX = 0;
        this.clipY = 0;
        this.iClipW = width;
        this.iClipHeight = height;
        this.frameBuffer = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        this.canvas = new Canvas(frameBuffer);
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public Bitmap getFrameBuffer() {
        return frameBuffer;
    }

    public void setFrameBuffer(Bitmap bitmap) {
        this.frameBuffer = bitmap;
        if (bitmap != null) {
            this.canvas = new Canvas(bitmap);
            this.width = bitmap.getWidth();
            this.height = bitmap.getHeight();
        }
    }

    public Canvas getCanvas() { return canvas; }

    @Override
    public void setColor(int color) {
        this.rgbDefault = color;
    }

    public int getColor() {
        return rgbDefault;
    }

    @Override
    public void setClip(int x, int y, int w, int h) {
        this.clipX = x;
        this.clipY = y;
        this.iClipW = w;
        this.iClipHeight = h;
        if (canvas != null) canvas.clipRect(x, y, x+w, y+h);
    }

    @Override
    public int getClipX() { return clipX; }
    @Override
    public int getClipY() { return clipY; }
    @Override
    public int getClipWidth() { return iClipW; }
    @Override
    public int getClipHeight() { return iClipHeight; }

    public int getDisplayWidth() { return width; }
    public int getScreenHeight() { return height; }

    @Override
    public void fillRect(int x, int y, int w, int h) {
        if (canvas != null) {
            Paint p = new Paint();
            p.setColor(rgbDefault);
            p.setStyle(Paint.Style.FILL);
            canvas.drawRect(x, y, x+w, y+h, p);
        }
    }

    @Override
    public void drawRect(int x,int y,int w,int h) {
        if (canvas != null) {
            Paint p = new Paint();
            p.setColor(rgbDefault);
            p.setStyle(Paint.Style.STROKE);
            canvas.drawRect(x, y, x+w, y+h, p);
        }
    }

    @Override
    public void drawLine(int x1,int y1,int x2,int y2) {
        if (canvas != null) {
            Paint p = new Paint();
            p.setColor(rgbDefault);
            canvas.drawLine(x1, y1, x2, y2, p);
        }
    }

    @Override
    public void drawImage(javax.microedition.lcdui.Image img,int x,int y,int anchor) {
        if (canvas != null && img != null) {
            // Anchor handling simplified
            int ax = x, ay = y;
            if ((anchor & HCENTER) != 0) ax -= img.getWidth()/2;
            else if ((anchor & RIGHT) != 0) ax -= img.getWidth();
            if ((anchor & VCENTER) != 0) ay -= img.getHeight()/2;
            else if ((anchor & BOTTOM) != 0) ay -= img.getHeight();
            Paint p = new Paint();
            p.setColor(rgbDefault);
            // Draw placeholder rect for image
            canvas.drawRect(ax, ay, ax+img.getWidth(), ay+img.getHeight(), p);
            // Try to draw actual image if it has bitmap
            try {
                java.lang.reflect.Field f = img.getClass().getDeclaredField("bitmap");
                f.setAccessible(true);
                Object bmp = f.get(img);
                if (bmp instanceof Bitmap) canvas.drawBitmap((Bitmap)bmp, ax, ay, null);
            } catch (Exception e) {}
        }
    }

    @Override
    public void drawRGB(int[] rgbData,int offset,int scanlength,int x,int y,int width,int height,boolean processAlpha) {
        if (canvas != null && rgbData != null) {
            for (int yy=0; yy<height; yy++) {
                for (int xx=0; xx<width; xx++) {
                    int col = rgbData[offset+yy*scanlength+xx];
                    if (frameBuffer != null && x+xx>=0 && x+xx<this.width && y+yy>=0 && y+yy<this.height) {
                        frameBuffer.setPixel(x+xx, y+yy, col);
                    }
                }
            }
        }
    }

    public void drawRGB(int x, int y, int color) {
        if (canvas != null && frameBuffer != null && x>=0 && x<width && y>=0 && y<height) {
            frameBuffer.setPixel(x, y, color);
        }
    }

    @Override
    public void drawString(String text, int x, int y, int anchor) {
        if (canvas != null) {
            Paint paint = new Paint();
            paint.setColor(rgbDefault);
            paint.setAntiAlias(true);
            paint.setTextSize(12);
            canvas.drawText(text, x, y, paint);
        }
    }

    @Override
    public int getRGB(int x, int y) {
        if (frameBuffer != null && x >= 0 && x < width && y >= 0 && y < height) {
            return frameBuffer.getPixel(x, y);
        }
        return 0;
    }

    public void setRGB(int x, int y, int rgb) {
        if (frameBuffer != null && x >= 0 && x < width && y >= 0 && y < height) {
            frameBuffer.setPixel(x, y, rgb);
        }
    }
}