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
}
