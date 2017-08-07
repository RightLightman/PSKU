package com.jiangtea.psku;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

public class DrawableUtil {
	//返回绘画矩形图片的方法
	public static Drawable getGradientDrawable(int rgb,int r){
		//创建绘画圆角矩形对象的操作
		GradientDrawable gradientDrawable = new GradientDrawable(); 
		//圆角矩形
		gradientDrawable.setGradientType(GradientDrawable.RECTANGLE);
		//设置绘画图片颜色
		gradientDrawable.setColor(rgb);
		//设置圆角弧度(内切圆半径r)
		gradientDrawable.setCornerRadius(r);
		return gradientDrawable;
	}
	
	//生成背景选择器(1,选中背景2,未选中背景)的方法
	public static StateListDrawable getStateListDrawable(Drawable drawablePress,Drawable drawableNormal){
		StateListDrawable stateListDrawable = new StateListDrawable();
		//添加处在当前状态下的图片
		stateListDrawable.addState(
				new int[]{android.R.attr.state_enabled,android.R.attr.state_pressed}, 
				drawablePress);
		stateListDrawable.addState(new int[]{android.R.attr.state_enabled}, drawableNormal);
		stateListDrawable.addState(new int[]{}, drawableNormal);
		return stateListDrawable;
	}
}
