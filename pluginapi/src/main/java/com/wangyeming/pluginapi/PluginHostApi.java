package com.wangyeming.pluginapi;

import android.content.Context;
import android.content.Intent;

/**
 * Host提供给插件的接口
 * <p>
 * Created by wangyeming on 2017/9/5.
 */
public abstract class PluginHostApi {

    protected static PluginHostApi mPluginHostApi = null;

    public static PluginHostApi getInstance() {
        return mPluginHostApi;
    }

    /**
     * Api 1
     *
     * @param context
     * @param pluginPackage
     * @param intent
     * @param activityClass
     */
    public abstract void startActivity(Context context,
                                       PluginPackage pluginPackage,
                                       Intent intent,
                                       Class<? extends PluginBaseActivity> activityClass);

    /**
     * Api 1
     *
     * @param pluginBaseActivity
     * @param pluginPackage
     * @param intent
     * @param activityClass
     * @param requestCode
     */
    public abstract void startActivityForResult(PluginBaseActivity pluginBaseActivity,
                                                PluginPackage pluginPackage,
                                                Intent intent,
                                                Class<? extends PluginBaseActivity> activityClass,
                                                int requestCode);

    /**
     * 打开url
     *
     * @param url
     */
    public abstract void openUrl(String url);
}
