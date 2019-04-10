package com.example.test.shundemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xushun on 2018/10/9 21:30.
 * 自定义流程节点图--
 */

public class ProcessView extends View {
    //点击事件的时间差
    private static final int TOUCH_TIME = 1000;
    //上一次点击的时间
    private long preTime = 0L;
    //当前点击的时间
    private long currTime = 0L;

    //垂直方向item的个数
    private int verticalNum = 0;

    //水平方向item的个数 (与垂直方向一一对应)
    private String[] horizontalNum = null;

    //item的宽度
    private int itemWidth = 0;

    //item的高度
    private int itemHeight = 0;

    //divider的高度
    private int verticalSpace = 0;

    //item的颜色
    private int itemColor = Color.BLACK;

    //item的字体大小
    private float itemTextSize = 0f;

    //item水平间距
    private int horizontalSpace = 30;

    private float textHeight = 0f;

    private Paint paint = new Paint();

    private Rect lastItemRect;

    //用来保存每隔item的Rect，用于后面的点击事件判断
    private HashMap<Integer, List<Rect>> rects = new HashMap<>();

    private HashMap<Integer, List<String>> texts = getTextShow();

    private OnItemClickListener listener = null;

    public ProcessView(Context context) {
        this(context, null);
    }

    public ProcessView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ProcessView);
            itemColor = typedArray.getColor(R.styleable.ProcessView_itemColor, Color.BLACK);
            verticalNum = typedArray.getInt(R.styleable.ProcessView_verticalNum, 0);
            itemTextSize = typedArray.getDimension(R.styleable.ProcessView_itemTextSize, 16f);
            itemHeight = (int) typedArray.getDimension(R.styleable.ProcessView_itemHeight, 30f);
            itemWidth = (int) typedArray.getDimension(R.styleable.ProcessView_itemWidth, 150f);
            verticalSpace = (int) typedArray.getDimension(R.styleable.ProcessView_itemVerticalSpace, 30f);
            horizontalSpace = (int) typedArray.getDimension(R.styleable.ProcessView_itemHorizontalSpace, 30f);
            horizontalNum = typedArray.getString(R.styleable.ProcessView_horizontalNum).split(",");
            typedArray.recycle();
        }
        paint.setAntiAlias(true);
        paint.setColor(itemColor);
        paint.setTextSize(itemTextSize);
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextAlign(Paint.Align.CENTER);

        textHeight = paint.getFontMetrics().descent - paint.getFontMetrics().ascent + paint.getFontMetrics().leading;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(View.MeasureSpec.getSize(widthMeasureSpec), measureSize(1));
    }

    /**
     * 测量 子View宽高
     */
    private int measureSize(int i) {
        int newSize = 0;
        if (i == 0) {
            //newSize = ScreenUtils_Java.getScreenWidth(getContext());
        } else {
            newSize = verticalNum * (itemHeight + verticalSpace);
        }
        return newSize;
    }

    @Override
    public void onDraw(Canvas canvas) {
        for (int vIndex = 0; vIndex < verticalNum; vIndex++) {
            List<Rect> list = new ArrayList<>();
            int hNum = Integer.parseInt(horizontalNum[vIndex]);
            // item实际宽度
            int tempWidth = (getWidth() - horizontalSpace * (hNum - 1)) / hNum;

            for (int hIndex = 0; hIndex < hNum; hIndex++) {
                Rect rectBorder = new Rect();
                String str = texts.get(vIndex).get(hIndex);
                rectBorder.top = (vIndex) * itemHeight + (vIndex) * verticalSpace;
                rectBorder.bottom = rectBorder.top + itemHeight;
                if (tempWidth >= itemWidth) {
                    // 距离两边的距离
                    int margin = (getWidth() - (itemWidth * hNum + (hNum - 1) * horizontalSpace)) / 2;
                    rectBorder.left = margin + hIndex * itemWidth + hIndex * horizontalSpace;
                    rectBorder.right = itemWidth + rectBorder.left;
                } else {
                    rectBorder.left = hIndex * tempWidth + hIndex * horizontalSpace;
                    rectBorder.right = tempWidth + rectBorder.left;
                }
                drawContent(canvas, rectBorder, str);
                drawLinkLine(canvas, rectBorder, hNum, vIndex);
                list.add(rectBorder);
            }
            rects.put(vIndex, list);
        }
    }

    /**
     * 画文字和背景
     */
    private void drawContent(Canvas canvas, Rect rect, String str) {
        canvas.drawRect(rect, paint);
        canvas.drawText(str
                , (rect.right + rect.left) / 2f
                , (rect.bottom + rect.top) / 2f + textHeight / 2 - paint.getFontMetrics().descent
                , paint);
    }

    /***
     * 画连接线
     * @param rect     当前Item的Rect
     * @param currHNum 当前的水平方向 有多少个Item
     * @param vIndex 当前是垂直方向的第几个
     */
    private void drawLinkLine(Canvas canvas, Rect rect, int currHNum, int vIndex) {
        int stopX = getX(rect);
        int startX = getX(rect);

        if (vIndex < verticalNum - 1) {
            //画Item下面一半的垂直连接线
            canvas.drawLine(startX, rect.bottom, stopX, (rect.bottom + verticalSpace / 2f), paint);

            //画水平连接线
            if (currHNum > 1 && lastItemRect != null && lastItemRect.top == rect.top) {
                canvas.drawLine(getX(lastItemRect), (rect.bottom + verticalSpace / 2f), getX(rect), (rect.bottom + verticalSpace / 2f), paint);
            }
        }

        if (vIndex != 0) {
            //画Item上面一半的垂直连接线
            canvas.drawLine(startX, (rect.top - verticalSpace / 2f), stopX, rect.top, paint);

            //画水平连接线
            if (currHNum > 1 && lastItemRect != null && lastItemRect.top == rect.top) {
                canvas.drawLine(getX(lastItemRect), (rect.top - verticalSpace / 2f), getX(rect), (rect.top - verticalSpace / 2f), paint);
            }
        }
        lastItemRect = rect;
    }

    private int getX(Rect rect) {
        return (int) ((rect.left + rect.right) / 2f);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                preTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_UP:
                currTime = System.currentTimeMillis();
                Result result = isTouchPointInView(event);
                if (currTime - preTime < TOUCH_TIME && result.isTouchInView) {
                    listener.onItemClick(result.vPosition, result.hPosition, texts.get(result.vPosition).get(result.hPosition));
                }
                break;
        }
        return true;
    }

    /**
     * 判断点击事件是否在某个上面
     */
    private Result isTouchPointInView(MotionEvent event) {
        if (rects.size() == 0) return new Result(false, 0, 0);

        int eventX = (int) event.getRawX();
        int eventY = (int) event.getY();

        for (int vIndex = 0; vIndex < rects.size(); vIndex++) {
            List<Rect> list = rects.get(vIndex);
            for (int hIndex = 0; hIndex < list.size(); hIndex++) {
                Rect rect = list.get(hIndex);
                if (rect.contains(eventX, eventY)) {
                    return new Result(true, vIndex, hIndex);
                }
            }
        }
        return new Result(false, 0, 0);
    }

    public HashMap<Integer, List<String>> getTextShow() {
        List<String> text1 = Arrays.asList("执行通知");
        List<String> text2 = Arrays.asList("送达文书");
        List<String> text3 = Arrays.asList("强行措施", "财产调查", "解除措施");
        List<String> text4 = Arrays.asList("查询存款", "搜查", "传唤", "悬赏执行");
        List<String> text5 = Arrays.asList("查明财产");
        List<String> text6 = Arrays.asList("查封", "扣押", "冻结", "扣划", "评估", "拍卖");
        List<String> text7 = Arrays.asList("执行和解", "终本约谈", "自动履行");
        HashMap<Integer, List<String>> map = new HashMap<Integer, List<String>>();
        map.put(0, text1);
        map.put(1, text2);
        map.put(2, text3);
        map.put(3, text4);
        map.put(4, text5);
        map.put(5, text6);
        map.put(6, text7);
        return map;
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int vPosition, int hPosition, String text);
    }

    class Result {
        boolean isTouchInView;
        int vPosition;
        int hPosition;

        Result(boolean isTouchInView, int vPosition, int hPosition) {
            this.isTouchInView = isTouchInView;
            this.vPosition = vPosition;
            this.hPosition = hPosition;
        }
    }
}
