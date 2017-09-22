package com.single.ofomenu;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.FrameLayout;

import com.single.ofomenu.view.OfoContentLayout;
import com.single.ofomenu.view.OfoMenuLayout;

/**
 * Created by xiangcheng on 17/9/19.
 */

public class OfoMenuActivity extends AppCompatActivity {
    //最外层的view，用来管理title和content的动画
    OfoMenuLayout ofoMenuLayout;
    //contennt中列表view，主要是控制自己的动画
    OfoContentLayout ofoContentLayout;
    FrameLayout menu;
    Button startBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_ofo_layout);
//        ofoMenuLayout = ((OfoMenuLayout) findViewById(R.id.ofo_menu));
//        ofoContentLayout = ((OfoContentLayout) findViewById(R.id.ofo_content));
//        menu = (FrameLayout) findViewById(R.id.menu_content);
//        startBtn = (Button) findViewById(R.id.start_ofo);
//        //启动menu
//        startBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startBtn.setVisibility(View.GONE);
//                ofoMenuLayout.setVisibility(View.VISIBLE);
//                ofoMenuLayout.open();
//                menu.setBackground(new MenuBrawable(BitmapFactory.decodeResource(getResources(), R.mipmap.bitmap), OfoMenuActivity.this));
//            }
//        });
//        //关闭menu
//        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ofoMenuLayout.close();
//            }
//        });
//        //menu的监听
//        ofoMenuLayout.setOfoMenuStatusListener(new OfoMenuLayout.OfoMenuStatusListener() {
//            @Override
//            public void onOpen() {
//
//            }
//
//            @Override
//            public void onClose() {
//                startBtn.setVisibility(View.VISIBLE);
//            }
//        });
//        //给menu设置content部分
//        ofoMenuLayout.setOfoContentLayout(ofoContentLayout);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        OfoFragment mHomeFragment = new OfoFragment();
        ft.replace(android.R.id.content, mHomeFragment);
        ft.commit();
    }
}
