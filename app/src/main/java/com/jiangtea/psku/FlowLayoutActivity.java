package com.jiangtea.psku;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by pc-gaox on 2016/4/20.
 */
public class FlowLayoutActivity extends Activity {


    private List<String> data = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataCenter.addData(data);

        ScrollView scrollView = new ScrollView(this);
        //在scrollView内部去添加自定义控件
//        int padding = UIUtils.dip2px(10);
        scrollView.setPadding(10, 10, 10, 10);


//		//测量onMeasure 布局onlayout  绘画onDraw
        MyFlowLayout flowLayout = new MyFlowLayout(this);
//
//		//设置每一个行间textView水平方向间距
//		int horizontalSpacing = UIUtils.dip2px(6);
//		flowLayout.setHorizontalSpacing(horizontalSpacing);
//`
//		//设置行于行竖直方向间距
//		int verticalSpacing = UIUtils.dip2px(10);
//		flowLayout.setVerticalSpacing(verticalSpacing);

//        MyFlowLayout flowLayout = new MyFlowLayout(this);

        //在FlowLayout添加TextView过程中,texView的个数,就是data.size()
        for (int i = 0; i < data.size(); i++) {
            final TextView textView = new TextView(this);
            textView.setTextColor(Color.WHITE);
            //文字居中
            textView.setGravity(Gravity.CENTER);
            //给控件设置文字
            textView.setText(data.get(i));

            int red = 30 + new Random().nextInt(210);
            int green = 30 + new Random().nextInt(210);
            int blue = 30 + new Random().nextInt(210);

            //此颜色用于绘画textView的背景
            int rgb = Color.rgb(red, green, blue);
            Drawable drawableNormal = DrawableUtil.getGradientDrawable(rgb, 6);

            //创建选中的背景图片
            //偏白色
            int pressRgb = 0xffcecece;
            Drawable drawablePress = DrawableUtil.getGradientDrawable(pressRgb, 6);

            StateListDrawable stateListDrawable = DrawableUtil.getStateListDrawable(drawablePress, drawableNormal);

            //设置包含了背景选择器的背景图
            textView.setBackgroundDrawable(stateListDrawable);

            textView.setPadding(5, 5, 5, 5);

            //给textView设置点击事件,让其可以被点击
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(FlowLayoutActivity.this, textView.getText().toString(), Toast.LENGTH_LONG).show();
                }
            });

            flowLayout.addView(textView);
        }
        scrollView.addView(flowLayout);
        setContentView(scrollView);
    }
}
