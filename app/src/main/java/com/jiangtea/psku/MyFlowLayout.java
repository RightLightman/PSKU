package com.jiangtea.psku;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaox on 2016/6/13.
 */
public class MyFlowLayout extends ViewGroup {
    /**
     * 行对象
     */
    private Line mLine;
    /**
     * 已使用的宽度
     */
    private int usedWidth;
    /**
     * 水平间距
     */
    private int mHorizontalSpacing = 6;
    /**
     * 垂直间距
     */
    private int mVerticalSpaceing = 6;
    /**
     * 记录行的集合
     */
    private List<Line> lineList = new ArrayList<Line>();


    public MyFlowLayout(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //获取当前flowLayout的测量模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //获取当前flowLayout的测量尺寸
        int widthSize = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int heightSize = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
        restore();
        //得到当前flowLayout的所有子view的个数
        int childCount = getChildCount();
        //循环得到每一个子view
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() == View.GONE) {
                continue;
            }
            //规范子View的大小确保其不超过父view的大小
            //1.已知流式布局宽度的测量模式是精确地
            //2.高度的测量模式是未定义的
            int childWidthSpec = MeasureSpec.makeMeasureSpec(widthSize, widthMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : widthMode);
            int childHeightSpec = MeasureSpec.makeMeasureSpec(heightSize, heightMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST : heightMode);
            childView.measure(childWidthSpec, childHeightSpec);
            //创建一个行对象
            if (mLine == null) {
                mLine = new Line();
            }
            //获取子view的宽度
            int childWidth = childView.getMeasuredWidth();

            usedWidth += childWidth;
            if (usedWidth <= widthSize) {//== 对应的是换行的第三种情况
                //不换行
                mLine.addView(childView);
                usedWidth += mHorizontalSpacing;
                if (usedWidth >= widthSize) {
                    //换行 对应的是换行逻辑的第二种情况
                    if (!newLine()) {
                        break;
                    }
                }

            } else {
                //换行 对应的是换行逻辑的第一种情况
                if (!newLine()) {
                    break;
                }
                mLine.addView(childView);
                usedWidth += childWidth + mHorizontalSpacing;
            }


        }
        //将最后一行添加进行集合中
        if (mLine != null && mLine.getViewCount() > 0 && !lineList.contains(mLine)) {
            lineList.add(mLine);
        }
        //flowLayout的宽
        int flowLayoutWidth = MeasureSpec.getSize(widthMeasureSpec);
        //flowLayout的高
        //行高
        int totalLineHeight = 0;
        for (int i = 0; i < lineList.size(); i++) {
            totalLineHeight += lineList.get(i).lineHeight;
        }
        int flowLayoutHeight = totalLineHeight + (lineList.size() - 1) * mVerticalSpaceing + getPaddingTop() + getPaddingBottom();

//        widthMeasureSpec=MeasureSpec.makeMeasureSpec(flowLayoutWidth,MeasureSpec.EXACTLY);
//        heightMeasureSpec= MeasureSpec.makeMeasureSpec(flowLayoutHeight,MeasureSpec.EXACTLY);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(flowLayoutWidth, flowLayoutHeight);


    }

    /**
     * 清空数据
     */
    private void restore() {
        lineList.clear();
        mLine = new Line();
        usedWidth=0;
    }

    /**
     * 换行
     *
     * @return
     */
    private boolean newLine() {
        lineList.add(mLine);
        if (lineList.size() < 10) {
            mLine = new Line();
            usedWidth = 0;
            return true;
        }
        return false;
    }

    /**
     * 布局行对象
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = getPaddingLeft();
        int top = getPaddingTop();

        for (int i = 0; i < lineList.size(); i++) {
            Line line = lineList.get(i);
            line.layout(left, top);
            top += line.lineHeight + mVerticalSpaceing;
        }
    }

    /**
     * 行对象
     */
    public class Line {
        /**
         * 记录当前行所有子view的集合
         */
        private List<View> viewList = new ArrayList<View>();
        /**
         * 行高
         */
        private int lineHeight;
        /**
         * 当前行所有控件的宽度的和
         */
        private int totalViewWidth;

        /**
         * 往行对象中添加子view
         *
         * @param view
         */
        private void addView(View view) {
            viewList.add(view);
            int viewHeight = view.getMeasuredHeight();
            lineHeight = Math.max(lineHeight, viewHeight);

            int viewWidth = view.getMeasuredWidth();
            totalViewWidth += viewWidth;
        }

        /**
         * 获取当前行所有子view的个数
         *
         * @return
         */
        private int getViewCount() {
            return viewList.size();
        }

        /**
         * 布局行里面所有子view的位置
         *
         * @param left
         * @param top
         */
        public void layout(int left, int top) {
            //layout宽度
            int layoutWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
            //水平留白区域的宽度
            int surplusWidth = layoutWidth - totalViewWidth - (getViewCount() - 1) * mHorizontalSpacing;
            //分配给当前行每一个控件的宽度
            int oneSurplusWidth = surplusWidth / getViewCount();

            for (int i = 0; i < viewList.size(); i++) {
                View view = viewList.get(i);
                int viewWidth = view.getMeasuredWidth() + oneSurplusWidth;
                int viewHeight = view.getMeasuredHeight();
                //重新构建view
                int viewWidthSpec = MeasureSpec.makeMeasureSpec(viewWidth, MeasureSpec.EXACTLY);
                int viewHeightSpec = MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY);
                view.measure(viewWidthSpec, viewHeightSpec);
                //获取子view居中的top
                int childTop = (lineHeight-viewHeight)/2;
                view.layout(left,childTop+top,left+view.getMeasuredWidth(),childTop+top+viewHeight);
                left+=view.getMeasuredWidth()+mHorizontalSpacing;
            }
        }
    }
}
