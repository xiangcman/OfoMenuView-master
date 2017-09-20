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
    private ObjectAnimator titleAnimator, contentAnimator;
    private int titleStartY, titleEndY;
    private int contentStartY, contentEndY;
    private boolean titleAnimationing;
    private boolean contentAnimationing;

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

    public void open() {
        int titleHeight = titleView.getLayoutParams().height;
        titleStartY = -titleHeight;
        titleEndY = 0;

        contentStartY = getHeight() + contentView.getHeight();
        contentEndY = 0;
        definitAnimation();
        titleAnimator.start();
        contentAnimator.start();
    }

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
