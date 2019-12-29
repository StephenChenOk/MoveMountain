package com.chen.fy.movemountain.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chen.fy.movemountain.R;
import com.chen.fy.movemountain.utils.UiUtils;

public class GuideActivity extends AppCompatActivity {

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            Intent intent = new Intent(GuideActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_layout);

        initView();

        handler.sendEmptyMessageDelayed(0, 3000);
    }

    private void initView() {

        UiUtils.changeStatusBarTextImgColor(this, true);

        ImageView ivGuide = findViewById(R.id.iv_guide);
        ivGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                handler.removeCallbacksAndMessages(null);
                startActivity(intent);
                finish();
            }
        });
    }
}
