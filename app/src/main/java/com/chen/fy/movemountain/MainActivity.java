package com.chen.fy.movemountain;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

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
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvContent;
    private RadioButton rbShou;
    private RadioButton rbChan;
    private RadioButton rbChe;
    private RadioButton rbWaJueJi;

    private ImageView ivMountain;
    private Button btnMove;

    private HorizontalBarChart chart;

    //山的总权值
    private int mTotalWeight;
    //山剩余的权值
    private int mRemainWeight;

    //y轴最大值
    private float chartWeight;

    //移山次数
    int moveTimes;

    //山大小的缩放大小起始设置
    private float fromX;
    private float fromY;
    private float toX;
    private float toY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initBar();
    }

    private void initView() {

        UiUtils.changeStatusBarTextImgColor(this, true);

        tvContent = findViewById(R.id.tv_content);
        rbShou = findViewById(R.id.rb_shou);
        rbChan = findViewById(R.id.rb_chan);
        rbChe = findViewById(R.id.rb_che);
        rbWaJueJi = findViewById(R.id.rb_wa_jue_ji);

        ivMountain = findViewById(R.id.iv_mountain);
        btnMove = findViewById(R.id.btn_move);

        btnMove.setOnClickListener(this);

        rbShou.setOnClickListener(this);
        rbChan.setOnClickListener(this);
        rbChe.setOnClickListener(this);
        rbWaJueJi.setOnClickListener(this);

    }

    private void initWeight() {

        Random random = new Random(System.currentTimeMillis());
        mTotalWeight = random.nextInt(900);

        mRemainWeight = mTotalWeight + 50;

        chartWeight = mRemainWeight + 100;

        moveTimes = 0;

        fromX = 1.0f;
        fromY = 1.0f;
        toX = (float) (fromX - 0.2);
        toY = (float) (fromY - 0.2);
    }

    /**
     * 减少山的大小
     */
    private void reduceMountain(double reduceWeight) {

        moveTimes++;

        //剩余的权值
        mRemainWeight = (int) (mRemainWeight - reduceWeight);

        //设置山体布局
        if (mRemainWeight == 0) {
            ivMountain.clearAnimation();
            ivMountain.setVisibility(View.GONE);
            gameOver(true);
        } else {
            setMountainLayout();
        }

        //重新绘制柱状图
        setData();
    }

    private void selectTool(int i) {
        switch (i) {
            case 0:
            case 1:
            case 2:     //8
                rbShou.setChecked(true);
                tvContent.setText(UiUtils.highLightText("正在手工进行移山", "手工"));
                break;
            case 3:
            case 4:     //16
                rbChan.setChecked(true);
                tvContent.setText(UiUtils.highLightText("正在使用铲子进行移山", "铲子"));
                break;
            case 5:
            case 6:     //64
                rbChe.setChecked(true);
                tvContent.setText(UiUtils.highLightText("正在使用运泥车进行移山", "运泥车"));
                break;
            default:
                rbWaJueJi.setChecked(true);
                tvContent.setText(UiUtils.highLightText("正在使用挖掘机进行移山", "挖掘机"));
        }
    }

    private void setMountainLayout() {

        switch (moveTimes) {
            case 0:
                startAnimation(0, 1, 0, 1);
                break;
            case 1:
                startAnimation(fromX, toX, fromY, toY);
                fromX = toX;
                toX = (float) (fromX - 0.2);
                fromY = toY;
                toY = (float) (fromY - 0.2);
                break;
            case 2:
                startAnimation(fromX, toX, fromY, toY);
                fromX = toX;
                toX = (float) (fromX - 0.15);
                fromY = toY;
                toY = (float) (fromY - 0.15);

                break;
            case 3:
                startAnimation(fromX, toX, fromY, toY);
                fromX = toX;
                toX = (float) (fromX - 0.12);
                fromY = toY;
                toY = (float) (fromY - 0.12);

                break;
            case 4:
                startAnimation(fromX, toX, fromY, toY);
                fromX = toX;
                toX = (float) (fromX - 0.10);
                fromY = toY;
                toY = (float) (fromY - 0.10);

                break;
            case 5:
                startAnimation(fromX, toX, fromY, toY);
                fromX = toX;
                toX = (float) (fromX - 0.08);
                fromY = toY;
                toY = (float) (fromY - 0.08);

                break;
            case 6:
            case 7:
                startAnimation(fromX, toX, fromY, toY);
                fromX = toX;
                toX = (float) (fromX - 0.05);
                fromY = toY;
                toY = (float) (fromY - 0.05);

                break;
            case 8:
            case 9:
            default:
                startAnimation(fromX, toX, fromY, toY);
                fromX = toX;
                toX = (float) (fromX - 0.02);
                fromY = toY;
                toY = (float) (fromY - 0.02);
        }

    }

    //设置动画效果
    private void startAnimation(float fromX, float toX, float fromY, float toY) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(fromX, toX, fromY, toY,
                ivMountain.getPivotX(), ivMountain.getPivotY());
        scaleAnimation.setDuration(2000);
        scaleAnimation.setFillAfter(true);
        ivMountain.startAnimation(scaleAnimation);

//        AnimationDrawable animationDrawable = (AnimationDrawable) getResources().getDrawable(R.drawable.man_play);
//        //ivMan.setImageDrawable(animationDrawable);
//        animationDrawable.start();
//        animationDrawable.setOneShot(true);  //播放一次
    }

    private void setData() {

        //柱子大小
        float barWidth = 2.7f;
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
    private int getIndex(int remain) {
        int i = -1;
        while (true) {
            i++;
            int f = (int) Math.pow(2, i);
            if (f > remain) {
                i--;
                break;
            }
        }
        return i;
    }

    //移山结束，点击可以重新开始
    private void gameOver(boolean isFinish) {

        XPopup.Builder builder = new XPopup.Builder(this)
                .isCenterHorizontal(true)
                .offsetY(800)
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
                        setMountainLayout();
                        setData();
                        ivMountain.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public boolean onBackPressed() {
                        return false;
                    }

                });
        if (isFinish) {
            builder.asCustom(new MoveFinishedPopup(this))
                    .show();
        }else{
            builder.asCustom(new GameOverPopup(this))
                    .show();
        }
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
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(false);
        yl.setAxisMinimum(0f);
        yl.setAxisMaximum(chartWeight);  //设置y轴最大值

        YAxis yr = chart.getAxisRight();
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(true);
        yr.setAxisMinimum(0f);
        yr.setAxisMaximum(chartWeight);  //设置y轴最大值

        chart.setFitBars(true);
        chart.animateY(1000);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setXEntrySpace(4f);

        setData();
    }

    //工具最多可以挖的权值
    private int shouIndex = 2;      //4
    private int chanIndex = 4;      //16
    private int cheIndex = 6;       //64
    private int waJueJiIndex = 8;        //256

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_move:
                //reduceMountain();
                break;
            case R.id.rb_shou:
                if (shouIndex > getIndex(mRemainWeight)) {
                    reduceMountain(mRemainWeight);
                } else {
                    reduceMountain(Math.pow(2, shouIndex));
                }
                tvContent.setText(UiUtils.highLightText("正在手工进行移山", "手工"));

                break;
            case R.id.rb_chan:
                if (chanIndex > getIndex(mRemainWeight)) {
                    gameOver(false);
                } else {
                    reduceMountain(Math.pow(2, chanIndex));
                    tvContent.setText(UiUtils.highLightText("正在使用铲子进行移山", "铲子"));
                }

                break;
            case R.id.rb_che:
                if (cheIndex > getIndex(mRemainWeight)) {
                    gameOver(false);
                } else {
                    reduceMountain(Math.pow(2, cheIndex));
                    tvContent.setText(UiUtils.highLightText("正在使用运泥车进行移山", "运泥车"));
                }

                break;
            case R.id.rb_wa_jue_ji:
                if (waJueJiIndex > getIndex(mRemainWeight)) {
                    gameOver(false);
                } else {
                    reduceMountain(Math.pow(2, waJueJiIndex));
                    tvContent.setText(UiUtils.highLightText("正在使用挖掘机进行移山", "挖掘机"));
                }

                break;
        }
    }

}
