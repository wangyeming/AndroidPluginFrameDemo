package com.wangyeming.pluginapi;

import android.content.Context;

/**
 * 插件必须要实现该接口，实现事件的处理
 * <p>
 * Created by wangyeming on 2017/9/6.
 */
public interface IPluginMessageReceiver {

    /**
     * 处理消息
     *
     * @param context
     * @param pluginPackage
     * @param url
     * @return
     */
    boolean handleMessage(Context context, PluginPackage pluginPackage, String url);
}
