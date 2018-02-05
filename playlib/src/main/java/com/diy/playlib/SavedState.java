package com.diy.playlib;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by Administrator on 2017/12/29.
 */

public class SavedState extends View.BaseSavedState {
    private float height;
    private float width;
    private String playStatus;
    private String fullScreenStatus;
    private IjkMediaPlayer ijkMediaPlayer;

    public String getPlayStatus() {
        return playStatus;
    }

    public void setPlayStatus(String playStatus) {
        this.playStatus = playStatus;
    }

    public String getFullScreenStatus() {
        return fullScreenStatus;
    }

    public void setFullScreenStatus(String fullScreenStatus) {
        this.fullScreenStatus = fullScreenStatus;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }


    public IjkMediaPlayer getIjkMediaPlayer() {
        return ijkMediaPlayer;
    }

    public void setIjkMediaPlayer(IjkMediaPlayer ijkMediaPlayer) {
        this.ijkMediaPlayer = ijkMediaPlayer;
    }


    public SavedState(Parcel source) {
        super(source);
    }

    public SavedState(Parcelable superState) {
        super(superState);
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
    }

    @Override
    public int describeContents() {
        return super.describeContents();
    }
}
