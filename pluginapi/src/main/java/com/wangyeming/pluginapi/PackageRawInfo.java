package com.wangyeming.pluginapi;

import java.util.ArrayList;
import java.util.List;

/**
 * 插件包信息
 * <p>
 * Created by wangyeming on 2017/9/5.
 */
public class PackageRawInfo {

    public int mVersion;            //插件版本号
    public String mVersionName;     //插件版本名称
    public String mPackageName;     //插件包名
    public int mMinApiLevel;        //最小Api版本号
    public String mMessageHandlerName;   //Message handler的名称
    public List<String> mUrlList = new ArrayList<>();       //支持的url列表

    public PackageRawInfo() {

    }
}
