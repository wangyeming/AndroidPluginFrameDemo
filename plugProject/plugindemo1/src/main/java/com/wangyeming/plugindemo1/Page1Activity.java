package com.wangyeming.plugindemo1;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.wangyeming.pluginapi.PluginBaseActivity;
import com.wangyeming.pluginapi.PluginHostApi;
import com.wangyeming.pluginapi.UrlConstant;

/**
 * Created by wangyeming on 2017/9/5.
 */

public class Page1Activity extends PluginBaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo1_main);

        findViewById(R.id.plugin_demo1_route_to_page2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PluginHostApi.getInstance().openUrl(UrlConstant.DEMO1_PAGE2);
            }
        });
    }
}
