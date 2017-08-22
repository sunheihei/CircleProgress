package com.custom.sun.circleprogresslibrary;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;


/**
 * Created by Sun on 2017/8/18.
 */

public class CircleProgressView extends View {

    public static final String TAG = "CircleProgressView";

    Paint mBigCirclePaint, mSmallCirclePaint, mMainTextPaint,TopTextPaint,BottomTextPaint;

    RectF oval;  //矩形，确定进度位置

    private int mWidth, mHeight;//View的长宽
    private int mRadius; //半径
    private int StrokeWidth;//进度粗细
    private int mProgress;
    private int mArc, mStartArc;//角度
    private int BgCircle, ProgressCircleColor;


    private Boolean SetAnimation = false;  //动画默认关闭
    private long AnimDuration;
    private int StartNum = 0;
    private String AnimTempProgress;

    //MainText
    private float MainTextSize;//字体大小
    private int MainTextStroke;//字体粗细
    private int MainTextColor;

    private String TopText;
    private float TopTextSize;
    private int TopTextStroke;
    private int TopTextColor;

    private String BottomText;
    private float BottomTextSize;
    private int BottomTextStroke;
    private int BottomTextColor;

    //渐变色
    private int FirstColor,SecondColor,ThirdColor;
    private int color[];

    public CircleProgressView(Context context) {
        super(context);
        init();

    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressView);

        mProgress = a.getColor(R.styleable.CircleProgressView_progress, 0);
        StrokeWidth = (int) a.getDimension(R.styleable.CircleProgressView_ProgressCircleStorke, 10);
        BgCircle = a.getColor(R.styleable.CircleProgressView_BgCircleColor, Color.GRAY);
        ProgressCircleColor = a.getColor(R.styleable.CircleProgressView_ProgressCircleColor, Color.GREEN);

        initStartArc(a.getInteger(R.styleable.CircleProgressView_StartArcMode, 0));
        MainTextSize = a.getDimension(R.styleable.CircleProgressView_MainTextSize, 20);
        MainTextColor = a.getColor(R.styleable.CircleProgressView_MainTextColor, Color.BLACK);
        MainTextStroke = (int) a.getDimension(R.styleable.CircleProgressView_MainTextStroke, 5);

        TopText = a.getString(R.styleable.CircleProgressView_TopText);
        TopTextSize = a.getDimension(R.styleable.CircleProgressView_TopTextSize, 20);
        TopTextColor = a.getColor(R.styleable.CircleProgressView_TopTextColor, Color.BLACK);
        TopTextStroke = (int) a.getDimension(R.styleable.CircleProgressView_TopTextStroke, 5);

        BottomText = a.getString(R.styleable.CircleProgressView_BottomText);
        BottomTextSize = a.getDimension(R.styleable.CircleProgressView_BottomTextSize, 20);
        BottomTextColor = a.getColor(R.styleable.CircleProgressView_BottomTextColor, Color.BLACK);
        BottomTextStroke = (int) a.getDimension(R.styleable.CircleProgressView_BottomTextStroke, 5);


        FirstColor = a.getColor(R.styleable.CircleProgressView_FirstColor,Color.GREEN);
        SecondColor = a.getColor(R.styleable.CircleProgressView_SecondColor,Color.YELLOW);
        ThirdColor = a.getColor(R.styleable.CircleProgressView_ThirdColor,Color.RED);

        // 解析后释放资源
        a.recycle();

        init();

    }

    /**
     * 初始化起点角度
     * 0-0
     * 1-90
     * 2-180
     * 3-270
     *
     * @param StartTag
     */
    private void initStartArc(int StartTag) {
        switch (StartTag) {
            case 0:
                mStartArc = 0;
                break;
            case 1:
                mStartArc = 90;
                break;
            case 2:
                mStartArc = 180;
                break;
            case 3:
                mStartArc = 270;
                break;

        }
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getWidth();
        mHeight = getHeight();

        oval = new RectF(StrokeWidth, StrokeWidth, mWidth - StrokeWidth, mHeight - StrokeWidth);

        //初始化渐变色，旋转
        color = new int[]{FirstColor,SecondColor,ThirdColor};
        Shader mSweepGradient = new SweepGradient(mWidth/2, mHeight/2,color,null);
        Matrix mMatrix = new Matrix();
        mMatrix.setRotate(mStartArc-5,mWidth/2, mHeight/2);  //由于使用了Paint.Cap.ROUND，旋转角度略小于真实的旋转角
        mSweepGradient.setLocalMatrix(mMatrix);
        mSmallCirclePaint.setShader(mSweepGradient);

        //动画
        if (SetAnimation) {
            runint();
        }
    }

    private void init() {
        //BgCircle
        mBigCirclePaint = new Paint();
        mBigCirclePaint.setColor(BgCircle);
        mBigCirclePaint.setStrokeWidth(StrokeWidth);
        mBigCirclePaint.setStyle(Paint.Style.STROKE);


        //progress
        mSmallCirclePaint = new Paint();
        mSmallCirclePaint.setColor(ProgressCircleColor);
        mSmallCirclePaint.setStrokeWidth(StrokeWidth);
        mSmallCirclePaint.setStyle(Paint.Style.STROKE);
        mSmallCirclePaint.setStrokeCap(Paint.Cap.ROUND);

        //MainText;
        mMainTextPaint = new Paint();
        mMainTextPaint.setColor(MainTextColor);
        mMainTextPaint.setStrokeWidth(MainTextStroke);
        mMainTextPaint.setAntiAlias(true);
        mMainTextPaint.setTextSize(MainTextSize);
        mMainTextPaint.setStyle(Paint.Style.FILL);
        mMainTextPaint.setTextAlign(Paint.Align.CENTER);

        //Top;
        TopTextPaint = new Paint();
        TopTextPaint.setColor(TopTextColor);
        TopTextPaint.setStrokeWidth(TopTextStroke);
        TopTextPaint.setAntiAlias(true);
        TopTextPaint.setTextSize(TopTextSize);
        TopTextPaint.setStyle(Paint.Style.FILL);
        TopTextPaint.setTextAlign(Paint.Align.CENTER);

        //Bottom
        BottomTextPaint = new Paint();
        BottomTextPaint.setColor(BottomTextColor);
        BottomTextPaint.setStrokeWidth(BottomTextStroke);
        BottomTextPaint.setAntiAlias(true);
        BottomTextPaint.setTextSize(BottomTextSize);
        BottomTextPaint.setStyle(Paint.Style.FILL);
        BottomTextPaint.setTextAlign(Paint.Align.CENTER);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mRadius = getWidth() / 2 - StrokeWidth;
        //绘制外圆
        canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius, mBigCirclePaint);

        //绘制进度
        if (!SetAnimation) {
            mArc = (int) (360 * ((float) mProgress / (float) 100));
        }
        canvas.drawArc(oval, mStartArc, mArc, false, mSmallCirclePaint);


        //绘制Maintext
        Paint.FontMetricsInt fontMetrics = mMainTextPaint.getFontMetricsInt();
        int baseline = (int) ((oval.bottom + oval.top - fontMetrics.bottom - fontMetrics.top) / 2); //字体垂直方向居中
        //如果动画false则直接描绘进度
        if (!SetAnimation) {
            canvas.drawText(mProgress + "%", mWidth / 2, baseline, mMainTextPaint);
        } else {
            canvas.drawText(AnimTempProgress + "%", mWidth / 2, baseline, mMainTextPaint);
        }

        //绘制top,bottom;
        if (TopText!=null){
            canvas.drawText(TopText,mWidth/2,mHeight/2-mHeight/5,TopTextPaint);
        }
        if (BottomText!=null){
            canvas.drawText(BottomText,mWidth/2,mHeight/2+mHeight/5,BottomTextPaint);
        }
    }

    //intAnim
    private void runint() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(StartNum, mProgress);
        valueAnimator.setDuration(AnimDuration);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mArc = (int) (360 * (Float.parseFloat(animation.getAnimatedValue().toString()) / (float) 100));
                AnimTempProgress = animation.getAnimatedValue().toString();
                postInvalidate();
            }
        });
        valueAnimator.start();
    }


    /**
     * setprogress  1-100
     */

    public void SetProgress(int progress) {
        this.mProgress = progress;
    }

    /**
     * SetAnimation  Boolean
     */

    public void SetAnimation(Boolean SetAnimation) {
        this.SetAnimation = SetAnimation;
    }

    /**
     * SetAnimationDuration
     * StartNum   default 0
     */

    public void SetAnimationDuration(long duration,int startnum) {
        this.AnimDuration = duration;
        this.StartNum = startnum;
    }
}
