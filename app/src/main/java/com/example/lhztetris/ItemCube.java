package com.example.lhztetris;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.Random;

public class ItemCube extends RelativeLayout {

    private int number = 8;
    private int size = 6;

    private View[][] viewArray;
    private int[][] pointArray;
    private int singleWidth;
    private int singleHeight;

    public ItemCube(Context context) {
        super(context);
        init();
    }

    public ItemCube(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ItemCube(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        viewArray = new View[number][size];
        for (int i = 0; i < number; i++) {
            for (int j = 0; j < size; j++) {
                viewArray[i][j] = new View(getContext());
                LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                viewArray[i][j].setLayoutParams(params);
                addView(viewArray[i][j]);
            }
        }
        DisplayMetrics dm = getResources().getDisplayMetrics();
        singleWidth = dm.widthPixels / size;
        singleHeight = dm.heightPixels / number;

        pointArray = new int[number][size];
        postDelayed(refreshRunnable, 1000);
    }

    private void initPointArray() {
        Random random = new Random();
        for (int i = 0; i < number; i++) {
            for (int j = 0; j < size; j++) {
                pointArray[i][j] = random.nextInt(2);
            }
        }
    }

    private Runnable refreshRunnable = new Runnable() {
        @Override
        public void run() {
            initPointArray();
            for (int i = 0; i < number; i++) {
                for (int j = 0; j < size; j++) {
                    System.out.println(String.format("i:%1$d,j:%2$d,%3$d", i, j, pointArray[i][j]));
                    viewArray[i][j].setBackgroundResource(pointArray[i][j] == 0 ? android.R.color.white : android.R.color.black);
                }
            }
            postDelayed(refreshRunnable, 1000);
        }
    };

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < number; i++) {
            for (int j = 0; j < size; j++) {
                viewArray[i][j].measure(MeasureSpec.makeMeasureSpec(singleWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(singleHeight, MeasureSpec.EXACTLY));
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < number; i++) {
            for (int j = 0; j < size; j++) {
                int left = j * singleWidth;
                int top = i * singleHeight;
                viewArray[i][j].layout(left, top, left + singleWidth, top + singleHeight);
            }
        }
    }

}
