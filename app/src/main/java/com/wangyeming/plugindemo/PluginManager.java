package com.wangyeming.plugindemo;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.wangyeming.plugin.FileUtils;
import com.wangyeming.plugin.PluginRuntimeManager;
import com.wangyeming.plugin.PluginUtil;
import com.wangyeming.pluginapi.PackageRawInfo;
import com.wangyeming.pluginapi.PluginPackage;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by wangyeming on 2017/9/5.
 */
public class PluginManager {

    private static volatile PluginManager mPluginManager;

    public static PluginManager getInstance() {
        if (mPluginManager == null) {
            synchronized (PluginManager.class) {
                if (mPluginManager == null) {
                    mPluginManager = new PluginManager();
                }
            }
        }
        return mPluginManager;
    }

    private PluginManager() {
        mAppContext = PluginApplication.getAppContext();
        ASSERT_PATH = "plugin";
        PLUGIN_INSTALL_PATH = getAppFilesDir() + File.separator + "plugin"
                + File.separator + "install" + File.separator + "apk";
    }

    private Context mAppContext;
    private final String ASSERT_PATH;
    private final String PLUGIN_INSTALL_PATH;     //插件安装路径
    private String mAppFilesDir = null;

    private String getAppFilesDir() {
        if (TextUtils.isEmpty(mAppFilesDir)) {
            mAppFilesDir = mAppContext.getFilesDir().getPath();
        }
        return mAppFilesDir;
    }

    /**
     * 插件安装根目录
     *
     * @param packageRawInfo
     * @return
     */
    private String getInstallPluginRootPath(PackageRawInfo packageRawInfo) {
        return PLUGIN_INSTALL_PATH + File.separator + packageRawInfo.mPackageName;
    }

    /**
     * 插件apk安装目录
     *
     * @param packageRawInfo
     * @return
     */
    private String getInstallPluginPath(PackageRawInfo packageRawInfo) {
        return getInstallPluginRootPath(packageRawInfo) + File.separator + packageRawInfo.mVersion;
    }

    /**
     * 具体的插件apk安装目录
     *
     * @param packageRawInfo
     * @return
     */
    private String getInstallPluginFilePath(PackageRawInfo packageRawInfo) {
        return getInstallPluginPath(packageRawInfo) + File.separator + packageRawInfo.mVersion + PLUGIN_INSTALL_PATH;
    }

    private String getAssetsPluginName(String packageName) {
        int pos = packageName.lastIndexOf('.');
        if (pos == -1 || pos == packageName.length() - 1)
            return "";
        String assetsName = packageName.substring(pos + 1);
        String[] assets = new String[0];
        try {
            assets = mAppContext.getAssets().list(ASSERT_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (assets != null && assets.length > 0) {
            for (String asset : assets) {
                if (asset.startsWith(assetsName)) {
                    return asset;
                }
            }
        }
        return "";
    }

    /**
     * 安装插件包
     */
    public boolean installPlugin(String pluginName) {
        //读取包信息
        String assetsPluginName = getAssetsPluginName(pluginName);
        if(TextUtils.isEmpty(assetsPluginName)) {
            Log.d("wym", pluginName + " file not exist");
            return false;
        }
        assetsPluginName = ASSERT_PATH + File.separator + assetsPluginName;
        Log.d("wym", "assetsPluginName " + assetsPluginName);
        String cacheFilePath = mAppContext.getCacheDir() + File.separator + assetsPluginName;
        try {
            FileUtils.copyAsserts(mAppContext, assetsPluginName, cacheFilePath);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        PackageRawInfo packageRawInfo = PluginUtil.loadPackageInfo(mAppContext, cacheFilePath);
        if (packageRawInfo == null) {
            //包信息读取失败
            Log.d("wym", pluginName + " packageRawInfo is null");
            return false;
        }
        String path = getInstallPluginPath(packageRawInfo);
        FileUtils.createDirIfNotExists(path);
        String installFilePath = getInstallPluginFilePath(packageRawInfo);
        FileUtils.copyFileToFile(pluginName, installFilePath);
        PluginRuntimeManager.getInstance(mAppContext).loadApk(pluginName, cacheFilePath, packageRawInfo);
        FileUtils.deleteFile(cacheFilePath);
        return true;
    }

    /**
     * 判断插件包是否安装
     *
     * @param packagePath
     * @return
     */
    public boolean isPluginInstall(String packagePath) {
        PluginPackage pluginPackage = PluginRuntimeManager.getInstance(mAppContext)
                .getPluginPackageByPackagePath(packagePath);
        return pluginPackage != null;
    }

    public List<String> getUrls(String packagePath) {
        PluginPackage pluginPackage = PluginRuntimeManager.getInstance(mAppContext)
                .getPluginPackageByPackagePath(packagePath);
        if(pluginPackage == null) {
            Log.d("wym", packagePath + " pluginPackage is null");
            return null;
        }
        PackageRawInfo packageRawInfo = pluginPackage.getPackageRawInfo();
        if(packageRawInfo == null) {
            Log.d("wym", packagePath + " packageRawInfo is null");
            return null;
        }
        return packageRawInfo.mUrlList;
    }

    public void removePlugin(String pluginName) {
        PluginRuntimeManager.getInstance(PluginApplication.getAppContext()).removePlugin(pluginName);
    }
}
