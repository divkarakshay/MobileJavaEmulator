package javax.microedition.lcdui;

import java.io.IOException;
import java.io.InputStream;

public class Image {
    public static final int MODE_RGB = 0;
    public static final int MODE_RGBA = 1;
    public static final int FILTER_NONE = 0;
    public static final int FILTER_BILINEAR = 1;

    private int width;
    private int height;
    private int[] pixels;
    private boolean mutable;

    public Image(int width, int height) {
        this.width = width;
        this.height = height;
        this.pixels = new int[width * height];
        this.mutable = true;
    }

    public static Image createImage(int width, int height) {
        return new Image(width, height);
    }

    public static Image createImage(String name) throws IOException {
        // Load from resources via classloader
        InputStream is = Image.class.getResourceAsStream(name);
        if (is == null) is = Image.class.getResourceAsStream("/"+name);
        if (is == null) {
            // Try via MIDlet classloader
            try {
                is = Thread.currentThread().getContextClassLoader().getResourceAsStream(name.replaceFirst("^/",""));
                if (is == null) is = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
            } catch (Exception e) {}
        }
        if (is == null) return new Image(1,1);
        try { is.close(); } catch (Exception e) {}
        return new Image(32,32);
    }

    public static Image createImage(byte[] data, int offset, int length) {
        return new Image(32,32);
    }

    public static Image createImage(Image source) {
        Image img = new Image(source.width, source.height);
        System.arraycopy(source.pixels, 0, img.pixels, 0, source.pixels.length);
        return img;
    }

    public static Image createImage(Image image, int x, int y, int width, int height, int transform) {
        Image img = new Image(width, height);
        // Simple copy region
        for (int yy=0; yy<height; yy++) {
            for (int xx=0; xx<width; xx++) {
                int sx = x+xx, sy = y+yy;
                if (sx>=0 && sx<image.width && sy>=0 && sy<image.height) {
                    img.pixels[yy*width+xx] = image.pixels[sy*image.width+sx];
                }
            }
        }
        return img;
    }

    public static Image createImage(InputStream stream) throws IOException {
        return new Image(32,32);
    }

    public static Image createRGBImage(int[] rgb, int width, int height, boolean processAlpha) {
        Image img = new Image(width, height);
        System.arraycopy(rgb, 0, img.pixels, 0, Math.min(rgb.length, img.pixels.length));
        return img;
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getRGB(int x, int y) { 
        if (x<0||x>=width||y<0||y>=height) return 0;
        return pixels[y * width + x]; 
    }
    public void setRGB(int x, int y, int rgb) { 
        if (x<0||x>=width||y<0||y>=height) return;
        pixels[y * width + x] = rgb; 
    }
    public void getRGB(int[] rgbData, int offset, int scanlength, int x, int y, int width, int height) {
        for (int yy=0; yy<height; yy++) {
            for (int xx=0; xx<width; xx++) {
                int sx=x+xx, sy=y+yy;
                if (sx>=0 && sx<this.width && sy>=0 && sy<this.height) {
                    rgbData[offset+yy*scanlength+xx] = pixels[sy*this.width+sx];
                }
            }
        }
    }
    public Graphics getGraphics() { return new Graphics(); }
    public boolean isMutable() { return mutable; }
    public static Image createImage(int[] rgb, int w, int h, boolean alpha) { return createRGBImage(rgb,w,h,alpha); }
}