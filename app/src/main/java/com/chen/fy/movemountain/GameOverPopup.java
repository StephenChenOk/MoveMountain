package com.chen.fy.movemountain;

import android.content.Context;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.PositionPopupView;

public class GameOverPopup extends PositionPopupView {
    public GameOverPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.game_over_layout;
    }
}
