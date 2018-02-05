package diy.com.play;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Created by Administrator on 2018/1/5.
 */

public abstract class SystemElectricityManager {
    private static int level;
    private static int scale;
    private static Context mContext;
    private static BroadcastReceiver broadcastReceiver;

    public static void initData(Context context, final OnSystemElectricityManager onSystemElectricityManager) {
        mContext=context;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
                    level = intent.getIntExtra("level", 0);
                    scale = intent.getIntExtra("scale", 100);
                    onSystemElectricityManager.onReceive(level, scale);
                }
            }
        };
        context.registerReceiver(broadcastReceiver, intentFilter);
    }

    public static int getCurrentElectricity() {
        return level;
    }

    public static int getMaxElectricity() {
        return scale;
    }
    public static void unregisterReceiver(){
        mContext.unregisterReceiver(broadcastReceiver);
        mContext=null;
    }
    interface OnSystemElectricityManager {
        void onReceive(int level, int scale);
    }

}
