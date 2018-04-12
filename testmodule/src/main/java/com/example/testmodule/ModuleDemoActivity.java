package com.example.testmodule;

import android.os.Bundle;

import com.wangyeming.pluginapi.PluginBaseActivity;

/**
 * 示范为什么在插件依赖模块的代码里，引用R资源会报错
 */
public class ModuleDemoActivity extends PluginBaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_demo_act);
    }
}
