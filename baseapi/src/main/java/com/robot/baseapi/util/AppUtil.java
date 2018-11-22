package com.robot.baseapi.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by lny on 2017/10/16.
 */

public class AppUtil {
    /**
     * 获取versionCode
     *
     * @return
     */
    public static int getVersionCode(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            return manager.getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取已安装app 的版本号
     *
     * @param context
     * @param packageName
     * @return
     */
    public static int getAppVersionCode(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        try {
            return manager.getPackageInfo(packageName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 获取versionName
     *
     * @return
     */
    public static String getVersionName(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            return manager.getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取已安装app的versionName
     *
     * @return
     */
    public static String getAppVersionName(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        try {
            return manager.getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 获取apk 的应用名称
     *
     * @param context
     * @param apkPath
     * @return
     */
    public static String getApkName(Context context, String apkPath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkPath,
                PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            appInfo.sourceDir = apkPath;
            appInfo.publicSourceDir = apkPath;
            try {
                return appInfo.loadLabel(pm).toString();
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取apk 包名
     *
     * @param context
     * @param apkPath
     * @return
     */
    public static String getApkPackageName(Context context, String apkPath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkPath,
                PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            appInfo.sourceDir = apkPath;
            appInfo.publicSourceDir = apkPath;
            try {
                return appInfo.packageName;
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 获取apk 版本名称
     *
     * @param context
     * @param apkPath
     * @return
     */
    public static String getApkVersionName(Context context, String apkPath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkPath,
                PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            appInfo.sourceDir = apkPath;
            appInfo.publicSourceDir = apkPath;
            try {
                String version = info.versionName == null ?
                        "0" : info.versionName;
                return version;
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 获取apk 版本号
     *
     * @param context
     * @param apkPath
     * @return
     */
    public static int getApkVersionCode(Context context, String apkPath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkPath,
                PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            appInfo.sourceDir = apkPath;
            appInfo.publicSourceDir = apkPath;
            try {
                int versionCode =
                        info.versionCode;
                return versionCode;
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    /**
     * 安装apk
     *
     * @param context
     * @param apkPath
     */
    public static void installApk(Context context, String apkPath) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + apkPath),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 打开app
     *
     * @param context
     * @param packagename
     */
    public static boolean openApp(Context context, String packagename) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(packagename);
        try {
            context.startActivity(intent);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean openApp(Context context, String packagename, Map<String, Object> params) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(packagename);
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                Object value = params.get(key);
                if (value instanceof String) {
                    intent.putExtra(key, String.valueOf(value));
                } else if (value instanceof Integer) {
                    intent.putExtra(key, (int) value);
                } else if (value instanceof Boolean) {
                    intent.putExtra(key, (boolean) value);
                } else if (value instanceof Float) {
                    intent.putExtra(key, (float) value);
                } else if (value instanceof Double) {
                    intent.putExtra(key, (double) value);
                } else if (value instanceof Serializable) {
                    intent.putExtra(key, (Serializable) value);
                } else if (value instanceof Parcelable) {
                    intent.putExtra(key, (Parcelable) value);
                } else {
                    intent.putExtra(key, value.toString());
                }
            }
        }


        try {
            context.startActivity(intent);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 打开外部的action
     *
     * @param context
     * @param action
     * @param flags
     */
    public static boolean openExteranlActivity(Context context, String action, int... flags) {
        Intent intent = new Intent(action);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        if (flags != null && flags.length > 0) {
            for (int flag : flags) {
                if (flag != Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(flag);
            }
        }
        try {
            context.startActivity(intent);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean openExteranlActivity(Context context, String action, Map<String, Object> params, int... flags) {
        Intent intent = new Intent(action);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        if (flags != null && flags.length > 0) {
            for (int flag : flags) {
                if (flag != Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(flag);
            }
        }
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                Object value = params.get(key);
                if (value instanceof String) {
                    intent.putExtra(key, String.valueOf(value));
                } else if (value instanceof Integer) {
                    intent.putExtra(key, (int) value);
                } else if (value instanceof Boolean) {
                    intent.putExtra(key, (boolean) value);
                } else if (value instanceof Float) {
                    intent.putExtra(key, (float) value);
                } else if (value instanceof Double) {
                    intent.putExtra(key, (double) value);
                } else if (value instanceof Serializable) {
                    intent.putExtra(key, (Serializable) value);
                } else if (value instanceof Parcelable) {
                    intent.putExtra(key, (Parcelable) value);
                } else {
                    intent.putExtra(key, value.toString());
                }
            }
        }

        try {
            context.startActivity(intent);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    /*
     * 採用了新的办法获取APK图标。之前的失败是由于android中存在的一个BUG,通过
     * appInfo.publicSourceDir = apkPath;来修正这个问题，详情參见:
     * http://code.google.com/p/android/issues/detail?id=9151
     */
    public static Drawable getApkIcon(Context context, String apkPath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkPath,
                PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            appInfo.sourceDir = apkPath;
            appInfo.publicSourceDir = apkPath;
            try {
                return appInfo.loadIcon(pm);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取app icon
     *
     * @param context
     * @param packageName
     * @return
     */
    public static Drawable getAppIcon(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = pm.getPackageInfo(packageName, 0);
            if (info != null) {
                return info.applicationInfo.loadIcon(pm);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @param context
     * @param packageName
     * @return
     * @desc 检测app是否安装
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        List<String> pName = new ArrayList<String>();
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);
    }


    /**
     * 获取application中指定的meta-data
     *
     * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
     */
    public static String getAppMetaDataString(Context ctx, String key) {
        if (ctx == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return resultData;
    }

    /**
     * 获取application中指定的meta-data
     *
     * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为0
     */
    public static int getAppMetaDataInt(Context ctx, String key) {
        if (ctx == null || TextUtils.isEmpty(key)) {
            return 0;
        }
        int resultData = 0;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getInt(key);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return resultData;
    }
}
