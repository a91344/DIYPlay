package diy.com.play;

import android.content.Context;
import android.media.AudioManager;

/**
 * Created by Administrator on 2018/1/4.
 */

public class SystemVolumeManager {
    private static AudioManager systemService;

    public static void initData(Context mContext) {
        systemService = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
    }

    public static int getMaxSystemVolume() {
        return systemService.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

    }

    public static int getCurrentSystemVolume() {
        return systemService.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    public static void setSystemVolume(int volume) {
        systemService.setStreamVolume(AudioManager.STREAM_MUSIC,volume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }
}
