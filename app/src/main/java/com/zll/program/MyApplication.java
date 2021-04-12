package com.zll.program;


import android.os.Build;
import android.provider.Settings;
import android.provider.SyncStateContract;

import com.zll.program.ui.main.MainActivity2;
import com.zll.program.utils.UserPreference;

import me.goldze.mvvmhabit.base.BaseApplication;
import me.goldze.mvvmhabit.crash.CaocConfig;
import me.goldze.mvvmhabit.utils.KLog;

public class MyApplication extends BaseApplication {
  /*  // 正常状态
    public static final int STATE_NORMAL = 0;
    // 从后台回到前台
    public static final int STATE_BACK_TO_FRONT = 1;
    // 从前台进入后台
    public static final int STATE_FRONT_TO_BACK = 2;
    // APP状态
    private static int sAppState = STATE_NORMAL;
    // 标记程序是否已进入后台(依据onStop回调)
    private boolean flag;
    // 标记程序是否已进入后台(依据onTrimMemory回调)
    private boolean background;
    // 从前台进入后台的时间
    private static long frontToBackTime;
    // 从后台返回前台的时间
    private static long backToFrontTime;*/
    @Override
    public void onCreate() {
        super.onCreate();

        //是否开启日志打印
        KLog.init(BuildConfig.DEBUG);
        BaseApplication.setApplication(this);
        //设备号
        String androidID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        UserPreference.getInstance(this).setDeviceID(androidID+Build.SERIAL);
        //配置全局异常崩溃操作
        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //背景模式,开启沉浸式
                .enabled(true) //是否启动全局异常捕获
                .showErrorDetails(true) //是否显示错误详细信息
                .showRestartButton(true) //是否显示重启按钮
                .trackActivities(true) //是否跟踪Activity
                .minTimeBetweenCrashesMs(2000) //崩溃的间隔时间(毫秒)
                .errorDrawable(R.mipmap.ic_launcher) //错误图标
                .restartActivity(MainActivity2.class) //重新启动后的activity
                //.errorActivity(YourCustomErrorActivity.class) //崩溃后的错误activity
                //.eventListener(new YourCustomEventListener()) //崩溃后的错误监听
                .apply();



//        AutoSizeConfig.getInstance().getUnitsManager()
//                .setSupportDP(false)
//                .setSupportSP(false)
//                .setSupportSubunits(Subunits.MM);
//        //友盟
//        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null);
//        // 选用AUTO页面采集模式
//        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
//        TTSUtils.getInstance().init(getApplicationContext());
    }
    /*
        //进入后台30秒打开页面的判断，到时直接让application实现ActivityLifecycleCallbacks即可
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        // TRIM_MEMORY_UI_HIDDEN是UI不可见的回调, 通常程序进入后台后都会触发此回调,大部分手机多是回调这个参数
        // TRIM_MEMORY_BACKGROUND也是程序进入后台的回调, 不同厂商不太一样, 魅族手机就是回调这个参数
        if (level == Application.TRIM_MEMORY_UI_HIDDEN || level == Application.TRIM_MEMORY_BACKGROUND) {
            background = true;
        } else if (level == Application.TRIM_MEMORY_COMPLETE) {
            background = !isCurAppTop(this);
        }
        if (background) {
            frontToBackTime = System.currentTimeMillis();
            sAppState = STATE_FRONT_TO_BACK;
            Log.e(TAG, "onTrimMemory: TRIM_MEMORY_UI_HIDDEN || TRIM_MEMORY_BACKGROUND");
        } else {
            sAppState = STATE_NORMAL;
        }

    }
    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        if (background || flag) {
            background = false;
            flag = false;
            sAppState = STATE_BACK_TO_FRONT;
            backToFrontTime = System.currentTimeMillis();
            Log.e(TAG, "onResume: STATE_BACK_TO_FRONT");
            if (canShowAd()) {
                // TODO: 2020/3/10 显示广告
                // ShowADActivity.show(activity);
            }
        } else {
            sAppState = STATE_NORMAL;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        //判断当前activity是否处于前台
        if (!isCurAppTop(activity)) {
            // 从前台进入后台
            sAppState = STATE_FRONT_TO_BACK;
            frontToBackTime = System.currentTimeMillis();
            flag = true;
            Log.e(TAG, "onStop: " + "STATE_FRONT_TO_BACK");
        } else {
            // 否则是正常状态
            sAppState = STATE_NORMAL;
        }
    }


    *//**
     * 进入后台间隔10分钟以后可以再次显示广告
     *
     * @return 是否能显示广告
     *//*
    public static boolean canShowAd() {
        return sAppState == STATE_BACK_TO_FRONT &&
                (backToFrontTime - frontToBackTime) > 10 * 60 * 1000;
    }
    *//**
     * 判断当前程序是否前台进程
     *
     * @param context
     * @return
     *//*
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public  boolean isCurAppTop(Context context) {
        if (context == null) {
            return false;
        }
        String curPackageName = context.getPackageName();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ActivityManager.RunningTaskInfo info = list.get(0);
            String topPackageName = info.topActivity.getPackageName();
            String basePackageName = info.baseActivity.getPackageName();
            if (topPackageName.equals(curPackageName) && basePackageName.equals(curPackageName)) {
                return true;
            }
        }
        return false;
    }*/

}
