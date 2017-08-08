package com.example.ai.dtest.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by ai on 2017/7/25.
 */

public class eye extends View {

    private Paint eyeOutPaint;

    private Paint eyeInPaint;

    private boolean close=true;

    private boolean open=false;

    private boolean open_close= false;

    private float width,height;

    private ValueAnimator animatorOpen;

    private ValueAnimator animatorClose;

    private openListener listener;

    public eye(Context context) {
        super(context);
        initPaint();
    }

    public void setListener(openListener listener){
        this.listener=listener;
    }

    public eye(final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        initAnima();
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(close){
                    close=false;
                    open=true;
                    open_close=false;
                    postInvalidate();
                    listener.openEye();
                    animatorOpen.start();
                }else {
                    if(animatorOpen.isRunning()){
                        animatorOpen.pause();
                    }
                    if(animatorClose.isRunning()){
                        animatorClose.pause();
                    }
                    open=false;
                    close=true;
                    open_close=false;
                    postInvalidate();
                    listener.closeEye();
                }
            }
        });
    }

    private void initPaint(){
        eyeOutPaint= new Paint();
        eyeOutPaint.setColor(0xff969696);
        eyeOutPaint.setStrokeWidth(dp2px(1));
        eyeOutPaint.setAntiAlias(true);
        eyeOutPaint.setStyle(Paint.Style.STROKE);

        eyeInPaint=new Paint();
        eyeInPaint.setColor(0xff969696);
        eyeInPaint.setStrokeWidth(dp2px(1));
        eyeInPaint.setAntiAlias(true);
        eyeInPaint.setStyle(Paint.Style.FILL);
    }


    private void initAnima(){
        animatorOpen=ValueAnimator.ofFloat(1,0);
        animatorOpen.setDuration(2000);

        animatorClose= ValueAnimator.ofFloat(1,0);
        animatorClose.setDuration(400);


        animatorOpen.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                open=false;
                open_close=true;
                postInvalidate();
                animatorClose.start();
            }
        });
        animatorClose.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                open=true;
                open_close=false;
                postInvalidate();
                animatorOpen.start();
            }
        });
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode= MeasureSpec.getMode(widthMeasureSpec);
        int widthSize= MeasureSpec.getSize(widthMeasureSpec);

        int heightMode= MeasureSpec.getMode(heightMeasureSpec);
        int heightSize= MeasureSpec.getSize(heightMeasureSpec);

        int defaultHeight= dp2px(20);

        if(heightMode==MeasureSpec.EXACTLY||heightMode==MeasureSpec.AT_MOST){
            height=defaultHeight;
        }
        if(widthMode==MeasureSpec.EXACTLY||widthMode==MeasureSpec.AT_MOST){
            width=defaultHeight;
        }
        setMeasuredDimension((int) width,(int) height);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(close){
            draw_close(canvas);
        }
        if(open){
            draw_open(canvas);
        }
        if(open_close){
            draw_close(canvas);
        }
    }

    private void draw_close(Canvas canvas){
        canvas.save();
        float length= (float) (height/2*Math.pow(2.0,0.5));
        canvas.translate(width/2,0);
        canvas.rotate(45);
        RectF arc= new RectF(-length,-length,length,length);
        canvas.drawArc(arc,0,90,false,eyeOutPaint);
        for(int i=0;i<9;i++) {
            canvas.rotate(10f);
            canvas.drawLine(length, 0, length + width / 10, 0, eyeOutPaint);
        }
        canvas.restore();
    }

    private void draw_open(Canvas canvas){
        canvas.save();
        float length= (float) (height/2*Math.pow(2.0,0.5));
//        float minLength= (float) (height/8*Math.pow(2.0,0.5));
        canvas.translate(width/2,height);
        canvas.rotate(-135);
        RectF upArc= new RectF(-1*length,-1*length,length,length);
        canvas.drawArc(upArc,0,90,false,eyeOutPaint);
        for(int i=0;i<9;i++) {
            canvas.rotate(10f);
            canvas.drawLine(length, 0, length + width / 10, 0, eyeOutPaint);
        }
        canvas.save();
        canvas.translate(length,-length);
        canvas.drawArc(upArc,90,90,false,eyeOutPaint);
        canvas.restore();
        canvas.rotate(45);
        canvas.translate(0,-1*height/2);
        canvas.drawCircle(0,0,height/8,eyeOutPaint);
        canvas.drawCircle(0,0,height/28,eyeInPaint);
        canvas.restore();
    }

    public interface openListener{
        void openEye();
        void closeEye();
    }

    public void clear(){
        animatorOpen.pause();
        animatorOpen.removeAllListeners();
        animatorOpen=null;
        animatorClose.pause();
        animatorClose.removeAllListeners();
        animatorClose=null;
    }

    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    protected int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, getResources().getDisplayMetrics());
    }
}
