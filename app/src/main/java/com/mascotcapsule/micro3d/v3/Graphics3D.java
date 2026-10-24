package com.mascotcapsule.micro3d.v3;
// MascotCapsule V3 from freej2me-plus (MIT) - command-list deferred renderer
// Fixed-point 12-bit rasterizer, original by Roman Lahin. Stub for Android.
import android.graphics.Bitmap;
public class Graphics3D {
    public static final int COMMAND_LIST_VERSION_1_0=0;
    private Bitmap target;
    public void bind(Bitmap bmp){ target=bmp; }
    public void bind(Bitmap bmp, int x,int y,int w,int h){}
    public void release(Bitmap bmp){}
    public void clear(){}
    public void flush(){}
    public void renderFigure(Figure f,int x,int y,int z){}
    public void renderPrimitives(Figure f,int x,int y,int z){}
    public void drawCommandList(CommandList l){}
}
