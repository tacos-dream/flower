package net.somethingnew.kawatan.flower;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import net.somethingnew.kawatan.flower.db.DatabaseHelper;
import net.somethingnew.kawatan.flower.db.dao.CardDao;
import net.somethingnew.kawatan.flower.db.dao.FolderDao;
import net.somethingnew.kawatan.flower.model.CardModel;
import net.somethingnew.kawatan.flower.model.FolderModel;
import net.somethingnew.kawatan.flower.util.LogUtility;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * スプラッシュ画面Activity.<br>
 */
public class SplashActivity extends AppCompatActivity {

    GlobalManager globalMgr;
    SharedPreferences mSharedPref;
    TextView mTextViewVersion;
    TextView mTextViewTitle;
    private Handler mTimeoutHandler = new Handler();
    private Runnable mRunnable;
    private long mStartTime;
    boolean stopTimer = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        GlobalManager.onCreateApplication(getApplicationContext());
        globalMgr = GlobalManager.getInstance();

        setContentView(R.layout.activity_splash);

        loadSharedPreference();

        mTextViewTitle = findViewById(R.id.textViewTitle);
        mTextViewVersion = findViewById(R.id.textViewTitleVersion);
        mTextViewVersion.setText(Constants.CATEGORY_NAME[globalMgr.mCategory] + " Version");

        IconManager.init(this);
        ImageView imageView = findViewById(R.id.imageViewLogo);
        imageView.setImageResource(IconManager.getResIdAtRandom(globalMgr.mCategory));
        imageView.setOnClickListener(view -> {
            stopTimer = true;
            Intent intent = new Intent();
            intent.setClass(SplashActivity.this, TestActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        /*
        LogUtility.d("Showing splash for a while...");
        mRunnable                = new Runnable() {
            @Override
            public void run() {
                doPostSplash();
            }
        };
        mTimeoutHandler.postDelayed(mRunnable, Constants.SPLASH_DISPLAY_TIME);

         */

        LogUtility.d("Loading...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                mStartTime = System.currentTimeMillis();
                // Database作成（すでに存在する場合はアクセスするためのHelperインスタンスを受け取る）
                LogUtility.d("new DatabaseHelper...");
                globalMgr.mDbHelper = new DatabaseHelper(getApplicationContext());
                globalMgr.mDbHelper.getWritableDatabase();

                loadDb();

                startRotation();

                while (true) {
                    if (stopTimer) {
                        return;
                    }
                    if (System.currentTimeMillis() - mStartTime > Constants.SPLASH_TIME_MILLI_SEC) {
                        break;
                    }
                    try {
                        LogUtility.d("Thread.sleep...");
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                }
                doPostSplash();
            }
        }).start();

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

    public void loadDb() {
        FolderDao folderDao = new FolderDao(getApplicationContext());
        ArrayList<FolderModel> folderModelArrayList = folderDao.selectAll();
        globalMgr.mFolderLinkedList = new LinkedList<>(folderModelArrayList);
        LogUtility.d("Loading FOLDER_TBL counts: " + globalMgr.mFolderLinkedList.size());

        // CardLinkedListの器をmCardListMapに追加（枠だけで中身は後で追加する）
        for (FolderModel folder : globalMgr.mFolderLinkedList) {
            globalMgr.mCardListMap.put(folder.getId(), new LinkedList<CardModel>());
        }

        // 全てのCardを読み込み、それぞれのfolderIdにより、該当のCardのLinkedListに追加していく
        CardDao cardDao = new CardDao(getApplicationContext());
        ArrayList<CardModel> cardModelArrayList = cardDao.selectAll();
        LogUtility.d("Loading CARD_TBL counts: " + cardModelArrayList.size());
        for (CardModel card : cardModelArrayList) {
            globalMgr.mCardListMap.get(card.getFolderId()).add(card);
        }
    }

    public void loadSharedPreference() {
        // Preferenceから設定情報を取得しglobalMgrに保持
        mSharedPref = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        globalMgr.mCategory = mSharedPref.getInt(Constants.SHARED_PREF_KEY_CATEGORY, Constants.CATEGORY_INDEX_JEWELRY);
        globalMgr.skinHeaderColor = mSharedPref.getInt(Constants.SHARED_PREF_KEY_SKIN_HEADER_COLOR, Color.parseColor(Constants.DEFAULT_SKIN_HEADER_COLOR));
        globalMgr.skinBodyColor = mSharedPref.getInt(Constants.SHARED_PREF_KEY_SKIN_BODY_COLOR, Color.parseColor(Constants.DEFAULT_SKIN_BODY_COLOR));
    }

    private void startRotation() {
        float toDegrees = new Random(1000).nextInt() % 2 == 0 ? 360.0f : -360.0f;
        // RotateAnimation(float fromDegrees, float toDegrees, int pivotXType, float pivotXValue, int pivotYType,float pivotYValue)
        RotateAnimation rotate = new RotateAnimation(0.0f, toDegrees,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        // animation時間 msec
        rotate.setDuration(1000);
        // 繰り返し回数
        rotate.setRepeatCount(0);
        // animationが終わったそのまま表示にする
        rotate.setFillAfter(true);

        //アニメーションの開始
        mTextViewTitle.startAnimation(rotate);
    }
}
