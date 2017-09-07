package com.wangyeming.pluginapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * 插件基类Activity，本质上是一个Activity壳，
 * <p>
 * 在这里我们做了两件事，一个是复写activity的各种方法，实际调用的是host的方法
 * 另一个就是支持
 * <p>
 * Created by wangyeming on 2017/9/4.
 */
public class PluginBaseActivity extends AppCompatActivity implements IPluginActivity {

    public static final String FINISH_TAG = "plugin_finish";
    public static final String LAST_FINISH_ACTIVITY = "plugin_last_finish_activity";

    protected IHostActivity mHostBaseActivity;
    protected Activity mActivity;
    protected PluginPackage mPluginPackage;

    @Override
    public void attach(IHostActivity iHostActivity, Activity activity, PluginPackage pluginPackage) {
        mHostBaseActivity = iHostActivity;
        mActivity = activity;
        mPluginPackage = pluginPackage;

        attachBaseContext(mActivity);
    }

    @Override
    public Activity getHostActivity() {
        return mActivity;
    }

    @Override
    public void setContentView(View view) {
        mActivity.setContentView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        mActivity.setContentView(view, params);
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = getLayoutInflater().inflate(layoutResID, null);
        mActivity.setContentView(view);
    }

    @Override
    public void addContentView(View view, ViewGroup.LayoutParams params) {
        mActivity.addContentView(view, params);
    }

    @Override
    public Looper getMainLooper() {
        return mActivity.getMainLooper();
    }

    @Override
    public View findViewById(int id) {
        return mActivity.findViewById(id);
    }

    @Override
    public Intent getIntent() {
        return mActivity.getIntent();
    }

    @Override
    public void setIntent(Intent newIntent) {
        mActivity.setIntent(newIntent);
    }

    @Override
    public ClassLoader getClassLoader() {
        return mActivity.getClassLoader();
    }

    @Override
    public Resources getResources() {
        return mActivity.getResources();
    }

    @Override
    public AssetManager getAssets() {
        return mActivity.getAssets();
    }

    @Override
    public String getPackageName() {
        return mActivity.getPackageName();
    }

    @Override
    public LayoutInflater getLayoutInflater() {
        return LayoutInflaterManager.getInstance().getLayoutInflater(this);
    }

    @Override
    public MenuInflater getMenuInflater() {
        return mActivity.getMenuInflater();
    }

    @Override
    public SharedPreferences getSharedPreferences(String name, int mode) {
        return mActivity.getSharedPreferences(name, mode);
    }

    @Override
    public Context getApplicationContext() {
        return mActivity.getApplicationContext();
    }

    @Override
    public WindowManager getWindowManager() {
        return mActivity.getWindowManager();
    }

    @Override
    public Window getWindow() {
        return mActivity.getWindow();
    }

    @Override
    public Object getSystemService(String name) {
        return mActivity.getSystemService(name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
//        super.onStart();
    }

    @Override
    public void onRestart() {
//        super.onRestart();
    }

    @Override
    public void onResume() {
//        super.onResume();
    }

    @Override
    public void onPostResume() {
//        super.onPostResume();
    }

    @Override
    public void onPause() {
//        super.onPause();
    }

    @Override
    public void onStop() {
//        super.onStop();
    }

    @Override
    public void onDestroy() {
//        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
    }

    @Override
    public void finish() {
        mActivity.finish();
    }

    @Override
    public void onBackPressed() {
        mActivity.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            boolean isFinish = data.getBooleanExtra(FINISH_TAG, false);
            String activitName = this.getClass().getName();
            String lastActivity = data.getStringExtra(LAST_FINISH_ACTIVITY);
            if (isFinish && !activitName.equals(lastActivity)) {
                finishParent(lastActivity);
            }
        }
    }

    /**
     * 按照调用栈一直回退finish，直到lastActivityClass为止,当lastActivityClass为null会一直回退到设备列表
     *
     * @param lastActivityClass
     */
    public void finishParent(String lastActivityClass) {
        Intent data = new Intent();
        data.putExtra(FINISH_TAG, true);
        if (!TextUtils.isEmpty(lastActivityClass)) {
            data.putExtra(LAST_FINISH_ACTIVITY, lastActivityClass);
        }
        mActivity.setResult(RESULT_OK, data);
        finish();
    }

    public void startActivity(Intent intent, Class<? extends PluginBaseActivity> activityClass) {
        PluginHostApi.getInstance().startActivity(this, mPluginPackage, intent, activityClass);
    }


    public void startActivityForResult(Intent intent, Class<? extends PluginBaseActivity> activityClass,
                                       int requestCode) {
        PluginHostApi.getInstance().startActivityForResult(this, mPluginPackage, intent,
                null, requestCode);
    }
}
