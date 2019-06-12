package com.markzl.android.thumbupview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class ThumbUpImageView extends View {

    private Paint paint;

    private boolean isLiked = false;
    private Bitmap mBitmapLikeUnSelected;
    private Bitmap mBitmapLikeSelected;
    private Bitmap mBitmapLikeShining;


    private float bitmapHeight;
    private float bitmapWidth;
    private float mBitmapLikeShiningHeight;
    private float mBitmapLikeShiningWidth;

    private ObjectAnimator objectAnimator;
    private float animProgress;

    private int middlePadding = ThumbUpUtils.dp2px(getContext(), 4);

    private Context mContext;

    //默认显示光标
    private boolean mShiningVisible = true;

    public ThumbUpImageView(Context context) {
        this(context, null);
    }

    public ThumbUpImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThumbUpImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {

        mBitmapLikeUnSelected = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_messages_like_unselected);
        mBitmapLikeSelected = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_messages_like_selected);
        mBitmapLikeShining = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_messages_like_selected_shining);
        mBitmapLikeShiningHeight = mBitmapLikeShining.getHeight();
        mBitmapLikeShiningWidth = mBitmapLikeShining.getWidth();

        paint = new Paint();
        paint.setAntiAlias(true);
        bitmapWidth = mBitmapLikeSelected.getWidth();
        bitmapHeight = mBitmapLikeSelected.getHeight();
        objectAnimator = ObjectAnimator.ofFloat(this, "animProgress", 0, 1);
    }

    @SuppressWarnings("unused")
    public void setAnimProgress(float animProgress) {
        this.animProgress = animProgress;
        invalidate();
    }

    @SuppressWarnings("unused")
    public float getAnimProgress() {
        return animProgress;
    }

    public void setMiddlePadding(float middlePadding) {
        this.middlePadding = (int) middlePadding * 2;
    }

    public void setLiked() {
        isLiked = !isLiked;
        objectAnimator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = mBitmapLikeSelected.getWidth() + getPaddingLeft() + getPaddingRight() + middlePadding;
        int height = (int) (bitmapHeight + mBitmapLikeShiningHeight + getPaddingTop() + getPaddingBottom());
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        //画底图
        drawImage(canvas, centerX, centerY);
        //画闪光
        drawShining(canvas, centerX, centerY);
        //画圆环
        drawCircle(canvas, centerX, centerY);

    }

    private void drawImage(Canvas canvas, float centerX, float centerY) {

        Bitmap bitmap = mBitmapLikeUnSelected;
        float scaleExtra = 0;


        if (isLiked) {
            //灰色缩小
            if (animProgress > 0 && animProgress <= 0.1f) {
                scaleExtra = -0.1f;
                bitmap = mBitmapLikeUnSelected;
            }

            //红色按钮透明度由0.5 -> 1
            if (animProgress > 0.1 && animProgress <= 0.5) {
                paint.setAlpha((int) ((0.5 + animProgress) * 255));
            } else {
                paint.setAlpha(255);
            }

            //放大红色按钮
            if (animProgress > 0.1 && animProgress <= 0.9) {
                bitmap = mBitmapLikeSelected;
                scaleExtra = -0.1f + animProgress * 0.2f;
            }

            //回归正常
            if (animProgress > 0.9 || animProgress == 0) {
                bitmap = mBitmapLikeSelected;
                scaleExtra = 0;
            }
        } else {
            //红色图标变小至0.9倍且变成半透明 动画完成到一半时变成灰色的正常大小
            if (animProgress > 0 && animProgress < 0.5f) {
                bitmap = mBitmapLikeSelected;
                paint.setAlpha((int) ((-animProgress + 1) * 255));
                scaleExtra = 0.1f * (2 * animProgress);
            }
            //一半的时候变灰色
            if (animProgress > 0.5f || animProgress == 0) {
                paint.setAlpha(255);
                bitmap = mBitmapLikeUnSelected;
                scaleExtra = 0;
            }
        }

        canvas.save();
        canvas.scale(1 + scaleExtra, 1 + scaleExtra, centerX, centerY);
        canvas.drawBitmap(bitmap, centerX - bitmapWidth / 2, centerY - bitmapHeight / 2, paint);
        canvas.restore();
    }

    private void drawShining(Canvas canvas, float centerX, float centerY) {
        if (isLiked && mShiningVisible) {
            float left = centerX - mBitmapLikeShiningWidth / 2;
            float top = centerY - bitmapWidth / 2 - mBitmapLikeShiningHeight / 2;
            paint.setAlpha(255);
            canvas.save();
            canvas.scale(animProgress, animProgress, centerX, centerY - bitmapHeight / 2f);
            canvas.drawBitmap(mBitmapLikeShining, left, top, paint);
            canvas.restore();
        }
    }

    private void drawCircle(Canvas canvas, float centerX, float centerY) {

        if (isLiked && animProgress > 0) {

            float radius = bitmapWidth > bitmapHeight ? bitmapHeight / 2 : bitmapWidth / 2;

            paint.setStrokeWidth(3);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.parseColor("#cc775c"));
            if (animProgress < 0.5f) {
                //透明变实体
                paint.setAlpha((int) (0.5 + animProgress) * 255);
            } else {
                //实体变透明
                paint.setAlpha((int) (((1 - animProgress) * 2) * 255));
            }
            canvas.drawCircle(centerX, centerY, radius * (0.6f + 0.7f * animProgress), paint);
        }
    }


    public void setLikeViewSelectedDrawable(int resourceSelectedId) {
        mBitmapLikeSelected = BitmapFactory.decodeResource(mContext.getResources(),
                resourceSelectedId);
    }


    public void setLikeViewUnSelectedDrawable(int resourceUnSelectedId) {
        mBitmapLikeUnSelected = BitmapFactory.decodeResource(mContext.getResources(),
                resourceUnSelectedId);
    }

    public void setLikeViewShiningDrawable(int resourceShiningId) {
        mBitmapLikeShining = BitmapFactory.decodeResource(mContext.getResources(),
                resourceShiningId);
    }

    public void setShiningVisible(boolean visible) {
        mShiningVisible = visible;
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        objectAnimator.end();
    }
}
