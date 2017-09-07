package com.wangyeming.pluginapi;

import android.content.res.AssetManager;
import android.content.res.Resources;

import dalvik.system.DexClassLoader;

/**
 * Host需要Plugin的信息
 *
 * Created by wangyeming on 2017/9/4.
 */
public class PluginPackage {

    /**
     * 包路径
     */
    private String packagePath;

    private DexClassLoader classLoader;

    private AssetManager assetManager;

    private Resources resources;

    private IPluginMessageReceiver pluginMessageReceiver;

    private PackageRawInfo packageRawInfo;

    public synchronized String getPackagePath() {
        return packagePath;
    }

    public synchronized DexClassLoader getClassLoader() {
        return classLoader;
    }

    public synchronized AssetManager getAssetManager() {
        return assetManager;
    }

    public synchronized Resources getResources() {
        return resources;
    }

    public IPluginMessageReceiver getPluginMessageReceiver() {
        return pluginMessageReceiver;
    }

    public PackageRawInfo getPackageRawInfo() {
        return packageRawInfo;
    }

    public PluginPackage(String packagePath, DexClassLoader dexClassLoader,
                         AssetManager assetManager, Resources resources,
                         IPluginMessageReceiver iPluginMessageReceiver, PackageRawInfo packageRawInfo) {
        this.packagePath = packagePath;
        this.classLoader = dexClassLoader;
        this.assetManager = assetManager;
        this.resources = resources;
        this.pluginMessageReceiver = iPluginMessageReceiver;
        this.packageRawInfo = packageRawInfo;
    }


}
