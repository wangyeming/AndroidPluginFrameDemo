package com.wangyeming.plugindemo1;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.wangyeming.pluginapi.IPluginMessageReceiver;
import com.wangyeming.pluginapi.PluginHostApi;
import com.wangyeming.pluginapi.PluginPackage;
import com.wangyeming.pluginapi.UrlConstant;

/**
 * Created by wangyeming on 2017/9/6.
 */
public class PluginMessageReceiver implements IPluginMessageReceiver {

    @Override
    public boolean handleMessage(Context context, PluginPackage pluginPackage, String url) {
        String path = getPath(url);
        Intent intent = new Intent();
        switch (path) {
            case UrlConstant.DEMO1_PAGE1:
                PluginHostApi.getInstance().startActivity(context, pluginPackage, intent, Page1Activity.class);
                break;
            case UrlConstant.DEMO1_PAGE2:
                PluginHostApi.getInstance().startActivity(context, pluginPackage, intent, Page2Activity.class);
                break;
        }
        return true;
    }

    public static String getPath(String url) {
        int pos = url.indexOf("?");
        if (pos >= 0) {
            url = url.substring(0, pos);
        }
        pos = url.indexOf("/shop/");
        if (pos >= 0) {
            url = url.substring(pos + 6);
        }
        return url;
    }
}
