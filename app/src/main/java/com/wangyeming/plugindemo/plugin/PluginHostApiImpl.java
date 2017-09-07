package com.wangyeming.plugindemo.plugin;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.wangyeming.plugin.PluginRuntimeManager;
import com.wangyeming.plugin.PluginUtil;
import com.wangyeming.pluginapi.IPluginMessageReceiver;
import com.wangyeming.pluginapi.PluginBaseActivity;
import com.wangyeming.pluginapi.PluginHostApi;
import com.wangyeming.pluginapi.PluginPackage;
import com.wangyeming.plugindemo.PluginApplication;

/**
 * 插件接口实现
 * <p>
 * Created by wangyeming on 2017/9/5.
 */
public class PluginHostApiImpl extends PluginHostApi {

    public static synchronized void init() {
        mPluginHostApi = new PluginHostApiImpl();
    }

    @Override
    public void startActivity(Context context, PluginPackage pluginPackage,
                              Intent intent, Class<? extends PluginBaseActivity> activityClass) {
        PluginUtil.startActivity(context, pluginPackage, intent, activityClass);
    }

    @Override
    public void startActivityForResult(PluginBaseActivity pluginBaseActivity, PluginPackage pluginPackage,
                                       Intent intent, Class<? extends PluginBaseActivity> activityClass,
                                       int requestCode) {
        PluginUtil.startActivityForResult(pluginBaseActivity, pluginPackage, intent, activityClass, requestCode);
    }

    @Override
    public void openUrl(String url) {
        Log.d("wym", "openUrl " + url);
        Context appContext = PluginApplication.getAppContext();
        if(TextUtils.isEmpty(url)) {
            Log.d("wym", "path is empty");
            return;
        }
        PluginPackage pluginPackage = PluginRuntimeManager.getInstance(appContext).getPluginPackageByUrl(url);
        if(pluginPackage == null) {
            Log.d("wym", "pluginPackage is null");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("url", url);
        IPluginMessageReceiver iPluginMessageReceiver =  pluginPackage.getPluginMessageReceiver();
        if(iPluginMessageReceiver == null) {
            Log.d("wym", "iPluginMessageReceiver is null");
            return;
        }
        iPluginMessageReceiver.handleMessage(appContext, pluginPackage, url);
    }
}
