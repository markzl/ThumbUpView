package com.markzl.android.thumbupview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class ThumbUpView extends LinearLayout {

    private ThumbUpCountView mThumbUpCountView;
    private ThumbUpImageView mThumbUpImageView;

    public ThumbUpView(Context context) {
        this(context, null);
    }

    public ThumbUpView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThumbUpView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.ThumbUpView, defStyleAttr, 0);
        //ThumbUpCountView
        float textSize = typedArray.getDimension(R.styleable.ThumbUpView_like_count_text_size,
                ThumbUpCountView.DEFAULT_TEXT_SIZE);
        String textColor = typedArray.getString(R.styleable.ThumbUpView_like_count_text_color);
        int count = typedArray.getInt(R.styleable.ThumbUpView_like_count, 0);
        //mThumbUpImageView
        float middlePadding = typedArray.getDimension(R.styleable.ThumbUpView_like_view_middle_padding,
                0);
        int resourceSelectedId = typedArray.getResourceId(R.styleable.ThumbUpView_like_view_src_selected,
                R.drawable.ic_messages_like_selected);
        int resourceUnSelectedId = typedArray.getResourceId(R.styleable.ThumbUpView_like_view_src_unSelected,
                R.drawable.ic_messages_like_unselected);
        int resourceShiningId = typedArray.getResourceId(R.styleable.ThumbUpView_like_view_src_shining,
                R.drawable.ic_messages_like_selected_shining);
        boolean visible = typedArray.getBoolean(R.styleable.ThumbUpView_like_view_shining_visible,
                true);
        typedArray.recycle();
        mThumbUpCountView.setCount(count);
        mThumbUpCountView.setTextSize(textSize);
        mThumbUpCountView.setTextColor(textColor);
        mThumbUpImageView.setMiddlePadding(middlePadding);
        mThumbUpImageView.setLikeViewSelectedDrawable(resourceSelectedId);
        mThumbUpImageView.setLikeViewUnSelectedDrawable(resourceUnSelectedId);
        mThumbUpImageView.setLikeViewShiningDrawable(resourceShiningId);
        mThumbUpImageView.setShiningVisible(visible);
    }

    {
        setOrientation(HORIZONTAL);
        mThumbUpImageView = new ThumbUpImageView(getContext());
        addView(mThumbUpImageView);
        mThumbUpCountView = new ThumbUpCountView(getContext());
        addView(mThumbUpCountView);
    }

    @Override
    public boolean performClick() {
        mThumbUpImageView.setLiked();
        mThumbUpCountView.setLiked();
        return super.performClick();
    }

    @Override
    public boolean callOnClick() {
        mThumbUpImageView.setLiked();
        mThumbUpCountView.setLiked();
        return super.callOnClick();
    }


    @SuppressWarnings("unused")
    public void setCount(int count) {
        mThumbUpCountView.setCount(count);
    }


    @SuppressWarnings("unused")
    public int getCount() {
        return mThumbUpCountView.getCount();
    }


    @SuppressWarnings("unused")
    public void setTextSize(float textSize) {
        mThumbUpCountView.setTextSize(textSize);
    }


    @SuppressWarnings("unused")
    public void setTextColor(String textColor) {
        mThumbUpCountView.setTextColor(textColor);
    }

    @SuppressWarnings("unused")
    public void setMiddlePadding(float middlePadding) {
        mThumbUpImageView.setMiddlePadding(middlePadding);
    }

    @SuppressWarnings("unused")
    public void setLikeViewSelectedDrawable(int resourceSelectedId) {
        mThumbUpImageView.setLikeViewSelectedDrawable(resourceSelectedId);
    }


    @SuppressWarnings("unused")
    public void setLikeViewUnSelectedDrawable(int resourceUnSelectedId) {
        mThumbUpImageView.setLikeViewUnSelectedDrawable(resourceUnSelectedId);
    }


    @SuppressWarnings("unused")
    public void setLikeViewShiningDrawable(int resourceShiningId) {
        mThumbUpImageView.setLikeViewShiningDrawable(resourceShiningId);
    }

    @SuppressWarnings("unused")
    public void setShiningVisible(boolean visible) {
        mThumbUpImageView.setShiningVisible(visible);
    }


}
