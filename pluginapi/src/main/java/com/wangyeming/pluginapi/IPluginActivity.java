package com.wangyeming.pluginapi;

import android.app.Activity;

/**
 * Created by wangyeming on 2017/9/4.
 */
public interface IPluginActivity {

    /**
     * 传递Host信息给Plugin
     */
    void attach(IHostActivity iHostActivity, Activity activity, PluginPackage pluginPackage);

    /**
     * 获取Host Activity
     *
     * @return
     */
    Activity getHostActivity();
}
