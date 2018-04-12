package com.wangyeming.plugindemo2;

import android.os.Bundle;
import android.util.Log;

import com.wangyeming.pluginapi.PluginBaseActivity;

/**
 * Created by wangyeming on 2017/9/5.
 */

public class Page1Activity extends PluginBaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo1_main);
    }
}
