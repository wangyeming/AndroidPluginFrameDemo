package com.wangyeming.plugindemo;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by wangyeming on 2017/9/5.
 */
public class ProcessUtil {

    public static String sProcessName = null;

    public static void initProcessName(Context context) {
        if (sProcessName == null) {
            ActivityManager am = ((ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE));
            List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
            int myPid = android.os.Process.myPid();
            for (ActivityManager.RunningAppProcessInfo info : processInfos) {
                if (info.pid == myPid) {
                    sProcessName = info.processName;
                }
            }
        }
    }

    public static String getCurrentProcessName(Context context) {
        initProcessName(context);
        return sProcessName;
    }

    public static boolean isMainProcess(Context context) {
        initProcessName(context);
        return "com.wangyeming.plugindemo".equals(sProcessName);
    }

}
