package com.single.ofomenu.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by xiangcheng on 17/9/19.
 */

public class OfoMenuLayout extends RelativeLayout {
    private View titleView;
    private View contentView;
    //动画对象
    private ObjectAnimator titleAnimator, contentAnimator;
    //title起始和终止坐标，主要为动画做准备
    private int titleStartY, titleEndY;
    //content起始和终止坐标，主要为动画做准备
    private int contentStartY, contentEndY;
    //title动画标志，为事件分发做准备
    private boolean titleAnimationing;
    //content动画标志，为事件分发做准备
    private boolean contentAnimationing;
    //content中列表内容布局，它里面也有自己的动画
    private OfoContentLayout ofoContentLayout;

    private boolean isOpen;

    private OfoMenuStatusListener ofoMenuStatusListener;

    public interface OfoMenuStatusListener {
        void onOpen();

        void onClose();
    }

    public OfoMenuLayout(Context context) {
        super(context);
    }

    public OfoMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OfoMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        titleView = getChildAt(0);
        contentView = getChildAt(1);
    }

    //定义动画部分
    private void definitAnimation() {
        PropertyValuesHolder titlePropertyValuesHolder = PropertyValuesHolder.ofFloat("translationY", titleStartY, titleEndY);
        titleAnimator = ObjectAnimator.ofPropertyValuesHolder(titleView, titlePropertyValuesHolder);
        titleAnimator.setDuration(300);

        contentAnimator = ObjectAnimator.ofFloat(contentView, "translationY", contentStartY, contentEndY);
        contentAnimator.setDuration(500);

        titleAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                titleAnimationing = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                titleAnimationing = false;
            }
        });

        contentAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                contentAnimationing = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                contentAnimationing = false;
                isOpen = !isOpen;
                setVisibility(isOpen ? VISIBLE : INVISIBLE);
                if (isOpen) {
                    if (ofoMenuStatusListener != null) {
                        ofoMenuStatusListener.onOpen();
                    }
                } else {
                    if (ofoMenuStatusListener != null) {
                        ofoMenuStatusListener.onClose();
                    }
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return titleAnimationing || contentAnimationing || ofoContentLayout.isAnimationing();
    }

    //菜单打开的动画
    public void open() {
        int titleHeight = titleView.getLayoutParams().height;
        titleStartY = -titleHeight;
        titleEndY = 0;

        contentStartY = getHeight() + contentView.getHeight();
        contentEndY = 0;
        definitAnimation();
        titleAnimator.start();
        contentAnimator.start();
        ofoContentLayout.open();
    }

    //菜单关闭的动画
    public void close() {
        int titleHeight = titleView.getLayoutParams().height;
        titleStartY = 0;
        titleEndY = -titleHeight;

        contentStartY = 0;
        contentEndY = getHeight() + contentView.getHeight();

        definitAnimation();

        titleAnimator.start();
        contentAnimator.start();
    }

    public void setOfoMenuStatusListener(OfoMenuStatusListener ofoMenuStatusListener) {
        this.ofoMenuStatusListener = ofoMenuStatusListener;
    }

    public void setOfoContentLayout(OfoContentLayout ofoContentLayout) {
        this.ofoContentLayout = ofoContentLayout;
    }

}
