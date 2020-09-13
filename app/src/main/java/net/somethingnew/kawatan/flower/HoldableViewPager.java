package net.somethingnew.kawatan.flower;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.viewpager.widget.ViewPager;

/**
 * スワイプを禁止可能にするViewPager
 * https://grandbig.github.io/blog/2016/01/30/android-tablayout/
 */
public class HoldableViewPager extends ViewPager {

    // スワイプの禁止フラグ(true: スワイプ禁止, false: スワイプOK)
    boolean isSwipeHold = false;

    public void setSwipeHold(boolean enabled) {
        isSwipeHold = enabled;
    }

    // コンストラクタ
    public HoldableViewPager(Context context) {
        super(context);
    }

    // コンストラクタ
    public HoldableViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (isSwipeHold) {
            // スワイプ禁止の場合
            return false;
        }
        return super.onTouchEvent(motionEvent);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (isSwipeHold) {
            // スワイプ禁止の場合
            return false;
        }
        return super.onInterceptTouchEvent(event);
    }
}