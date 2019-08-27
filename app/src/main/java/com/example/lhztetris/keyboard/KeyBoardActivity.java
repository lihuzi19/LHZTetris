package com.example.lhztetris.keyboard;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lhztetris.R;

public class KeyBoardActivity extends AppCompatActivity {

    private int keyboardHeight = -1;

    private TextView emotionTv;
    private RelativeLayout emotionLayout;

    private int type;

    private final int TYPE_SHOW_HIDE = 0;
    private final int TYPE_SHOW_KEYBOARD = 1;
    private final int TYPE_SHOW_EMOTION = 2;
    private AppCompatEditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_board);

        emotionTv = findViewById(R.id.act_keyboard_emotion_tv);
        emotionLayout = findViewById(R.id.emotion_layout);

        showKeyboard();

        editText = findViewById(R.id.act_keyboard_send_edit);
        editText.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                int height = getSupportSoftInputHeight(KeyBoardActivity.this);
                System.out.println(String.format("height: %1$d", height));
                if (height > 200) {
                    keyboardHeight = height;
                }
                if (height > 200) {
                    type = TYPE_SHOW_KEYBOARD;
                } else {
                    if (emotionLayout.getLayoutParams().height > 200) {
                        type = TYPE_SHOW_EMOTION;
                    } else {
                        type = TYPE_SHOW_HIDE;
                    }
                }
                System.out.println(String.format("type: %1$d", type));
            }
        });

        emotionTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == TYPE_SHOW_HIDE) {
                    if (keyboardHeight > 200) {
                        showEmotionLayout();
                    } else {
                        showKeyboard();
                        emotionTv.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showEmotionLayout();
                                hideKeyboard();
                            }
                        }, 500);
                    }
                } else if (type == TYPE_SHOW_KEYBOARD) {
                    showEmotionLayout();
                    hideKeyboard();
                } else if (type == TYPE_SHOW_EMOTION) {
                    hideEmotionLayout();
                    showKeyboard();
                }
            }
        });
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideEmotionLayout();
                return false;
            }
        });
    }

    private void showEmotionLayout() {
        emotionLayout.getLayoutParams().height = keyboardHeight;
    }

    private void hideEmotionLayout() {
        emotionLayout.getLayoutParams().height = 0;
    }

    /**
     * 获取软件盘的高度
     *
     * @return
     */
    private int getSupportSoftInputHeight(Activity mActivity) {
        Rect r = new Rect();
        /**
         * decorView是window中的最顶层view，可以从window中通过getDecorView获取到decorView。
         * 通过decorView获取到程序显示的区域，包括标题栏，但不包括状态栏。
         */
        mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
        //获取屏幕的高度
        int screenHeight = mActivity.getWindow().getDecorView().getRootView().getHeight();
        //计算软件盘的高度
        int softInputHeight = screenHeight - r.bottom;

        /**
         * 某些Android版本下，没有显示软键盘时减出来的高度总是144，而不是零，
         * 这是因为高度是包括了虚拟按键栏的(例如华为系列)，所以在API Level高于20时，
         * 我们需要减去底部虚拟按键栏的高度（如果有的话）
         */
        if (Build.VERSION.SDK_INT >= 20) {
            // When SDK Level >= 20 (Android L), the softInputHeight will contain the height of softButtonsBar (if has)
            softInputHeight = softInputHeight - getSoftButtonsBarHeight(mActivity);
        }

        if (softInputHeight < 0) {
//            KLog.logI("EmotionKeyboard", "EmotionKeyboard--Warning: value of softInputHeight is below zero!");
        }
        //存一份到本地
        if (softInputHeight > 0) {
//            sp.edit().putInt(SHARE_PREFERENCE_SOFT_INPUT_HEIGHT, softInputHeight).apply();
        }
        return softInputHeight;
    }

    /**
     * 底部虚拟按键栏的高度
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private int getSoftButtonsBarHeight(Activity mActivity) {
        DisplayMetrics metrics = new DisplayMetrics();
        //这个方法获取可能不是真实屏幕的高度
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        //获取当前屏幕的真实高度
        mActivity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }


    private void showKeyboard() {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager != null) manager.showSoftInput(editText, 0);
    }

    private void hideKeyboard() {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        ;
        if (manager != null)
            manager.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
