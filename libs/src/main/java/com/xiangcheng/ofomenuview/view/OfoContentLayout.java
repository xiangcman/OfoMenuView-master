package com.xiangcheng.ofomenuview.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiangcheng on 17/9/20.
 */

public class OfoContentLayout extends LinearLayout {
    private static final String TAG = OfoContentLayout.class.getSimpleName();
    //存储每个child的终点坐标
    List<Float> endOffset = new ArrayList<>();

    public boolean isAnimationing() {
        return isAnimationing;
    }

    //是否在动画中的标志，为事件分发做准备
    private boolean isAnimationing;
    //是否添加监听的标志，因为所有的child时间都是一样的，所以监听第一个child就行
    private boolean hasListener;

    public OfoContentLayout(Context context) {
        super(context);
    }

    public OfoContentLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OfoContentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void open() {
        for (int i = 0; i < getChildCount(); i++) {
            ObjectAnimator oa = ObjectAnimator.ofFloat(getChildAt(i), "translationY", endOffset.get(i), 0);
            oa.setDuration(700);
            if (!hasListener) {
                hasListener = true;
                oa.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        isAnimationing = true;

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        isAnimationing = false;
                        hasListener = false;
                    }
                });

            }
            oa.start();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            child.setTag(i);
            child.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    child.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    //终点坐标按照每个child的起点坐标+递增15dp
                    endOffset.add(child.getTop() + ((int) child.getTag()) *
                            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getContext().getResources().getDisplayMetrics()));
                }
            });
        }

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public void setItemContentViews(List<View> itemContentViews) {
        for (int i = 0; i < itemContentViews.size(); i++) {
            final int position = i;
            View view = itemContentViews.get(i);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onClick(position);
                    }
                }
            });
            addView(view);
        }
    }
}
