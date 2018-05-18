package com.single.ofomenu;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.xiangcheng.ofomenuview.OfoMenuManager;
import com.xiangcheng.ofomenuview.drawable.MenuBrawable;
import com.xiangcheng.ofomenuview.view.OfoContentLayout;
import com.xiangcheng.ofomenuview.view.OfoMenuLayout;

/**
 * Created by xiangcheng on 17/9/19.
 */

public class OfoMenuActivity extends AppCompatActivity {
    OfoMenuManager ofoMenuManager;

    private int count;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ofo_layout);
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        ofoMenuManager = new OfoMenuManager.Builder(this)
                //CONCAVE：凹进去；CONVEX：凸出来
                .setRadian(MenuBrawable.CONVEX)
                .setOfoBackColor(android.R.color.holo_blue_light)
                .setOfoPosition(R.dimen.ofo_menu_height)
                .addItemContentView(R.layout.item_huiyuan)
                .addItemContentView(R.layout.item_qianbao)
                .addItemContentView(R.layout.item_xiaoxi)
                .addItemContentView(R.layout.item_xingcheng)
                .addItemContentView(R.layout.item_ziliao)
                .build();
        ((ViewGroup) findViewById(android.R.id.content)).addView(ofoMenuManager.getRootView());
        findViewById(R.id.start_ofo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ofoMenuManager.open();
            }
        });
        ofoMenuManager.setOfoMenuStatusListener(new OfoMenuLayout.OfoMenuStatusListener() {
            @Override
            public void onOpen() {
                findViewById(R.id.start_ofo).setVisibility(View.GONE);
            }

            @Override
            public void onClose() {
                findViewById(R.id.start_ofo).setVisibility(View.VISIBLE);
            }
        });
        ofoMenuManager.setOfoUsesrIconListener(new OfoMenuLayout.OfoUserIconListener() {
            @Override
            public void onClick() {
                count++;
                if (count % 2 == 0) {
                    ofoMenuManager.setUserIcon(R.mipmap.single);
                } else {
                    ofoMenuManager.setUserIcon(R.mipmap.timg);
                }
            }
        });
        ofoMenuManager.setOnItemClickListener(new OfoContentLayout.OnItemClickListener() {
            @Override
            public void onClick(int position) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (ofoMenuManager.isOpen()) {
            ofoMenuManager.close();
            return;
        }
        super.onBackPressed();
    }
}
