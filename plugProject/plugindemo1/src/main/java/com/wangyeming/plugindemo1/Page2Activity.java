package com.wangyeming.plugindemo1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.wangyeming.pluginapi.PluginBaseActivity;

/**
 * Created by wangyeming on 2017/9/5.
 */

public class Page2Activity extends PluginBaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo2_main);
        findViewById(R.id.plugin_demo1_route_to_page3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(), Page3Activity.class);
            }
        });
    }
}
