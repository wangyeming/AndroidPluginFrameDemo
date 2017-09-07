package com.wangyeming.plugin;

import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.wangyeming.pluginapi.HostInfo;
import com.wangyeming.pluginapi.IHostActivity;
import com.wangyeming.pluginapi.PluginBaseActivity;
import com.wangyeming.pluginapi.PluginPackage;

import java.lang.reflect.Constructor;

/**
 * Created by wangyeming on 2017/9/4.
 */
public class HostBaseActivity extends FragmentActivity implements IHostActivity {

    protected PluginBaseActivity mPluginBaseActivity;
    protected HostInfo mHostInfo;
    protected PluginPackage mPluginPackage;
    protected Resources.Theme mTheme;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Object hostInfo = getLastCustomNonConfigurationInstance();
        if(hostInfo != null) {
            mHostInfo = (HostInfo) hostInfo;
        }
        if (mHostInfo == null) {
            mHostInfo = PluginUtil.parseHostInfo(intent);
        }
        if (!TextUtils.isEmpty(mHostInfo.mPackagePath)) {
            mPluginPackage = PluginRuntimeManager.getInstance(this).getPluginPackageByPackagePath(mHostInfo.mPackagePath);
        }
        if (mPluginPackage == null) {
            setResult(RESULT_CANCELED);
            finish();
            return;
        }
        handleActivityInfo();
        launchActivity(intent);
    }

    protected void handleActivityInfo() {
        Resources.Theme superTheme = super.getTheme();
        mTheme = mPluginPackage.getResources().newTheme();
        mTheme.setTo(superTheme);

        int defaultTheme = R.style.PluginTheme;
        // Finals适配三星以及部分加载XML出现异常BUG
        try {
            mTheme.applyStyle(defaultTheme, true);
        } catch (Exception e) {

        }
    }

    private void launchActivity(Intent intent) {
        try {
            Class<?> localClass = getClassLoader().loadClass(mHostInfo.mClass);
            Constructor<?> localConstructor = localClass.getConstructor();
            Object instance = localConstructor.newInstance();
            mPluginBaseActivity = (PluginBaseActivity) instance;
            mPluginBaseActivity.attach(this, this, mPluginPackage);
            mPluginBaseActivity.setIntent(intent);
            mPluginBaseActivity.onCreate(intent.getExtras());
        } catch (Exception e) {
            e.printStackTrace();
            finish();
            return;
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mPluginBaseActivity.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPluginBaseActivity.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mPluginBaseActivity.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPluginBaseActivity.onResume();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(mPluginBaseActivity != null) {
            mPluginBaseActivity.onPostResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mPluginBaseActivity != null) {
            mPluginBaseActivity.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mPluginBaseActivity != null) {
            mPluginBaseActivity.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPluginBaseActivity != null) {
            mPluginBaseActivity.onDestroy();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mPluginBaseActivity.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mPluginBaseActivity.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPluginBaseActivity.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPluginBaseActivity.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mPluginBaseActivity.onNewIntent(intent);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return mHostInfo;
    }

    @NonNull
    @Override
    public LayoutInflater getLayoutInflater() {
        return mPluginBaseActivity.getLayoutInflater();
    }

    @Override
    public void onWindowAttributesChanged(WindowManager.LayoutParams params) {
        mPluginBaseActivity.onWindowAttributesChanged(params);
        super.onWindowAttributesChanged(params);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        mPluginBaseActivity.onWindowFocusChanged(hasFocus);
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mPluginBaseActivity.onCreateOptionsMenu(menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mPluginBaseActivity.onOptionsItemSelected(item);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onContentChanged() {
//        super.onContentChanged();
        mPluginBaseActivity.onContentChanged();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        mPluginBaseActivity.onAttachedToWindow();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mPluginBaseActivity.onDetachedFromWindow();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mPluginBaseActivity.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        mPluginBaseActivity.onTrimMemory(level);
    }

    @Override
    public boolean onTrackballEvent(MotionEvent event) {
        if(mPluginBaseActivity.onTrackballEvent(event)) {
            return true;
        }
        return super.onTrackballEvent(event);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        mPluginBaseActivity.onUserInteraction();
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        mPluginBaseActivity.onUserInteraction();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mPluginBaseActivity.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {
        if(mPluginPackage == null) {
            return super.getResources();
        } else {
            return mPluginPackage.getResources();
        }
    }

    @Override
    public AssetManager getAssets() {
        if(mPluginPackage == null) {
            return super.getAssets();
        } else {
            return mPluginPackage.getAssetManager();
        }
    }

    @Override
    public ClassLoader getClassLoader() {
        if(mPluginPackage == null) {
            return super.getClassLoader();
        } else {
            return mPluginPackage.getClassLoader();
        }
    }

    @Override
    public Resources.Theme getTheme() {
        if(mTheme == null) {
            return super.getTheme();
        } else {
            return mTheme;
        }
    }
}
