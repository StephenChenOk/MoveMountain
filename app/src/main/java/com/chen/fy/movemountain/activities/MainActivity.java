package com.chen.fy.movemountain.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chen.fy.movemountain.objects.Tool;
import com.chen.fy.movemountain.popups.MoveFinishedPopup;
import com.chen.fy.movemountain.R;
import com.chen.fy.movemountain.popups.TooBigPopup;
import com.chen.fy.movemountain.popups.TooSmallPopup;
import com.chen.fy.movemountain.utils.UiUtils;
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
    private RadioGroup rgTop;
    private RadioGroup rgBottom;

    private ImageView ivMountain;

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

    //工具
    private Tool toolShou;
    private Tool toolChan;
    private Tool toolYunNiChe;
    private Tool toolWaJueJi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initBar();
        initTools();
    }

    private void initView() {

        UiUtils.changeStatusBarTextImgColor(this, true);

        rgTop = findViewById(R.id.rg_top);
        rgBottom = findViewById(R.id.rg_bottom);

        tvContent = findViewById(R.id.tv_content);
        RadioButton rbShou = findViewById(R.id.rb_shou);
        RadioButton rbChan = findViewById(R.id.rb_chan);
        RadioButton rbChe = findViewById(R.id.rb_che);
        RadioButton rbWaJueJi = findViewById(R.id.rb_wa_jue_ji);

        ivMountain = findViewById(R.id.iv_mountain);

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

    private void initTools() {
        toolShou = new Tool("手工");
        toolChan = new Tool("铲子");
        toolYunNiChe = new Tool("运泥车");
        toolWaJueJi = new Tool("挖掘机");
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
            gameOver(1);
        } else {
            setMountainLayout();
        }

        //重新绘制柱状图
        setData();
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
                ivMountain.getPivotX(), ivMountain.getHeight());
        scaleAnimation.setDuration(2000);
        scaleAnimation.setFillAfter(true);
        ivMountain.startAnimation(scaleAnimation);
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

        tvContent.setText(UiUtils.highLightText("请选择最合适的工具进行移山", "工具"));
    }

    /**
     * 游戏结束
     *
     * @param type 1：移山完成；2：工具太大了；3：工具太小气
     */
    private void gameOver(int type) {

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
                        initBar();
                        setMountainLayout();
                        ivMountain.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public boolean onBackPressed() {
                        return false;
                    }

                });
        switch (type) {      //移山完成
            case 1:
                builder.asCustom(new MoveFinishedPopup(this))
                        .show();
                break;
            case 2:         //小题大作了
                builder.asCustom(new TooBigPopup(this))
                        .show();
                break;
            case 3:        //移到猴年马月
                builder.asCustom(new TooSmallPopup(this))
                        .show();
                break;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_shou:
                rgBottom.clearCheck();
                if (toolShou.getWeight() > mRemainWeight) {
                    reduceMountain(mRemainWeight);
                } else if (toolChan.getWeight() < mRemainWeight) {
                    gameOver(3);
                } else {
                    reduceMountain(toolShou.getWeight());
                }
                tvContent.setText(UiUtils.highLightText("正在手工进行移山", "手工"));

                break;
            case R.id.rb_chan:
                rgBottom.clearCheck();
                if (toolChan.getWeight() > mRemainWeight) {
                    gameOver(2);
                } else if (toolYunNiChe.getWeight() < mRemainWeight) {
                    gameOver(3);
                } else {
                    reduceMountain(toolChan.getWeight());
                    tvContent.setText(UiUtils.highLightText("正在使用铲子进行移山", "铲子"));
                }

                break;
            case R.id.rb_che:
                rgTop.clearCheck();
                if (toolYunNiChe.getWeight() > mRemainWeight) {
                    gameOver(2);
                } else if (toolWaJueJi.getWeight() < mRemainWeight) {
                    gameOver(3);
                } else {
                    reduceMountain(toolYunNiChe.getWeight());
                    tvContent.setText(UiUtils.highLightText("正在使用运泥车进行移山", "运泥车"));
                }

                break;
            case R.id.rb_wa_jue_ji:
                rgTop.clearCheck();
                if (toolWaJueJi.getWeight() > mRemainWeight) {
                    gameOver(2);
                } else {
                    reduceMountain(toolWaJueJi.getWeight());
                    tvContent.setText(UiUtils.highLightText("正在使用挖掘机进行移山", "挖掘机"));
                }

                break;
        }
    }

}
