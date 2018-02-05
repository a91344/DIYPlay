package diy.com.play;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Administrator on 2018/1/5.
 */

public class ShowAndHideAnimoUtil {
    public static final long PLAY_TIME = 500;
    private static final int SHOW_TOP = 1;
    private static final int SHOW_BOTTOM = 2;
    private static final int HIDE_TOP = 3;
    private static final int HIDE_BOTTOM = 4;

    public static void showTop(View v) {
        v.startAnimation(getAnimation(SHOW_TOP));
        v.setVisibility(VISIBLE);
    }

    public static void hideTop(View v) {
        v.startAnimation(getAnimation(HIDE_TOP));
        v.setVisibility(GONE);
    }

    public static void showBottom(View v) {
        v.startAnimation(getAnimation(SHOW_BOTTOM));
        v.setVisibility(VISIBLE);
    }

    public static void hideBottom(View v) {
        v.startAnimation(getAnimation(HIDE_BOTTOM));
        v.setVisibility(GONE);
    }

    private static TranslateAnimation getAnimation(int type) {
        TranslateAnimation translateAnimation;
        switch (type) {
            case SHOW_TOP:
                translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                        0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF,
                        0.0f);
                translateAnimation.setRepeatMode(Animation.REVERSE);
                break;
            case SHOW_BOTTOM:
                translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                        0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF,
                        0.0f);
                translateAnimation.setRepeatMode(Animation.REVERSE);
                break;
            case HIDE_TOP:
                translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                        0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
                break;
            case HIDE_BOTTOM:
                translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                        0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
                break;
            default:
                translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                        0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                        Animation.RELATIVE_TO_SELF, -0f, Animation.RELATIVE_TO_SELF,
                        0.0f);
                translateAnimation.setRepeatMode(Animation.REVERSE);
                break;
        }
        translateAnimation.setDuration(PLAY_TIME);
        return translateAnimation;
    }
}
