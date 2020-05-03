package net.somethingnew.kawatan.flower;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;

import net.somethingnew.kawatan.flower.db.DatabaseHelper;
import net.somethingnew.kawatan.flower.db.dao.CardDao;
import net.somethingnew.kawatan.flower.db.dao.FolderDao;
import net.somethingnew.kawatan.flower.model.CardModel;
import net.somethingnew.kawatan.flower.model.FolderModel;
import net.somethingnew.kawatan.flower.util.LogUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * スプラッシュ画面Activity.<br>
 */
public class SplashActivity extends Activity {

    GlobalManager               globalMgr;
    SharedPreferences           mSharedPref;
    private Handler             mTimeoutHandler = new Handler();
    private Runnable            mRunnable;
    private long                mStartTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        GlobalManager.onCreateApplication(getApplicationContext());
        globalMgr               = GlobalManager.getInstance();

        setContentView(R.layout.activity_splash);

        //TextView txvVersion     = findViewById(R.id.txvVerion);
        //txvVersion.setText("Version: " + Constants.VERSION);

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
                mStartTime          = System.currentTimeMillis();
                // Database作成（すでに存在する場合はアクセスするためのHelperインスタンスを受け取る）
                LogUtility.d("new DatabaseHelper...");
                globalMgr.mDbHelper = new DatabaseHelper(getApplicationContext());
                globalMgr.mDbHelper.getWritableDatabase();

                loadDb();
                loadSharedPreference();
                loadIcons();

                while (true) {
                    if (System.currentTimeMillis() - mStartTime > Constants.SPLASH_TIME_MILLI_SEC) {
                        break;
                    }
                    try {
                        LogUtility.d("Thread.sleep...");
                        Thread.sleep(100);
                    } catch(InterruptedException e){
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
        for (FolderModel folder: globalMgr.mFolderLinkedList) {
            globalMgr.mCardListMap.put(folder.getId(), new LinkedList<CardModel>());
        }

        // 全てのCardを読み込み、それぞれのfolderIdにより、該当のCardのLinkedListに追加していく
        CardDao cardDao = new CardDao(getApplicationContext());
        ArrayList<CardModel> cardModelArrayList = cardDao.selectAll();
        LogUtility.d("Loading CARD_TBL counts: " + cardModelArrayList.size());
        for (CardModel card: cardModelArrayList) {
            globalMgr.mCardListMap.get(card.getFolderId()).add(card);
        }
    }

    public void loadSharedPreference() {
        // Preferenceから設定情報を取得しglobalMgrに保持
        mSharedPref                                 = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME,Context.MODE_PRIVATE);
        //globalMgr.strParam                        = mSharedPref.getString(Constants.SHARED_PREF_KEY_USERID, "");
        //globalMgr.intParam                        = sharedPref.getInt(Constants.SHARED_PREF_KEY_GENRE,  Constants.GAE_API_GENRE_NUM_UNKNOWN);
        globalMgr.mUserSettings.aaa                 = mSharedPref.getBoolean(Constants.SHARED_PREF_KEY_ICON_RANDOM, false);
    }

    /**
     * カテゴリー毎のすべてのアイコンを読み込みArrayListの配列に保持する
     */
    public void loadIcons() {
        LogUtility.d("Loading Icons...");
        globalMgr.mIconResourceIdListArray                   = new ArrayList[Constants.NUM_OF_ICON_TAB];
        for (int category = 0; category < Constants.NUM_OF_ICON_TAB; category++) {
            globalMgr.mIconResourceIdListArray[category]     = new ArrayList<>();

            // Resource名をR.drawable.名前としてintに変換してarrayに登録
            // 先頭にはランダムアイコンをセットする
            int imageId         = getResources().getIdentifier(Constants.DEFAULT_ICON_NAME,"drawable", this.getPackageName());
            globalMgr.mIconResourceIdListArray[category].add(imageId);
            for (int i = 0; i < Constants.NUM_OF_ICONS_IN_CATEGORY[category]; i++) {
                String iconName     = Constants.ICON_TAB_ARRAY[category] + "_" +  String.format("%03d", i+1);
                imageId             = getResources().getIdentifier(iconName,"drawable", this.getPackageName());
                globalMgr.mIconResourceIdListArray[category].add(imageId);
            }
        }

    }

}
