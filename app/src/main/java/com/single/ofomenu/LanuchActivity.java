package com.single.ofomenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by xiangcheng on 17/12/28.
 */

public class LanuchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lanuch);
        findViewById(R.id.convex_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //凹进去
                startActivity(new Intent(LanuchActivity.this, OfoConvcaveMenuActivity.class));
            }
        });

        findViewById(R.id.concave_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LanuchActivity.this, OfoMenuActivity.class));
            }
        });
    }
}
