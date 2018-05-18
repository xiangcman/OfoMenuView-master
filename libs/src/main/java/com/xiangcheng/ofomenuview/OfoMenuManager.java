package com.xiangcheng.ofomenuview;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.xiangcheng.ofomenuview.view.OfoContentLayout;
import com.xiangcheng.ofomenuview.view.OfoMenuLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiangcheng on 18/5/11.
 */

public class OfoMenuManager {
    private Context context;

    private OfoMenuManager(Builder builder) {
        this.context = builder.context;
        ofoMenuLayout = new OfoMenuLayout(context, builder.radian, builder.ofoBackColor, builder.ofoStartPosition,
                builder.closeIcon, builder.userIcon, builder.itemViews);
    }

    private OfoMenuLayout ofoMenuLayout;

    public OfoMenuLayout getRootView() {
        return ofoMenuLayout;
    }

    public void open() {
        if (ofoMenuLayout != null) {
            ofoMenuLayout.open();
        }
    }

    public void close() {
        if (ofoMenuLayout != null) {
            ofoMenuLayout.close();
        }
    }

    public boolean isOpen() {
        if (ofoMenuLayout != null) {
            return ofoMenuLayout.isOpen();
        }
        return false;
    }

    public void setOfoMenuStatusListener(OfoMenuLayout.OfoMenuStatusListener ofoMenuStatusListener) {
        if (ofoMenuLayout != null) {
            ofoMenuLayout.setOfoMenuStatusListener(ofoMenuStatusListener);
        }
    }

    public void setOfoUsesrIconListener(OfoMenuLayout.OfoUserIconListener ofoUsesrIconListener) {
        if (ofoMenuLayout != null) {
            ofoMenuLayout.setOfoUserIconListener(ofoUsesrIconListener);
        }
    }

    public void setOnItemClickListener(OfoContentLayout.OnItemClickListener onItemClickListener) {
        if (ofoMenuLayout != null) {
            ofoMenuLayout.setOnItemClickListener(onItemClickListener);
        }
    }

    public void setUserIcon(@DrawableRes int userIcon) {
        if (ofoMenuLayout != null) {
            ofoMenuLayout.setUserIcon(userIcon);
        }
    }

    public static final class Builder {
        private Context context;
        private int radian;
        @ColorRes
        private int ofoBackColor = -1;
        @DimenRes
        private int ofoStartPosition = -1;

        @DrawableRes
        private int userIcon = -1;

        @DrawableRes
        private int closeIcon = -1;

        private List<View> itemViews;

        public Builder(Context context) {
            this.context = context;
            itemViews = new ArrayList<>();
        }

        //设置是凹进去还是凸出来的
        public Builder setRadian(int radian) {
            this.radian = radian;
            return this;
        }

        public Builder setOfoBackColor(@ColorRes int color) {
            this.ofoBackColor = color;
            return this;
        }

        //设置menu菜单距离上边的距离
        public Builder setOfoPosition(@DimenRes int dimens) {
            this.ofoStartPosition = dimens;
            return this;
        }

        public Builder setCloseIcon(@DrawableRes int closeIcon) {
            this.closeIcon = closeIcon;
            return this;
        }

        public Builder setUserIcon(@DrawableRes int userIcon) {
            this.userIcon = userIcon;
            return this;
        }

        public Builder addItemContentView(View view) {
            itemViews.add(view);
            return this;
        }

        public Builder addItemContentView(@LayoutRes int layout) {
            itemViews.add(View.inflate(context, layout, null));
            return this;
        }

        public OfoMenuManager build() {
            return new OfoMenuManager(this);
        }
    }
}
