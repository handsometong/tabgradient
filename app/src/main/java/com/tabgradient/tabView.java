package com.tabgradient;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * 作者：hst create on 2016/12/28 16:10
 */
public class tabView extends View {

    /**
     * * 文本
     */
    private String mText;
    /**
     * 文本的颜色
     */
    private int mTextColor;
    /**
     * 文本的大小
     */
    private int mTextSize;
    /**
     * 原始颜色
     */
    private int mOriginColor;

    /**
     * 变化颜色
     */
    private int mChangeColor;

    private Paint mPaint;

    Rect mTextBound = new Rect();

    private int mRealWidth;
    private int mTextStartX;
    private int mTextWidth;
    private static final int DIRECTION_LEFT = 0;
    private static final int DIRECTION_RIGHT = 1;
    //以下定义一些默认的变量
    private int mDirection = DIRECTION_LEFT;
    private float mProgress; // 进度，从0到1之间取值

    public tabView(Context context) {
        super(context);
    }

    public tabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        /**
         * 获得我们所定义的自定义样式属性
         */
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TabView);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.TabView_titleText:
                    mText = a.getString(attr);
                    break;
                case R.styleable.TabView_titleTextColor1:
                    mTextColor = a.getColor(attr, mTextColor);
                    break;
                case R.styleable.TabView_titleTextSize:
                    mTextSize = a.getDimensionPixelSize(attr, mTextSize);
                    break;
                case R.styleable.TabView_changeColor:
                    mChangeColor = a.getColor(attr, mChangeColor);
                    break;
                case R.styleable.TabView_originColor:
                    mOriginColor = a.getColor(attr, mOriginColor);
                    break;

            }

        }
        a.recycle();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(mTextSize);


    }

    @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasuredWidth(widthMeasureSpec);
        int height = MeasuredHeight(heightMeasureSpec);
        setMeasuredDimension(width, height);
        mTextWidth = (int) mPaint.measureText(mText);
        //这个函数可以获取最小的包裹文字矩形，赋值到mTextBound
        mPaint.getTextBounds(mText, 0,mText.length(), mTextBound);
        mRealWidth=getMeasuredWidth()-getPaddingRight()-getPaddingLeft();
        mTextStartX=mRealWidth/2-mTextWidth/2;

    }

    private int MeasuredHeight(int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        int result = 0;
        switch (mode) {

            case MeasureSpec.EXACTLY:
                result = size;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                result=mTextBound.height();
                break;

        }
        result =mode==MeasureSpec.AT_MOST ? Math.min(size,result) : result;
        return  result+getPaddingBottom()+getPaddingTop();
    }

    private int MeasuredWidth(int widthMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int result = 0;
        switch (mode) {

            case MeasureSpec.EXACTLY:
                result = size;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                result=mTextBound.width();
                break;

        }
        result =mode==MeasureSpec.AT_MOST ? Math.min(size,result) : result;
        return  result+getPaddingLeft()+getPaddingRight();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (mDirection==DIRECTION_LEFT)
        {
            drawChangeLeft(canvas);
            drawOriginLeft(canvas);

        }
        else if (mDirection==DIRECTION_RIGHT)
        {
            drawChangeRight(canvas);
            drawOriginRight(canvas);
        }
    }

    private void drawOriginRight(Canvas canvas) {

        drawText(canvas,mOriginColor,mTextStartX,(int)(mTextStartX+mTextWidth*(1-mProgress)));
    }


    private void drawChangeRight(Canvas canvas) {
        drawText(canvas,mChangeColor,(int)(mTextStartX+mTextWidth*(1-mProgress)),mTextStartX+mTextWidth);
    }

    private void drawOriginLeft(Canvas canvas) {
        drawText(canvas,mOriginColor,(int)(mTextStartX+mTextWidth*mProgress),mTextStartX+mTextWidth);
    }

    private void drawChangeLeft(Canvas canvas) {
        drawText(canvas,mChangeColor,mTextStartX,(int)(mTextStartX+mTextWidth*mProgress));
    }


    private void drawText(Canvas canvas, int color, int startX, int endX) {
        mPaint.setColor(color);
        canvas.save(Canvas.CLIP_SAVE_FLAG);
        canvas.clipRect(startX, 0, endX, getMeasuredHeight());
        canvas.drawText((String) mText, mTextStartX, getMeasuredHeight()
                / 2 + mTextBound.height() / 2, mPaint);
        canvas.restore();
        mPaint.setColor(color);
        canvas.save(Canvas.ALL_SAVE_FLAG);

    }
    public float getProgress() {
        return mProgress;
    }

    public void setProgress(float mProgress) {
        this.mProgress = mProgress;
        invalidate();//改变进度，需要重绘，实现字体渐变的效果
    }

    public int getDirection() {
        return mDirection;
    }

    public void setDirection(int mDirection) {
        this.mDirection = mDirection;
        invalidate(); //重新改变方向，需要重绘
    }

}
