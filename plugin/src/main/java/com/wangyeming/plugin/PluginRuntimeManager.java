package com.wangyeming.plugin;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;

import com.wangyeming.pluginapi.IPluginMessageReceiver;
import com.wangyeming.pluginapi.PackageRawInfo;
import com.wangyeming.pluginapi.PluginPackage;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

import dalvik.system.DexClassLoader;

/**
 * 插件运行的管理器
 * <p>
 * Created by wangyeming on 2017/9/4.
 */
public class PluginRuntimeManager {

    private Context mAppContext;
    private static volatile PluginRuntimeManager sInstance;

    private PluginRuntimeManager(Context context) {
        mAppContext = context.getApplicationContext();
    }

    public static PluginRuntimeManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (PluginRuntimeManager.class) {
                if (sInstance == null) {
                    sInstance = new PluginRuntimeManager(context);
                }
            }
        }
        return sInstance;
    }

    //根据包路径缓存PluginPackage
    private final ConcurrentHashMap<String, PluginPackage> mPackagePathPackages = new ConcurrentHashMap<>();
    //根据url缓存PluginPackage
    private final ConcurrentHashMap<String, PluginPackage> mUrlPackages = new ConcurrentHashMap<>();

    public PluginPackage getPluginPackageByPackagePath(String path) {
        return mPackagePathPackages.get(path);
    }

    public PluginPackage getPluginPackageByUrl(String url) {
        return mUrlPackages.get(url);
    }

    public PluginPackage loadApk(String pluginName, String packagePath, PackageRawInfo packageRawInfo) {
        PluginPackage pluginPackage = getPluginPackageByPackagePath(packagePath);
        if (pluginPackage != null) {
            return pluginPackage;
        }
        DexClassLoader dexClassLoader = createDexClassLoader(packagePath, packageRawInfo);
        AssetManager assetManager = createAssetManager(packagePath);
        Resources resources = createResources(assetManager);
        IPluginMessageReceiver pluginMessageReceiver = loadIPluginMessageReceiver(dexClassLoader, packageRawInfo);
        pluginPackage = new PluginPackage(pluginName,
                dexClassLoader,
                assetManager,
                resources,
                pluginMessageReceiver,
                packageRawInfo);
        mPackagePathPackages.put(pluginName, pluginPackage);
        Log.d("wym", "mPackagePathPackages put " + pluginName);
        for (String url : packageRawInfo.mUrlList) {
            mUrlPackages.put(url, pluginPackage);
        }
        return pluginPackage;
    }

    private DexClassLoader createDexClassLoader(String dexPath, PackageRawInfo packageRawInfo) {
        File dexOutputDir = mAppContext.getDir("dex", Context.MODE_PRIVATE);
        String dexOptimizedPath = dexOutputDir.getAbsolutePath()
                + File.separator
                + "plugin"
                + File.separator
                + packageRawInfo.mPackageName
                + File.separator
                + packageRawInfo.mVersion;

        FileUtils.createDirIfNotExists(dexOptimizedPath);
        return new DexClassLoader(dexPath, dexOptimizedPath, null,
                mAppContext.getClassLoader());
    }

    private AssetManager createAssetManager(String dexPath) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, dexPath);
            return assetManager;
        } catch (Exception e) {
            return null;
        }
    }

    private Resources createResources(AssetManager assetManager) {
        Resources superRes = mAppContext.getResources();
        return new Resources(assetManager,
                superRes.getDisplayMetrics(), superRes.getConfiguration());
    }

    private IPluginMessageReceiver loadIPluginMessageReceiver(DexClassLoader dexClassLoader,
                                                              PackageRawInfo packageRawInfo) {
        String messageHandlerName = packageRawInfo.mMessageHandlerName;
        if (TextUtils.isEmpty(messageHandlerName)) {
            return null;
        }
        IPluginMessageReceiver iPluginMessageReceiver = null;
        try {
            Class<?> localClass = dexClassLoader.loadClass(packageRawInfo.mMessageHandlerName);
            Constructor<?> localConstructor = localClass.getConstructor();
            Object instance = localConstructor.newInstance();
            iPluginMessageReceiver = (IPluginMessageReceiver) instance;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iPluginMessageReceiver;
    }

    public void removePlugin(String pluginName) {
        mPackagePathPackages.remove(pluginName);
        mUrlPackages.remove(pluginName);
    }
}
