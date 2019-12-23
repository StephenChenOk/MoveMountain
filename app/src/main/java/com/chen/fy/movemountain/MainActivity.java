package com.chen.fy.movemountain;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.XPopupCallback;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivMountain;
    private Button btnMove;

    private HorizontalBarChart chart;

    //山的总权值
    private int mTotalWeight;
    //山剩余的权值
    private int mRemainWeight;
    //当前山的宽高
    private int mMountainWidth;
    private int mMountainHeight;

    //原始山的宽高
    private int mFirstWidth;
    private int mFirstHeight;

    //是否是原始的山大小
    private boolean isFirst = true;

    //y轴最大值
    private float chartWeight;

    //移山次数
    int moveTimes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initBar();
    }

    private void initView() {
        ivMountain = findViewById(R.id.iv_mountain);
        btnMove = findViewById(R.id.btn_move);

        btnMove.setOnClickListener(this);

    }

    private void initWeight() {
        mTotalWeight = 9999;

        mRemainWeight = mTotalWeight;

        mMountainWidth = 400;
        mMountainHeight = 400;

        chartWeight = mTotalWeight + 500;

        moveTimes = 0;
    }

    /**
     * 减少山的大小
     */
    private void reduceMountain() {

        moveTimes++;

        //减少的权值
        int i = getIndex(mRemainWeight);
        double temp = mRemainWeight - Math.pow(2, i);

        //宽高的缩小比例
        double reduceRadio = temp / mRemainWeight;

        //剩余的权值
        mRemainWeight = (int) temp;

        Log.d("chenyisheng", reduceRadio + "");

        //山此时的宽高
        mMountainWidth = (int) (mMountainWidth * reduceRadio);
        mMountainHeight = (int) (mMountainHeight * reduceRadio);

        //设置山体布局
        if (reduceRadio == 0) {
            gameOver();
        } else {
            setMountainLayout();
        }

        //重新绘制柱状图
        setData();
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    ivMountain.setLayoutParams(layoutParams);
                    break;
            }
        }
    };

    private ViewGroup.LayoutParams layoutParams;

    private void setMountainLayout() {

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.tween_scale);
        animation.setFillAfter(true);
        ivMountain.startAnimation(animation);

        if (isFirst) {
            mFirstWidth = ivMountain.getWidth();
            mFirstHeight = ivMountain.getHeight();
            isFirst = false;
        }
        layoutParams = ivMountain.getLayoutParams();

        switch (moveTimes) {
            case 0:
                layoutParams.width = mFirstWidth;
                layoutParams.height = mFirstHeight;
                break;
            case 1:
                layoutParams.width = mMountainWidth + 250;
                layoutParams.height = mMountainHeight + 250;
                break;
            case 2:
                layoutParams.width = mMountainWidth + 200;
                layoutParams.height = mMountainHeight + 200;
                break;
            case 3:
                layoutParams.width = mMountainWidth + 150;
                layoutParams.height = mMountainHeight + 150;
                break;
            case 4:
                layoutParams.width = mMountainWidth + 120;
                layoutParams.height = mMountainHeight + 120;
            case 5:
                layoutParams.width = mMountainWidth + 100;
                layoutParams.height = mMountainHeight + 100;
                break;
            case 6:
                layoutParams.width = mMountainWidth + 80;
                layoutParams.height = mMountainHeight + 80;
            case 7:
                layoutParams.width = mMountainWidth + 60;
                layoutParams.height = mMountainHeight + 60;
                break;
            case 8:
                layoutParams.width = mMountainWidth + 45;
                layoutParams.height = mMountainHeight + 45;
                break;
            case 9:
                layoutParams.width = mMountainWidth + 30;
                layoutParams.height = mMountainHeight + 30;
                break;
            default:
                layoutParams.width = mMountainWidth + 10;
                layoutParams.height = mMountainHeight + 10;
        }
        handler.sendEmptyMessageDelayed(0,2000);
        //ivMountain.setLayoutParams(layoutParams);
    }


    private void setData() {

        //柱子大小
        float barWidth = 2.5f;
        //柱子间间隔
        float spaceForBar = 3f;
        //柱子初始化
        ArrayList<BarEntry> values = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            //值
            if (i % 2 == 0) {
                float val = (float) (mRemainWeight);
                values.add(new BarEntry(i * spaceForBar, val));
            } else {
                float val = (float) (mTotalWeight - mRemainWeight);
                values.add(new BarEntry(i * spaceForBar, val));
            }
        }

        //数据集初始化
        BarDataSet set1 = new BarDataSet(values, "剩余量和已移除量柱状图");
        set1.setDrawIcons(true);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        data.setBarWidth(barWidth);

        //刷新数据
        chart.setData(data);
        chart.notifyDataSetChanged();
        chart.animateY(2000);
        chart.invalidate();
    }

    //获取距离目标最近的2的多少次方
    private int getIndex(int temp) {
        int i = -1;
        while (true) {
            i++;
            int f = (int) Math.pow(2, i);
            if (f > temp) {
                i--;
                break;
            }
        }
        return i;
    }

    //移山结束，点击可以重新开始
    private void gameOver() {
        ivMountain.setVisibility(View.GONE);
        new XPopup.Builder(this)
                .isCenterHorizontal(true)
                .offsetY(200)
                .setPopupCallback(new XPopupCallback() {
                    @Override
                    public void onCreated() {
                    }

                    @Override
                    public void onShow() {
                    }

                    @Override
                    public void onDismiss() {
                        initWeight();
                        setData();
                        setMountainLayout();
                        ivMountain.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public boolean onBackPressed() {
                        return false;
                    }
                })
                .asCustom(new GameOverPopup(this))
                .show();
    }

    private void initBar() {

        initWeight();

        chart = findViewById(R.id.chart1);

        chart.setDrawBarShadow(false);

        //数字显示位置
        chart.setDrawValueAboveBar(true);

        chart.getDescription().setEnabled(false);

        // 如果图表中显示的条目超过10个，则不会有任何值
        chart.setMaxVisibleValueCount(10);

        // 现在只能分别在x轴和y轴上进行缩放
        chart.setPinchZoom(false);
        //不允许放大
        chart.setDoubleTapToZoomEnabled(false);
        //不显示网格
        chart.setDrawGridBackground(false);

        XAxis xl = chart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(false);
        xl.setDrawGridLines(false);

        xl.setGranularity(10f);

        YAxis yl = chart.getAxisLeft();
        yl.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yl.setDrawAxisLine(false);
        yl.setDrawGridLines(false);
        yl.setAxisMinimum(0f);
        yl.setAxisMaximum(chartWeight);  //设置y轴最大值

        YAxis yr = chart.getAxisRight();
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);
        yr.setAxisMinimum(0f);

        chart.setFitBars(true);
        chart.animateY(2000);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(7f);
        l.setXEntrySpace(4f);

        initWeight();
        setData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_move:
                reduceMountain();
                break;
        }
    }

}
