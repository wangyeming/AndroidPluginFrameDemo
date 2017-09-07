package com.wangyeming.plugindemo;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.wangyeming.pluginapi.PluginHostApi;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String DEMO1_PACKAGE_PATH = "com.wangyeming.plugindemo1";
    private static final String DEMO2_PACKAGE_PATH = "com.wangyeming.plugindemo2";

    private Button vDemo1Install;
    private Button vDemo1Remove;
    private ViewGroup vDemo1RouteGroup;
    private Button vDemo2Install;
    private Button vDemo2Remove;
    private ViewGroup vDemo2RouteGroup;

    private ProgressDialog vProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vDemo1Install = findViewById(R.id.plugin_demo1_install);
        vDemo1Remove = findViewById(R.id.plugin_demo1_delete);
        vDemo1RouteGroup = findViewById(R.id.plugin_demo1_route_group);
        vDemo2Install = findViewById(R.id.plugin_demo2_install);
        vDemo2Remove = findViewById(R.id.plugin_demo2_delete);
        vDemo2RouteGroup = findViewById(R.id.plugin_demo2_route_group);

        vDemo1Install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPlugin(DEMO1_PACKAGE_PATH, vDemo1Install, vDemo1Remove, vDemo1RouteGroup);
            }
        });

        vDemo2Install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadPlugin(DEMO2_PACKAGE_PATH, vDemo2Install, vDemo2Remove, vDemo2RouteGroup);
            }
        });

        vDemo1Remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PluginManager.getInstance().removePlugin(DEMO1_PACKAGE_PATH);
                showPluginNotInstall(vDemo1Install, vDemo1Remove, vDemo1RouteGroup);
            }
        });

        vDemo2Remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PluginManager.getInstance().removePlugin(DEMO2_PACKAGE_PATH);
                showPluginNotInstall(vDemo2Install, vDemo2Remove, vDemo2RouteGroup);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void loadPlugin(final String pluginName, final Button vInstall, final Button vRemove,
                            final ViewGroup vPageGroup) {
        AsyncTaskUtils.exe(new AsyncTask<Object, Object, Boolean>() {

            @Override
            protected void onPreExecute() {
                vProgressDialog = new ProgressDialog(MainActivity.this);
                vProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                vProgressDialog.setTitle("正在加载插件 " + pluginName + " 请稍候~");
                vProgressDialog.setCancelable(false);
                vProgressDialog.show();
            }

            @Override
            protected Boolean doInBackground(Object... objects) {
                return PluginManager.getInstance().installPlugin(pluginName);
            }

            @Override
            protected void onPostExecute(Boolean isInstallSuccess) {
                vProgressDialog.dismiss();
                if (isInstallSuccess) {
                    List<String> mUrls = PluginManager.getInstance().getUrls(pluginName);
                    showPluginInstalled(vInstall, vRemove, vPageGroup, mUrls);
                    if (mUrls == null || mUrls.isEmpty()) {
                        Toast.makeText(MainActivity.this, "没查到当前插件包的urls", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "插件 " + pluginName + " 加载失败",
                            Toast.LENGTH_SHORT).show();
                    showPluginNotInstall(vInstall, vRemove, vPageGroup);
                }
            }
        });
    }

    private void showPluginInstalled(Button vInstall, Button vRemove, ViewGroup vPageGroup, List<String> mUrls) {
        vInstall.setText("已安装");
        vInstall.setEnabled(false);
        vRemove.setEnabled(true);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(16, 0, 0, 0);
        if (mUrls == null) {
            return;
        }
        for (final String url : mUrls) {
            Button button = new Button(MainActivity.this);
            button.setText("点击进入 " + url);
            button.setLayoutParams(layoutParams);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PluginHostApi.getInstance().openUrl(url);
                }
            });
            vPageGroup.addView(button);
        }
    }

    private void showPluginNotInstall(Button vInstall, Button vRemove, ViewGroup vPageGroup) {
        vInstall.setText("安装插件");
        vInstall.setEnabled(true);
        vRemove.setEnabled(false);
        vPageGroup.removeAllViews();
    }
}
