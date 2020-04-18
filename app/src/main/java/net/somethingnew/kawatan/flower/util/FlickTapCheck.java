package net.somethingnew.kawatan.flower.util;

import android.util.Log;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.View.OnTouchListener;

public abstract class FlickTapCheck {

    public static final int LEFT_FLICK      = 0;
    public static final int RIGHT_FLICK     = 1;
    public static final int UP_FLICK        = 2;
    public static final int DOWN_FLICK      = 3;

    private float adjustX = 150.0f;
    private float adjustY = 150.0f;
    private float touchX;
    private float touchY;
    private float nowTouchX;
    private float nowTouchY;

    /**
     * frickViewにはフリックを検知させるViewをセット<br/>
     * adjustXには左右のフリック距離目安、adjustYには上下のフリック距離目安をセット
     * @param frickView
     * @param adjustX
     * @param adjustY
     */
    public FlickTapCheck(View frickView, float adjustX, float adjustY){

        this.adjustX = adjustX;
        this.adjustY = adjustY;

        frickView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        touchX = event.getX();
                        touchY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        nowTouchX = event.getX();
                        nowTouchY = event.getY();
                        check();
                        v.performClick();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                }
                return true;
            }

        });
    }

    /**
     * どの方向にフリックしたかチェック
     */
    private void check(){
        LogUtility.d("startX:" + touchX + " endX:" + nowTouchX + " startY:" + touchY + " endY:" + nowTouchY);

        if (touchX == nowTouchX) {
            onTap();
            return;
        }

        // 左フリック
        if(touchX > nowTouchX) {
            if(touchX - nowTouchX > adjustX) {
                onFlick(LEFT_FLICK);
            }
            else {
                onTap();
            }
            return;
        }
        // 右フリック
        if(nowTouchX > touchX) {
            if(nowTouchX - touchX > adjustX) {
                onFlick(RIGHT_FLICK);
            }
            else {
                onTap();
            }
            return;
        }
        // 上フリック
        if(touchY > nowTouchY) {
            if(touchY - nowTouchY > adjustY) {
                onFlick(UP_FLICK);
            }
            else {
                onTap();
            }
            return;
        }
        // 下フリック
        if(nowTouchY > touchY) {
            if(nowTouchY - touchY > adjustY) {
                onFlick(DOWN_FLICK);
            }
            else {
                onTap();
            }
            return;
        }
    }

    /**
     * 抽象メソッド：フリックを感知した際、方向を表す値をセットする
     */
    public abstract void onFlick(int swipe);

    /**
     * 抽象メソッド：タップを感知した際
     */
    public abstract void onTap();

}