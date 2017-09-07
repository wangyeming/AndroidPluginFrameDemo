package com.wangyeming.plugindemo;

import android.app.Application;
import android.content.Context;

import com.wangyeming.plugindemo.plugin.PluginHostApiImpl;

/**
 * Created by wangyeming on 2017/9/5.
 */
public class PluginApplication extends Application {

    private static PluginApplication mPluginApplication;

    public static Context getAppContext() {
        return mPluginApplication.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mPluginApplication = this;

        if (ProcessUtil.isMainProcess(this)) {
            //初始化插件Api
            PluginHostApiImpl.init();
        }
    }
}
