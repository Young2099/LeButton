package com.lanfeng.young.lebutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * 针对单个字母
 */
public class LeImage extends android.support.v7.widget.AppCompatImageView {
    private Paint mPaint;
    private int letterColor, bgColor;
    private float circleSize;
    private boolean isFlag = true;
    private String mLetter;

    public LeImage(Context context) {
        super(context);

    }

    public LeImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        initProperty(context, attrs);
    }


    public LeImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initProperty(context, attrs);
    }

    /**
     * 初始化
     *
     * @param context
     * @param attrs
     */
    private void initProperty(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.letButton);
        letterColor = typedArray.getColor(R.styleable.letButton_letterColor, Color.WHITE);
        bgColor = typedArray.getColor(R.styleable.letButton_bgColor, Color.WHITE);
        circleSize = typedArray.getDimension(R.styleable.letButton_circleBtnSize, 10);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        typedArray.recycle();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        mPaint.setColor(letterColor);
        if (isFlag) {
            mPaint.setStyle(Paint.Style.STROKE);
        }
        RectF rectF = new RectF(getWidth() / 2 - circleSize, getHeight() / 2 - circleSize, getWidth() / 2 + circleSize, getHeight() / 2 + circleSize);
        canvas.drawRoundRect(rectF, getWidth() / 2, getHeight() / 2, mPaint);
        mPaint.reset();
        mPaint.setColor(bgColor);
        mPaint.setTextSize(circleSize * 3 / 2);
        mPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
        int baseline = (int) ((rectF.bottom + rectF.top - fontMetrics.bottom - fontMetrics.top) / 2);
        canvas.drawText(mLetter, getWidth() / 2, baseline - 2, mPaint);
    }

    public LeImage setBgColor(int color) {
        this.bgColor = color;
        return this;
    }

    public LeImage setLetterColor(int color) {
        this.letterColor = color;
        return this;
    }

    public LeImage setLetterOrNumber(String letter) {
        this.mLetter = letter;
        return this;
    }

    public LeImage setCircleRadius(float letterSize) {
        this.circleSize = letterSize;
        return this;
    }

    public void setFillCircle(boolean a) {
        this.isFlag = a;
        invalidate();
    }
}
