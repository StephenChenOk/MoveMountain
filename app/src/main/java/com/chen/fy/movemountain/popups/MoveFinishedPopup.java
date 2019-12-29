package com.chen.fy.movemountain.popups;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chen.fy.movemountain.R;
import com.chen.fy.movemountain.activities.MainActivity;
import com.lxj.xpopup.core.PositionPopupView;

public class MoveFinishedPopup extends PositionPopupView {

    private TextView tvTimes;
    private TextView tvScores;

    public MoveFinishedPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.game_over_finished_layout;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        tvTimes = findViewById(R.id.tv_times);
        tvScores = findViewById(R.id.tv_scores);

        tvTimes.setText("总共移山次数：" + MainActivity.moveTimes);
        int scores = 50 * MainActivity.wajuejiTimes + 30 * MainActivity.yunnicheTimes
                + 20 * MainActivity.chanziTimes + 10 * MainActivity.shouTimes;
        tvScores.setText("获得分数：" + scores);
    }
}
