package com.wangyeming.plugindemo2;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.wangyeming.pluginapi.LayoutInflaterManager;

/**
 * Created by wangyeming on 2017/9/7.
 */
public class CustomView extends FrameLayout {
    public CustomView(Context context) {
        this(context, null);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflaterManager.getInstance().getLayoutInflater(getContext())
                .inflate(R.layout.plugin_demo2_view_custom, this, true);
    }
}
