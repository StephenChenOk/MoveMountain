package com.chen.fy.movemountain.popups;

import android.content.Context;

import androidx.annotation.NonNull;

import com.chen.fy.movemountain.R;
import com.lxj.xpopup.core.PositionPopupView;

public class TooSmallPopup extends PositionPopupView {

    public TooSmallPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.too_small_layout;
    }
}
