package com.diy.playlib;

import android.content.Context;
import android.provider.Settings;

/**
 * Created by Administrator on 2018/1/4.
 */

public class SystemBrightManager {
    private static Context mContext;

    public static void initData(Context mContext) {
        SystemBrightManager.mContext = mContext;
        stopAutoBrightness();
    }

    /**
     * 获取系统亮度
     * 取值在(0 -- 255)之间
     */
    public static int getSystemScreenBrightness() {
        int values = 0;
        try {
            values = Settings.System.getInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return values;
    }

    /**
     * 设置系统亮度
     *
     * @param systemBrightness 返回的亮度值是处于0-255之间的整型数值
     */
    public static boolean setSystemScreenBrightness(int systemBrightness) {
        return Settings.System.putInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, systemBrightness);
    }

    /**
     * 系统是否自动调节亮度
     * return true 是自动调节亮度   return false 不是自动调节亮度
     */
    public static boolean isAutoBrightness() {
        int autoBrightness = Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
        try {
            autoBrightness = Settings.System.getInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        if (autoBrightness == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 关闭系统自动调节亮度
     */
    public static void stopAutoBrightness() {
        Settings.System.putInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
    }

    /**
     * 打开系统自动调节亮度
     */
    public static void startAutoBrightness() {
        Settings.System.putInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
    }

    public static void clean() {
        mContext = null;
    }
}
