package com.chen.fy.movemountain;

import android.app.Activity;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;

public class UiUtils {

    /**
     * 界面设置状态栏字体颜色
     */
    public static void changeStatusBarTextImgColor(Activity activity, boolean isBlack) {
        if (isBlack) {
            //设置状态栏黑色字体
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            //恢复状态栏白色字体
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }

    /**
     * 高亮文字
     */
    public static SpannableStringBuilder highLightText(String str, String key){
        int start = str.toLowerCase().indexOf(key.toLowerCase());
        int end = start+key.length();

        SpannableStringBuilder sb = new SpannableStringBuilder(str);
        sb.setSpan(
                new ForegroundColorSpan(Color.RED), //设置高亮的颜色
                start,   //起始坐标
                end,     //终止坐标
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE   //旗帜  一般不用改变
        );
        return sb;
    }
}
