package com.example.ai.dtest.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import com.example.ai.dtest.R;
import com.example.ai.dtest.base.MyApplication;

/**
 * Created by ai on 2017/8/5.
 */

public class load extends View {

    private Paint circlePaint;

    private Paint fillPaint;

    private Paint pathPaint;

    private Path okPath;

    private DashPathEffect effect;

    private float length;

    private float width,height;

    private float M;

    private float R;

    private int defaultHeight;

    private Context context;

    private float circleH;

    private boolean isOk=false;

    private ValueAnimator animatorLoadCircle;

    public load(Context context) {
        super(context);
        this.context=context;
        initPaint();
        initData();
    }

    public load(Context context, @Nullable AttributeSet attrs) {
        super(context,attrs);
        this.context=context;
        initPaint();
        initData();
    }

    private void initPaint(){
        circlePaint= new Paint();
        circlePaint.setColor(MyApplication.getContext().getResources().getColor(com.example.ai.dtest.R.color.colorPrimary));
        circlePaint.setStrokeWidth(dp2px(2));
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.STROKE);

        fillPaint= new Paint();
        fillPaint.setColor(MyApplication.getContext().getResources().getColor(com.example.ai.dtest.R.color.colorPrimary));
        fillPaint.setStrokeWidth(dp2px(2));
        fillPaint.setAntiAlias(true);
        fillPaint.setStyle(Paint.Style.FILL);

        pathPaint= new Paint();
        pathPaint.setColor(Color.WHITE);
        pathPaint.setStrokeWidth(dp2px(1));
        pathPaint.setAntiAlias(true);
        pathPaint.setStyle(Paint.Style.STROKE);
    }

    private void initData(){
        defaultHeight= dp2px(32);
        float sureHeight = dp2px(30);
        M= (defaultHeight- sureHeight)/2;
        R= sureHeight /2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode= MeasureSpec.getMode(widthMeasureSpec);
        int widthSize= MeasureSpec.getSize(widthMeasureSpec);

        int heightMode= MeasureSpec.getMode(heightMeasureSpec);
        int heightSize= MeasureSpec.getSize(heightMeasureSpec);

        if(heightMode==MeasureSpec.EXACTLY||heightMode==MeasureSpec.AT_MOST){
            height=defaultHeight;
        }
        if(widthMode==MeasureSpec.EXACTLY||widthMode==MeasureSpec.AT_MOST){
            width=defaultHeight;
        }
        initPath();
        setMeasuredDimension((int) width,(int) height);
    }

    private void initPath(){
        okPath= new Path();
        okPath.moveTo(height / 4 * 1.2f, height / 2);
        okPath.lineTo(height / 2, height / 3 * 2);
        okPath.lineTo(height / 4 * 2.9f, height / 4 * 1.5f);
        PathMeasure okMeasure = new PathMeasure(okPath, false);
        length= okMeasure.getLength();
    }

    public void loadAnima(){
        animatorLoadCircle= ValueAnimator.ofFloat(1.4f*R+M,M);
        animatorLoadCircle.setDuration(1000);
        animatorLoadCircle.setRepeatCount(ValueAnimator.INFINITE);
        animatorLoadCircle.setRepeatMode(ValueAnimator.REVERSE);
//        animatorLoadCircle.setStartDelay();
        animatorLoadCircle.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                circleH= (float)(valueAnimator.getAnimatedValue());
                postInvalidate();
            }
        });
        animatorLoadCircle.start();
    }

    public void clearAnima(){
        if(animatorLoadCircle!=null&&animatorLoadCircle.isRunning()){
            animatorLoadCircle.pause();
            animatorLoadCircle.end();
            animatorLoadCircle=null;
        }
    }

    private void OkAnima(){
        if(animatorLoadCircle.isRunning()){
            animatorLoadCircle.pause();
            animatorLoadCircle.end();
            animatorLoadCircle=null;
        }
        ValueAnimator animatorOkPath = ValueAnimator.ofFloat(length, 0);
        animatorOkPath.setDuration(1000);
        animatorOkPath.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                isOk=true;
                effect= new DashPathEffect(new float[]{length,length},(float) valueAnimator.getAnimatedValue());
                pathPaint.setPathEffect(effect);
                postInvalidate();
            }
        });
        animatorOkPath.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

            }
        });
        animatorOkPath.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(height / 2, height / 2, R, circlePaint);
        drawLoad(canvas);
    }

    private void drawLoad(Canvas canvas){
        Path circlePath = new Path();
        RectF rectF= new RectF(M,M,M+2*R,M+2*R);
        if(circleH>=R+M) {
            float sBaseX = (float) (R + M - Math.sqrt(Math.pow(R, 2.0) - Math.pow((circleH-R-M), 2.0)));
            float eBaseX= (float) (R + M + Math.sqrt(Math.pow(R, 2.0) - Math.pow((circleH-R-M), 2.0)));
            float baseY= circleH;
            circlePath.moveTo(sBaseX,baseY);
            float C1_X= (float) (sBaseX+Math.sqrt(Math.pow(R, 2.0) - Math.pow((circleH-R-M), 2.0))/3*2);
            float C1_Y= circleH-R*1/4;
            float C2_X= (float) (eBaseX-Math.sqrt(Math.pow(R, 2.0) - Math.pow((circleH-R-M), 2.0))/3*2);
            float C2_Y= circleH+R*1/4;
            circlePath.cubicTo(C1_X,C1_Y,C2_X,C2_Y,eBaseX,baseY);
//            circlePath.lineTo(eBaseX,baseY);
            float angle= (float) (Math.acos((circleH-R-M)/R)*180/Math.PI);
            circlePath.addArc(rectF,90-angle,2*angle);
            canvas.drawPath(circlePath,fillPaint);
        }else {
            float sBaseX = (float) (R + M - Math.sqrt(Math.pow(R, 2.0) - Math.pow((R+M-circleH), 2.0)));
            float eBaseX= (float) (R + M + Math.sqrt(Math.pow(R, 2.0) - Math.pow((R+M-circleH), 2.0)));
            float baseY= circleH;
            circlePath.moveTo(sBaseX,baseY);
            float C1_X= (float) (sBaseX+Math.sqrt(Math.pow(R, 2.0) - Math.pow((circleH-R-M), 2.0))/3*2);
            float C1_Y= circleH-R*1/4;
            float C2_X= (float) (eBaseX-Math.sqrt(Math.pow(R, 2.0) - Math.pow((circleH-R-M), 2.0))/3*2);
            float C2_Y= circleH+R*1/4;
            circlePath.cubicTo(C1_X,C1_Y,C2_X,C2_Y,eBaseX,baseY);
//            circlePath.lineTo(eBaseX,baseY);
            float angle= (float) (Math.acos((R+M-circleH)/R)*180/Math.PI);
            circlePath.addArc(rectF,angle-90,360-2*angle);
            canvas.drawPath(circlePath,fillPaint);
        }
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
