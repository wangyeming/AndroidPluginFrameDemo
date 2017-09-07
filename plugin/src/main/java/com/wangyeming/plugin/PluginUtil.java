package com.wangyeming.plugin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.wangyeming.pluginapi.HostInfo;
import com.wangyeming.pluginapi.PackageRawInfo;
import com.wangyeming.pluginapi.PluginBaseActivity;
import com.wangyeming.pluginapi.PluginPackage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by wangyeming on 2017/9/5.
 */
public class PluginUtil {

    public static final String EXTRA_CLASS = "class:";
    public static final String EXTRA_PACKAGE_PATH = "packagepth:";

    /**
     * 打开一个插件Activity
     *
     * @param context
     * @param pluginPackage
     * @param intent
     * @param activityClass
     */
    public static void startActivity(Context context,
                                     PluginPackage pluginPackage,
                                     Intent intent,
                                     Class<? extends PluginBaseActivity> activityClass) {
        Intent startIntent = new Intent(context.getApplicationContext(), DefaultHostActivity.class);
        startIntent.addCategory(EXTRA_PACKAGE_PATH + pluginPackage.getPackagePath());
        startIntent.addCategory(EXTRA_CLASS + activityClass.getName());
        if (intent != null) {
            startIntent.setData(intent.getData());
            startIntent.putExtras(intent);
            startIntent.setFlags(intent.getFlags());
        }
        if (context instanceof PluginBaseActivity) {
            //如果是插件Activity启动的话
            PluginBaseActivity pluginBaseActivity = (PluginBaseActivity) context;
            pluginBaseActivity.getHostActivity().startActivity(startIntent);
        } else if (context instanceof Activity) {
            //如果是个普通的Activity启动的话
            context.startActivity(startIntent);
        } else {
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(startIntent);
        }
    }

    /**
     * 打开一个插件Activity For Result
     *
     * @param context
     * @param pluginPackage
     * @param intent
     * @param activityClass
     * @param requestCode
     */
    public static void startActivityForResult(Context context,
                                              PluginPackage pluginPackage,
                                              Intent intent,
                                              Class<? extends PluginBaseActivity> activityClass,
                                              int requestCode) {
        Intent startIntent = new Intent(context.getApplicationContext(), DefaultHostActivity.class);
        startIntent.addCategory(EXTRA_PACKAGE_PATH + pluginPackage.getPackagePath());
        startIntent.addCategory(EXTRA_CLASS + activityClass.getName());
        if (intent != null) {
            startIntent.setData(intent.getData());
            startIntent.putExtras(intent);
            startIntent.setFlags(intent.getFlags());
        }
        if (context instanceof PluginBaseActivity) {
            //如果是插件Activity启动的话
            PluginBaseActivity pluginBaseActivity = (PluginBaseActivity) context;
            pluginBaseActivity.getHostActivity().startActivityForResult(startIntent, requestCode);
        } else if (context instanceof Activity) {
            //如果是个普通的Activity启动的话
            ((Activity) context).startActivityForResult(startIntent, -1);
        } else {
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(startIntent);
        }
    }

    public static HostInfo parseHostInfo(Intent intent) {
        HostInfo hostInfo = new HostInfo();
        Set<String> categories = intent.getCategories();
        if (categories == null) {
            return hostInfo;
        }
        for (String category : categories) {
            if (category.startsWith(EXTRA_CLASS)) {
                hostInfo.mClass = category.substring(EXTRA_CLASS.length());
            } else if (category.startsWith(EXTRA_PACKAGE_PATH)) {
                hostInfo.mPackagePath = category.substring(EXTRA_PACKAGE_PATH.length());
            }
        }
        return hostInfo;
    }

    /**
     * 加载包信息
     *
     * @param context
     * @param packagePath
     * @return
     */
    @Nullable
    public static PackageRawInfo loadPackageInfo(Context context, String packagePath) {
        if (TextUtils.isEmpty(packagePath)) {
            return null;
        }
        PackageInfo packageInfo = context.getPackageManager().getPackageArchiveInfo(packagePath,
                PackageManager.GET_META_DATA);
        if (packageInfo == null) {
            return null;
        }
        PackageRawInfo packageRawInfo = new PackageRawInfo();
        packageRawInfo = new PackageRawInfo();
        packageRawInfo.mVersion = packageInfo.versionCode;
        packageRawInfo.mVersionName = packageInfo.versionName;
        packageRawInfo.mPackageName = packageInfo.packageName;
        //从Manifest中获取信息
        ApplicationInfo appInfo = packageInfo.applicationInfo;
        if (appInfo != null) {
            Bundle bundle = appInfo.metaData;
            if (bundle != null) {
                packageRawInfo.mMinApiLevel = bundle.getInt("minPluginSdkApiVersion", 0);
                String urls = bundle.getString("urls", "");
                Log.d("wym", "urls " + urls + " mMinApiLevel " + packageRawInfo.mMinApiLevel);
                packageRawInfo.mUrlList = Arrays.asList(urls.split("\\|"));
                packageRawInfo.mMessageHandlerName = bundle.getString("message_handler", "");
            }
        }
        return packageRawInfo;
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


    public static String getParams(String url) {
        int pos = url.indexOf("?");
        if (pos >= 0 && pos + 1 < url.length()) {
            return url.substring(pos + 1);
        }
        return "";
    }

    //解析url参数
    public static Map<String, String> getParamsMap(String url) {
        Map<String, String> params = new HashMap<>();
        String paramStr = getParams(url);
        if (TextUtils.isEmpty(paramStr)) {
            return params;
        }
        String[] paramArray = paramStr.split("&");
        if (paramArray != null && paramArray.length > 0) {
            for (String paramItem : paramArray) {
                int pos = paramItem.indexOf("=");
                if (pos > 0 && pos < paramItem.length() - 1) {
                    params.put(paramItem.substring(0, pos), paramItem.substring(pos + 1));
                }
            }
        }
        return params;
    }
}
