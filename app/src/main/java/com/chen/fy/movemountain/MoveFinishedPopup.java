package com.chen.fy.movemountain;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.PositionPopupView;

public class MoveFinishedPopup extends PositionPopupView {

    //移山是否结束
    private boolean isFinish;

    public MoveFinishedPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.game_over_finished_layout;
    }
}
