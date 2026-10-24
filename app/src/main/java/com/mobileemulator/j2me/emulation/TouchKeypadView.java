package com.mobileemulator.j2me.emulation;

import android.content.Context;
import android.graphics.*;
import android.view.MotionEvent;
import android.view.View;
// On-screen keypad inspired by j2me-loader touch controls
public class TouchKeypadView extends View {
    private Paint paint = new Paint();
    private AndroidGameCanvas target;
    private RectF[] keys = new RectF[12];
    private String[] labels = {"↑","↓","←","→","OK","L","R","*","#","1","2","3"};
    private int[] codes = {Mobile.KEY_UP, Mobile.KEY_DOWN, Mobile.KEY_LEFT, Mobile.KEY_RIGHT, Mobile.KEY_FIRE, Mobile.KEY_SOFT_LEFT, Mobile.KEY_SOFT_RIGHT, Mobile.KEY_STAR, Mobile.KEY_POUND, Mobile.KEY_1, Mobile.KEY_2, Mobile.KEY_3};

    public TouchKeypadView(Context ctx, AndroidGameCanvas target){
        super(ctx);
        this.target=target;
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
    }

    public void setTarget(AndroidGameCanvas t){ this.target=t; }

    @Override protected void onSizeChanged(int w,int h,int ow,int oh){
        int cols=4, rows=3;
        int kw=w/cols, kh=h/rows;
        for(int i=0;i<12;i++){
            int r=i/cols, c=i%cols;
            keys[i]=new RectF(c*kw, r*kh, (c+1)*kw, (r+1)*kh);
        }
    }

    @Override protected void onDraw(Canvas c){
        if(keys[0]==null) return;
        for(int i=0;i<keys.length;i++){
            paint.setColor(0x66000000);
            c.drawRoundRect(keys[i], 8,8, paint);
            paint.setColor(0xFFFFFFFF);
            paint.setTextSize(24);
            c.drawText(labels[i], keys[i].centerX(), keys[i].centerY()+8, paint);
        }
    }

    private int hit(float x,float y){
        for(int i=0;i<keys.length;i++) if(keys[i]!=null && keys[i].contains(x,y)) return i;
        return -1;
    }

    @Override public boolean onTouchEvent(MotionEvent e){
        int act=e.getAction();
        int idx=hit(e.getX(), e.getY());
        if(idx>=0 && target!=null){
            int code = codes[idx];
            boolean pressed = act==MotionEvent.ACTION_DOWN || act==MotionEvent.ACTION_MOVE;
            // translate via Mobile
            int emuKey = code;
            // Simulate key press on Mobile singleton
            Mobile.getInstance().setKey(emuKey, pressed);
        }
        if(act==MotionEvent.ACTION_UP && target!=null){
            // reset all
            for(int code: codes) Mobile.getInstance().setKey(code,false);
        }
        return true;
    }
}