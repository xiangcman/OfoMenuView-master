package com.single.ofomenu;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.single.ofomenu.drawable.MenuBrawable;
import com.single.ofomenu.view.OfoContentLayout;
import com.single.ofomenu.view.OfoMenuLayout;

/**
 * Created by xiangcheng on 17/9/19.
 */

public class OfoMenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ofo_layout);
        findViewById(R.id.start_ofo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.start_ofo).setVisibility(View.GONE);
                findViewById(R.id.ofo_menu).setVisibility(View.VISIBLE);
                ((OfoMenuLayout) findViewById(R.id.ofo_menu)).open();
                ((OfoContentLayout) findViewById(R.id.ofo_content)).open();
                FrameLayout menu = (FrameLayout) findViewById(R.id.menu_content);
                menu.setBackground(new MenuBrawable(BitmapFactory.decodeResource(getResources(), R.mipmap.bitmap), OfoMenuActivity.this));
            }
        });

        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OfoMenuLayout) findViewById(R.id.ofo_menu)).close();
            }
        });
        ((OfoMenuLayout) findViewById(R.id.ofo_menu)).setOfoMenuStatusListener(new OfoMenuLayout.OfoMenuStatusListener() {
            @Override
            public void onOpen() {

            }

            @Override
            public void onClose() {
                findViewById(R.id.start_ofo).setVisibility(View.VISIBLE);
            }
        });
        ((OfoMenuLayout) findViewById(R.id.ofo_menu)).setOfoContentLayout((OfoContentLayout) findViewById(R.id.ofo_content));
    }
}
