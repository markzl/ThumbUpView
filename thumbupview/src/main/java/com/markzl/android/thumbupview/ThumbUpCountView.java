package com.markzl.android.thumbupview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class ThumbUpCountView extends View {

    public static final String DEFAULT_TEXT_COLOR = "#cccccc";
    public static final float DEFAULT_TEXT_SIZE = 15f;
    private static final int COUNT_ANIM_DURING = 250;

    private Paint paint = new Paint();

    private Context mContext;

    private int mCurrentCount = 0;
    private int mLastCount = 0;
    private boolean isLiked;

    private ObjectAnimator objectAnimator;
    private float progress;
    private Rect bounds;
    private float mTextSize;
    private String mTextColor;


    public ThumbUpCountView(Context context) {
        this(context, null);
    }

    public ThumbUpCountView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThumbUpCountView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor(DEFAULT_TEXT_COLOR));
        paint.setTextSize(ThumbUpUtils.sp2px(mContext, DEFAULT_TEXT_SIZE));
        objectAnimator = ObjectAnimator.ofFloat(this, "progress", 0, 1f);
        objectAnimator.setDuration(COUNT_ANIM_DURING);
        bounds = new Rect();
    }

    @SuppressWarnings("unused")
    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }

    @SuppressWarnings("unused")
    public float getProgress() {
        return progress;
    }


    public void setCount(int count) {
        this.mLastCount = this.mCurrentCount = count;
    }

    public int getCount() {
        return mCurrentCount;
    }

    public void setTextSize(float textSize) {
        mTextSize = textSize;
        paint.setTextSize(mTextSize);

    }

    public void setTextColor(String textColor) {
        mTextColor = textColor;
        paint.setColor(Color.parseColor(textColor));
    }

    public void setLiked() {
        mLastCount = mCurrentCount;
        if (!isLiked) {
            mCurrentCount++;
        } else {
            mCurrentCount--;
        }
        isLiked = !isLiked;
        objectAnimator.start();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width = getContentWidth() + getPaddingLeft() + getPaddingRight();
        int height = getContentHeight() + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(ThumbUpUtils.getDefaultSize(widthMeasureSpec, width),
                ThumbUpUtils.getDefaultSize(heightMeasureSpec, height));
    }

    private int getContentWidth() {
        //保证+1之后字体的宽度 例 9 —> 10
        return (int) Math.ceil(paint.measureText(String.valueOf(mCurrentCount + 1)) * 1.2f);
    }

    private int getContentHeight() {
        paint.getTextBounds(String.valueOf(mCurrentCount), 0,
                String.valueOf(mCurrentCount).length(), bounds);
        return -(bounds.bottom + bounds.top);
    }

    private String[] mTexts = new String[3];// 0 不变的部分，1 原来的部分 2 现在的部分

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        String mStrLastCount = String.valueOf(mLastCount);
        String mStrCurrentCount = String.valueOf(mCurrentCount);
        int centerY = getHeight() / 2;
        int left = getPaddingLeft();
        paint.getTextBounds(mStrCurrentCount, 0, mStrCurrentCount.length(), bounds);
        int fontCenterY = -(bounds.bottom + bounds.top) / 2;

        if (mStrCurrentCount.equals(mStrLastCount)) {
            mTexts[0] = mStrCurrentCount;
            mTexts[1] = "";
            mTexts[2] = "";
        }
        for (int i = 0; i < mStrLastCount.length(); i++) {
            if (mStrCurrentCount.charAt(i) != mStrLastCount.charAt(i)) {
                mTexts[0] = mStrLastCount.substring(0, i);
                mTexts[1] = mStrLastCount.substring(i);
                mTexts[2] = mStrCurrentCount.substring(i);
                break;
            }
        }

        canvas.drawText(mTexts[0], left, centerY + fontCenterY, paint);
        float mUnChangeTextWidth = paint.measureText(mTexts[0], 0, mTexts[0].length());
        if (mLastCount != mCurrentCount && isLiked) {
            //点赞向上滑动
            canvas.drawText(mTexts[1], left + mUnChangeTextWidth,
                    (centerY + fontCenterY) * (1 - progress), paint);
            canvas.drawText(mTexts[2], left + mUnChangeTextWidth,
                    (centerY + fontCenterY) * (2 - progress), paint);
        }

        if (mLastCount != mCurrentCount && !isLiked) {
            //取消点赞 向下滑动
            canvas.drawText(mTexts[1], left + mUnChangeTextWidth,
                    (centerY + fontCenterY) * (1 + progress), paint);
            canvas.drawText(mTexts[2], left + mUnChangeTextWidth,
                    (centerY + fontCenterY) * progress, paint);
        }

    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superData", super.onSaveInstanceState());
        bundle.putInt("mCurrentCount", mCurrentCount);
        bundle.putFloat("textSize", mTextSize);
        bundle.putString("textColor", mTextColor);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle bundle = (Bundle) state;
        Parcelable superData = bundle.getParcelable("superData");
        super.onRestoreInstanceState(superData);
        mCurrentCount = bundle.getInt("mCurrentCount", 0);
        mTextSize = bundle.getFloat("textSize", DEFAULT_TEXT_SIZE);
        mTextColor = bundle.getString("textColor", mTextColor);
        init();
    }
}
