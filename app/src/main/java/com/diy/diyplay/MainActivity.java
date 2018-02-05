package com.diy.diyplay;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;

import diy.com.play.IJKVideoView;

public class MainActivity extends AppCompatActivity {
    private IJKVideoView mainIjk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainIjk = (IJKVideoView) findViewById(R.id.main_ijk);
        try {
            mainIjk.setPath("/storage/emulated/0/DCIM/100MEDIA/VIDEO0001.mp4");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
