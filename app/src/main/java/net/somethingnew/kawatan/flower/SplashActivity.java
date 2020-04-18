package net.somethingnew.kawatan.flower;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;

import net.somethingnew.kawatan.flower.util.LogUtility;


/**
 * スプラッシュ画面Activity.<br>
 */
public class SplashActivity extends Activity {

    GlobalManager               globalMgr;
    SharedPreferences           mSharedPref;
    private Handler             mTimeoutHandler = new Handler();
    private Runnable            mRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        GlobalManager.onCreateApplication(getApplicationContext());
        globalMgr               = GlobalManager.getInstance();

        // Preferenceから設定情報を取得しglobalMgrに保持
        mSharedPref             = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME,Context.MODE_PRIVATE);
        String userId           = mSharedPref.getString(Constants.SHARED_PREF_KEY_USERID, "");

        setContentView(R.layout.activity_splash);

        //TextView txvVersion     = findViewById(R.id.txvVerion);
        //txvVersion.setText("Version: " + Constants.VERSION);

        // タイムアウトの処理
        mRunnable                = new Runnable() {
            @Override
            public void run() {
                doPostSplash();
            }
        };

        LogUtility.d("Showing splash for a while...");
        mTimeoutHandler.postDelayed(mRunnable, Constants.SPLASH_DISPLAY_TIME);

        findViewById(R.id.buttonGoToTest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SplashActivity.this, TestActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        return;

    }

    public void onStart() {
        super.onStart();

    }

    /**
     * スプラッシュ画面表示後の処理
     */
    public void doPostSplash() {
        Intent intent = new Intent();
        intent.setClass(SplashActivity.this, MainActivity.class);
        //intent.setClass(SplashActivity.this, TestActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


}
