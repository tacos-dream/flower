package net.somethingnew.kawatan.flower;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import net.somethingnew.kawatan.flower.db.DatabaseHelper;
import net.somethingnew.kawatan.flower.db.dao.CardDao;
import net.somethingnew.kawatan.flower.db.dao.FolderDao;
import net.somethingnew.kawatan.flower.model.AvailableBookInfo;
import net.somethingnew.kawatan.flower.model.CardModel;
import net.somethingnew.kawatan.flower.model.FolderModel;
import net.somethingnew.kawatan.flower.util.LogUtility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

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
    ObjectMapper mObjectMapper = new ObjectMapper();

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

        LogUtility.d("Loading...");
        new Thread(() -> {
            mStartTime = System.currentTimeMillis();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (stopTimer) {
                return;
            }

            // Database作成（すでに存在する場合はアクセスするためのHelperインスタンスを受け取る）
            LogUtility.d("new DatabaseHelper...");
            globalMgr.mDbHelper = new DatabaseHelper(getApplicationContext());
            globalMgr.mDbHelper.getWritableDatabase();

            try {
                loadDb();
            }
            catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            downloadAvailableBookList();

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
        intent.setClass(SplashActivity.this, FolderListActivity.class);
        //intent.setClass(SplashActivity.this, TestActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void loadDb() throws JsonProcessingException {
        FolderDao folderDao = new FolderDao(getApplicationContext());
        ArrayList<FolderModel> folderModelArrayList = folderDao.selectAll();
        globalMgr.mFolderLinkedList = new LinkedList<>(folderModelArrayList);
        LogUtility.d("Loading FOLDER_TBL counts: " + globalMgr.mFolderLinkedList.size());

        // CardLinkedListの器をmCardListMapに追加（枠だけで中身は後で追加する）
        for (FolderModel folder : globalMgr.mFolderLinkedList) {
            LogUtility.d("FolderModel: " + mObjectMapper.writeValueAsString(folder));
            globalMgr.mCardListMap.put(folder.getId(), new LinkedList<CardModel>());
        }

        // 全てのCardを読み込み、それぞれのfolderIdにより、該当のCardのLinkedListに追加していく
        CardDao cardDao = new CardDao(getApplicationContext());
        ArrayList<CardModel> cardModelArrayList = cardDao.selectAll();
        LogUtility.d("Loading CARD_TBL counts: " + cardModelArrayList.size());
        for (CardModel card : cardModelArrayList) {
            LogUtility.d("CardModel: " + mObjectMapper.writeValueAsString(card));
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

    // AsyncTaskがdeprecatedなので、とりあえずバックグラウンドは無しでやってみる
    public void downloadAvailableBookList() {
        final int CONNECTION_TIMEOUT = 10 * 1000;
        final int READ_TIMEOUT = 10 * 1000;

        HttpURLConnection conn = null;
        try {
            URL url = new URL(Constants.AVAILABLE_BOOK_LIST_URI);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);

            conn.setRequestMethod("GET");
            conn.connect();
            int statusCode = conn.getResponseCode();

            StringBuilder result = new StringBuilder();
            if(statusCode == HttpURLConnection.HTTP_OK){
                //responseの読み込み
                final InputStream in = conn.getInputStream();
//            final String encoding = conn.getContentEncoding();
                final InputStreamReader inReader = new InputStreamReader(in, "UTF-8");
                final BufferedReader bufferedReader = new BufferedReader(inReader);
                String line = null;
                // データは改行の無い１行データなので、whileループは１回しか回らない
                while((line = bufferedReader.readLine()) != null) {
//                LogUtility.d(line);
                    result.append(line);
                }
                bufferedReader.close();
                inReader.close();
                in.close();
            }

            String json = result.toString();
            JSONArray jsonArray = new JSONArray(json);

            // パースした内容からListオブジェクトを作成
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                AvailableBookInfo availableBookInfo = new AvailableBookInfo();
                availableBookInfo.setCourseId((String) jsonObject.get("courseId"));
                availableBookInfo.setCourseName((String) jsonObject.get("courseName"));
                availableBookInfo.setPart((String) jsonObject.get("part"));
                availableBookInfo.setTitle((String) jsonObject.get("title"));
                availableBookInfo.setFileName((String) jsonObject.get("fileName"));
                availableBookInfo.setUrl((String) jsonObject.get("url"));
                globalMgr.availableBookInfoList.add(availableBookInfo);
            }
        } catch (Exception e) {
            LogUtility.d(e.getMessage());
        } finally {
            conn.disconnect();
        }

//        for(int i = 0; i < globalMgr.availableBookInfoList.size(); i++) {
//            LogUtility.d("title:" + globalMgr.availableBookInfoList.get(i).getTitle() + ", url:" + globalMgr.availableBookInfoList.get(i).getUrl());
//        }

        return;
    }

    /**
     * タイトルを回転させるアニメーション表示
     * この関数はタイマースレッドの中から呼ばれているので、メインスレッドでは無い。
     * よって、直接Viewを操作しようとすると「Only the original thread that created a view hierarchy can touch its views」のエラーになるので
     * メインスレッドのLooperに処理をPostして、メインスレッドで処理が行われるようにする
     */
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
//        mTextViewTitle.startAnimation(rotate);
        final Handler mainHandler = new Handler(Looper.getMainLooper());
        try {
            /* 処理 */
            mainHandler.post(() -> {
                mTextViewTitle.startAnimation(rotate);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
