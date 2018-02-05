package diy.com.play;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

import pl.droidsonroids.gif.GifImageView;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by Administrator on 2017/12/28.
 */

public class IJKVideoView extends FrameLayout implements SurfaceHolder.Callback, View.OnClickListener {
    private SurfaceView ijkSf;
    private Button ijkBtPlay;
    private SeekBar ijkSb;
    private RelativeLayout ijkRlStatus;
    private LinearLayout ijkLlTopStatus;
    private LinearLayout ijkLlBottomStatus;
    private Button ijkBtFullScreen;
    private GifImageView ijkGifLoding;
    private LinearLayout ijkLlManager;
    private LinearLayout ijkLlVolume;
    private TextView ijkTvVolume;
    private LinearLayout ijkLlBrightness;
    private TextView ijkTvBrightness;
    private IjkMediaPlayer ijkMediaPlayer;
    private TextView ijkTvReturn;
    private TextView ijkTvTitle;
    private TextView ijkTvElcOrTime;

    private Thread thread;

    private float volume;
    private float brightness;
    private float volumeMax;
    private float brightnessMax;
    private boolean isPlay;
    private float height;
    private float width;
    private int level;

    public IJKVideoView(Context context) {
        super(context);
        initView(context);
        initData();
        initEvent();
    }

    public IJKVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initData();
        initEvent();
    }

    public IJKVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initData();
        initEvent();
    }

    private void initView(Context context) {
        this.addView(LayoutInflater.from(context).inflate(R.layout.ijk_play_vdieo_view, this, false));
        ijkSf = (SurfaceView) findViewById(R.id.ijk_sf);
        ijkBtPlay = (Button) findViewById(R.id.ijk_bt_play);
        ijkSb = (SeekBar) findViewById(R.id.ijk_sb);
        ijkRlStatus = (RelativeLayout) findViewById(R.id.ijk_rl_status);
        ijkLlTopStatus = (LinearLayout) findViewById(R.id.ijk_ll_top_status);
        ijkLlBottomStatus = (LinearLayout) findViewById(R.id.ijk_ll_bottom_status);

        ijkBtFullScreen = (Button) findViewById(R.id.ijk_bt_full_screen);
        ijkGifLoding = (GifImageView) findViewById(R.id.ijk_gif_loding);

        ijkLlManager = (LinearLayout) findViewById(R.id.ijk_ll_manager);
        ijkLlVolume = (LinearLayout) findViewById(R.id.ijk_ll_volume);
        ijkTvVolume = (TextView) findViewById(R.id.ijk_tv_volume);
        ijkLlBrightness = (LinearLayout) findViewById(R.id.ijk_ll_brightness);
        ijkTvBrightness = (TextView) findViewById(R.id.ijk_tv_brightness);

        ijkTvReturn = (TextView) findViewById(R.id.ijk_tv_return);
        ijkTvTitle = (TextView) findViewById(R.id.ijk_tv_title);
        ijkTvElcOrTime = (TextView) findViewById(R.id.ijk_tv_elc_or_time);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ((Activity) context).getWindow().getDecorView().setSystemUiVisibility(View.INVISIBLE);
        }
        Log.i("AX1", "initView: ");
    }

    private void initData() {
        SystemBrightManager.initData(getContext());
        SystemVolumeManager.initData(getContext());
        SystemElectricityManager.initData(getContext(), new SystemElectricityManager.OnSystemElectricityManager() {
            @Override
            public void onReceive(int level, int scale) {
                IJKVideoView.this.level = level;
                ijkTvElcOrTime.setText("电量:" + level + "\n" + "时间:" + SystemTimeManage.getCrrentTime());
            }
        });

        volume = SystemVolumeManager.getCurrentSystemVolume();
        brightness = SystemBrightManager.getSystemScreenBrightness();
        volumeMax = SystemVolumeManager.getMaxSystemVolume();
        brightnessMax = 255;

        ijkMediaPlayer = new IjkMediaPlayer();
        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1);

        SurfaceHolder holder = ijkSf.getHolder();
        holder.addCallback(this);

        ijkGifLoding.setVisibility(GONE);
        ijkBtFullScreen.setText("全屏");

        ijkLlVolume.setVisibility(INVISIBLE);
        ijkLlBrightness.setVisibility(INVISIBLE);

        level = SystemElectricityManager.getCurrentElectricity();

        Log.i("AX1", "initData: ");
    }

    private void initEvent() {
        ijkMediaPlayer.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                ijkSb.setMax((int) iMediaPlayer.getDuration());
                height = getHeight();
                width = getWidth();
                creatTimeThread();
                ijkGifLoding.setVisibility(GONE);
                ijkMediaPlayer.seekTo(1);
                ijkMediaPlayer.pause();
            }
        });
        ijkMediaPlayer.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                ijkBtPlay.setBackgroundResource(R.drawable.stop_button);
                iMediaPlayer.seekTo(0);
            }
        });
        ijkSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                ijkMediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        ijkLlManager.setOnTouchListener(new OnTouchListener() {
            private float oldY;
            private float oldX;
            private float eventY;
            private float maxHeight;
            private float maxWidth;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        oldY = event.getY();
                        oldX = event.getX();
                        eventY = event.getY();
                        volume = SystemVolumeManager.getCurrentSystemVolume();
                        brightness = SystemBrightManager.getSystemScreenBrightness();
                        maxHeight = getMeasuredHeight();
                        maxWidth = getMeasuredWidth();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (oldX < maxWidth * 0.25
                                && Math.abs(oldY - event.getY()) > 20) {
                            int temp = (int) ((oldY - event.getY()) / maxHeight * volumeMax);
                            if (eventY > event.getY()) {
                                ijkTvVolume.setText((int) ((volume + temp) / volumeMax * 100) + "%");
                                SystemVolumeManager.setSystemVolume((int) (volume + temp));
                                eventY = event.getY();
                            } else {
                                ijkTvVolume.setText((int) ((volume + temp) / volumeMax * 100) + "%");
                                SystemVolumeManager.setSystemVolume((int) (volume + temp));
                                eventY = event.getY();
                            }
                            if (volume + temp < 0) {
                                ijkTvVolume.setText("0%");
                                SystemVolumeManager.setSystemVolume(0);
                            } else if (volume + temp > volumeMax) {
                                ijkTvVolume.setText("100%");
                                SystemVolumeManager.setSystemVolume((int) volumeMax);
                            }
                            Log.i("AX1", "onTouch: " + (volume + temp));
                            ijkLlVolume.setVisibility(VISIBLE);
                        } else if (oldX > maxWidth * 0.75 && Math.abs(oldY - event.getY()) > 20) {
                            if (eventY > event.getY()) {
                                brightness++;
                                SystemBrightManager.setSystemScreenBrightness((int) brightness);
                                ijkTvBrightness.setText(((int) (brightness / brightnessMax * 100)) + "%");
                                eventY = event.getY();
                            } else {
                                brightness--;
                                SystemBrightManager.setSystemScreenBrightness((int) brightness);
                                ijkTvBrightness.setText(((int) (brightness / brightnessMax * 100)) + "%");
                                eventY = event.getY();
                            }
                            if (brightness < 1) {
                                SystemBrightManager.setSystemScreenBrightness(1);
                                ijkTvBrightness.setText(0 + "%");
                                brightness = 1;
                            } else if (brightness > brightnessMax) {
                                SystemBrightManager.setSystemScreenBrightness((int) brightnessMax);
                                ijkTvBrightness.setText(100 + "%");
                                brightness = brightnessMax;
                            }
                            ijkLlBrightness.setVisibility(VISIBLE);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (Math.abs(oldX - event.getX()) <= 20
                                && Math.abs(oldY - event.getY()) <= 20
                                && Math.abs(event.getDownTime() - event.getEventTime()) < 300) {
                            ifShowOrHideStatus();
                        } else {
                            ijkLlVolume.setVisibility(INVISIBLE);
                            ijkLlBrightness.setVisibility(INVISIBLE);
                            volume = SystemVolumeManager.getCurrentSystemVolume();
                        }
                        break;
                }
                return false;
            }
        });

        ijkBtPlay.setOnClickListener(this);
        ijkBtFullScreen.setOnClickListener(this);
        Log.i("AX1", "initEvent: ");
    }

    private void creatTimeThread() {
        isPlay = true;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isPlay) {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        break;
                    }
                    ijkSb.setProgress((int) ijkMediaPlayer.getCurrentPosition());
                    Log.i("AX2", "run: \t" + ijkSb + "\t" + ijkSb.getMax() + "\t" + ijkSb.getProgress() + "\t" + thread.getName() + "\t" + ijkMediaPlayer.getCurrentPosition());
                }
                thread = null;
            }
        });
        thread.start();
        Log.i("AX1", "creatTimeThread: ");
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        return saveData(new SavedState(superState));
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState temp = (SavedState) state;
        super.onRestoreInstanceState(temp.getSuperState());
        restoreData((SavedState) state);
        Log.i("AX1", "onRestoreInstanceState: ");
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        ijkMediaPlayer.setDisplay(holder);
        try {
            ijkMediaPlayer.prepareAsync();
        } catch (IllegalStateException e) {
            return;
        }
        Log.i("AX1", "surfaceCreated: ");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i("AX1", "surfaceChanged: ");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i("AX1", "surfaceDestroyed: ");
    }


    public void setPath(String path) throws IOException {
        ijkMediaPlayer.setDataSource(path);
        ijkGifLoding.setVisibility(VISIBLE);
        Log.i("AX1", "setPath: ");
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isPlay = false;
    }

    @Override
    public void onClick(View v) {
        upPlayStatusOrFullScreenStatus((TextView) v);
    }

    private SavedState saveData(SavedState temp) {
//        if (((Activity) getContext()).getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//            height = getHeight();
//            width = getWidth();
//        }
//        temp.setHeight(height);
//        temp.setWidth(width);
        temp.setIjkMediaPlayer(ijkMediaPlayer);
        temp.setPlayStatus(ijkBtPlay.getText().toString().trim());
        temp.setFullScreenStatus(ijkBtFullScreen.getText().toString().trim());
        isPlay = false;
        return temp;
    }

    private void restoreData(SavedState temp) {
        ijkMediaPlayer = temp.getIjkMediaPlayer();
        ijkSb.setMax((int) ijkMediaPlayer.getDuration());
//        width = temp.getWidth();
//        height = temp.getHeight();
        ijkBtPlay.setText(temp.getPlayStatus());
        ijkBtFullScreen.setText(temp.getFullScreenStatus());
        ijkBtPlay.setBackgroundResource(temp.getPlayStatus().equals("暂停") ? R.drawable.pause_button : R.drawable.play_button);
        ijkBtFullScreen.setBackgroundResource(temp.getFullScreenStatus().equals("全屏") ? R.drawable.full_screen : R.drawable.exit_full_screent);
        if (ijkMediaPlayer.getDuration() != 0) {
            creatTimeThread();
            ijkGifLoding.setVisibility(GONE);
        }
//        if (((Activity) getContext()).getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            this.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            this.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
//        } else {
//            this.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
//            this.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
//            this.getLayoutParams().width = (int) width;
//            this.getLayoutParams().height = (int) height;
//        }
        this.requestLayout();
    }

    private void upPlayStatusOrFullScreenStatus(TextView tempTv) {
        int i = tempTv.getId();
        if (i == R.id.ijk_bt_play) {
            switch (tempTv.getText().toString().trim()) {
                case "播放":
                    tempTv.setText("暂停");
                    ijkMediaPlayer.start();
                    tempTv.setBackgroundResource(R.drawable.pause_button);
                    break;
                case "暂停":
                    tempTv.setText("播放");
                    ijkMediaPlayer.pause();
                    tempTv.setBackgroundResource(R.drawable.play_button);
                    break;
            }

        } else if (i == R.id.ijk_bt_full_screen) {
            switch (tempTv.getText().toString().trim()) {
                case "全屏":
                    tempTv.setBackgroundResource(R.drawable.full_screen);
                    tempTv.setText("返回");
                    break;
                case "返回":
                    tempTv.setBackgroundResource(R.drawable.exit_full_screent);
                    tempTv.setText("全屏");
                    break;
            }
            if (((Activity) getContext()).getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                //切换竖屏
                ((Activity) getContext()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else {
                //切换横屏
                ((Activity) getContext()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }

        }
    }

    private void ifShowOrHideStatus() {
        ijkLlManager.setClickable(false);
        switch (ijkLlTopStatus.getVisibility()) {
            case VISIBLE:
                ShowAndHideAnimoUtil.hideBottom(ijkLlBottomStatus);
                ShowAndHideAnimoUtil.hideTop(ijkLlTopStatus);
                break;
            case GONE:
                ShowAndHideAnimoUtil.showBottom(ijkLlBottomStatus);
                ShowAndHideAnimoUtil.showTop(ijkLlTopStatus);
                break;
        }
        ijkLlManager.postDelayed(new Runnable() {
            @Override
            public void run() {
                ijkLlManager.setClickable(true);
            }
        }, ShowAndHideAnimoUtil.PLAY_TIME);
    }
}
