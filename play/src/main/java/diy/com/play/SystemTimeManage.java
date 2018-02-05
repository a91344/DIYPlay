package diy.com.play;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2018/1/8.
 */

public class SystemTimeManage {
    public static String getCrrentTime() {
        return new SimpleDateFormat("hh:mm").format(new Date(System.currentTimeMillis()));
    }

    public static String longToString(long time) {
        time = time / 1000;
        long seconds = time % 60;
        long minutes = (time / 60) % 60;
        long hours = time / 3600;
        if (seconds == 0 && minutes == 0 && hours == 0) {
            seconds = 1;
        }
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
