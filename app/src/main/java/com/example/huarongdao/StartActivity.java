package com.example.huarongdao;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;

public class StartActivity extends Activity {
    private MediaPlayer bgmPlayer;
    @Override 
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // 首次启动弹窗
        SharedPreferences prefs = getSharedPreferences("huarongdao_prefs", MODE_PRIVATE);
        boolean firstLaunch = prefs.getBoolean("first_launch", true);
        if (firstLaunch) {
            new AlertDialog.Builder(this)
                .setTitle(getString(R.string.welcome_title))
                .setMessage(getString(R.string.welcome_message))
                .setPositiveButton(getString(R.string.got_it), null)
                .show();
            prefs.edit().putBoolean("first_launch", false).apply();
        }

        Button btnStart = findViewById(R.id.btn_start);
        Button btnExit = findViewById(R.id.btn_exit);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });

        // 启动背景音乐
        bgmPlayer = MediaPlayer.create(this, R.raw.index);
        bgmPlayer.setLooping(true);
        bgmPlayer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bgmPlayer != null) {
            bgmPlayer.stop();
            bgmPlayer.release();
            bgmPlayer = null;
        }
    }
} 