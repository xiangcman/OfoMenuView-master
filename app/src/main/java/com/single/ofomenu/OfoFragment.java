package com.single.ofomenu;

import android.app.Fragment;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.single.ofomenu.drawable.MenuBrawable;
import com.single.ofomenu.view.OfoContentLayout;
import com.single.ofomenu.view.OfoMenuLayout;

/**
 * Created by xiangcheng on 17/9/22.
 */

public class OfoFragment extends Fragment {
    //最外层的view，用来管理title和content的动画
    OfoMenuLayout ofoMenuLayout;
    //contennt中列表view，主要是控制自己的动画
    OfoContentLayout ofoContentLayout;
    FrameLayout menu;
    Button startBtn;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        inflater.inflate(R.layout.activity_ofo_layout, null)
        view = inflater.inflate(R.layout.activity_ofo_layout, null);
        ofoMenuLayout = ((OfoMenuLayout) view.findViewById(R.id.ofo_menu));
        ofoContentLayout = ((OfoContentLayout) view.findViewById(R.id.ofo_content));
        menu = (FrameLayout) view.findViewById(R.id.menu_content);
        startBtn = (Button) view.findViewById(R.id.start_ofo);
        //启动menu
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBtn.setVisibility(View.GONE);
                ofoMenuLayout.setVisibility(View.VISIBLE);
                ofoMenuLayout.open();
                menu.setBackground(new MenuBrawable(BitmapFactory.decodeResource(getResources(), R.mipmap.bitmap), getActivity()));
            }
        });
        //关闭menu
        view.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ofoMenuLayout.close();
            }
        });
        //menu的监听
        ofoMenuLayout.setOfoMenuStatusListener(new OfoMenuLayout.OfoMenuStatusListener() {
            @Override
            public void onOpen() {

            }

            @Override
            public void onClose() {
                startBtn.setVisibility(View.VISIBLE);
            }
        });
        //给menu设置content部分
        ofoMenuLayout.setOfoContentLayout(ofoContentLayout);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
