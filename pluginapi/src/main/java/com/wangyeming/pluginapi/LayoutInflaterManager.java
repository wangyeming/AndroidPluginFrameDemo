package com.wangyeming.pluginapi;

import android.content.Context;
import android.content.res.AssetManager;
import android.view.LayoutInflater;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangyeming on 2017/9/4.
 */
public class LayoutInflaterManager {

    private LayoutInflaterManager() {

    }

    private static LayoutInflaterManager sInstance = new LayoutInflaterManager();

    public static LayoutInflaterManager getInstance() {
        return sInstance;
    }

    Map<AssetManager, LayoutInflater> mLayoutInflaterCache = new HashMap<>();

    public LayoutInflater getLayoutInflater(Context context) {
        LayoutInflater layoutInflater = mLayoutInflaterCache.get(context.getAssets());
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(context).cloneInContext(context);
            LayoutInflaterFactory factory = new LayoutInflaterFactory();
            layoutInflater.setFactory2(factory);
            mLayoutInflaterCache.put(context.getAssets(), layoutInflater);
        }
        return layoutInflater;
    }

    public void clear() {
        mLayoutInflaterCache.clear();
    }
}
