package net.somethingnew.kawatan.flower;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import net.somethingnew.kawatan.flower.model.CardModel;
import net.somethingnew.kawatan.flower.model.FolderModel;
import net.somethingnew.kawatan.flower.util.LogUtility;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Random;

public class ExerciseActivity extends AppCompatActivity
        implements TextToSpeech.OnInitListener {

    private GlobalManager globalMgr = GlobalManager.getInstance();
    private Context mContext;
    private Activity mActivity;
    private GestureDetector mGestureDetector;
    private FolderModel mFolder;
    private CardModel mCard;
    private LinkedList<CardModel> mCardModelLinkedList;
    private LinkedList<CardModel> mCardModelLinkedListShuffle;
    private Boolean mExerciseHasStarted = false;
    private Boolean mSecondCardTextWasDisplayed = false;
    private int mTotalCardNum;
    private int mCurrentCardIndex;
    private int mExerciseMode;
    private int mCardNumToExercise;
    private int mExercisedCardNum = -1;
    private int mNotLearnedCardNum = 0;
    private int mFusenCardNum = 0;
    private int mFilter = Constants.EXERCISE_FILTER_ALL_CARDS;
    private int mFrontBackOrder = Constants.EXERCISE_FRONT_TO_BACK;

    private CardView mCardViewFront;
    private CardView mCardViewBack;
    private TextView mTextViewFront;
    private TextView mTextViewBack;
    private TextView mTextViewProgress;
    private ImageView mImageViewIconFront;
    private ImageView mImageViewIconBack;
    private ImageView mImageViewLearned;
    private ImageView mImageViewFusen;
    private ImageView mImageViewExerciseDirection;
    private Button mButtonFilterAll;
    private Button mButtonFilterNotLearnedOnly;
    private Button mButtonFilterFusenOnly;
    private TextToSpeech mTextToSpeech;

    private InterstitialAd mInterstitialAd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        mContext = getApplicationContext();
        mActivity = ExerciseActivity.this;

        // キーを元にデータを取得する(データがない場合、第２引数の 0 が返る)
        mExerciseMode = getIntent().getIntExtra(Constants.EXERCISE_MODE_KEY_NAME, Constants.EXERCISE_MODE_NORMAL);
        mFolder = globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex);
        mCardModelLinkedList = globalMgr.mCardListMap.get(mFolder.getId());

        mGestureDetector = new GestureDetector(this, simpleOnGestureListener);
        mCurrentCardIndex = 0;
        mTotalCardNum = mCardModelLinkedList.size();

        // 標準のActionBarの代わりにToolbarをActionBarとしてセットして利用する
        // ToolBar内の中央にタイトルを表示したいのでTextViewを配置してCenterにレイアウトする
        // (Toolbarに対する直接のsetTitleでは中央表示はできないため）
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setBackground(new ColorDrawable(globalMgr.skinHeaderColor));
        TextView title = findViewById(R.id.textViewTitle);
        title.setText(globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).getTitleName());    // setSupportActionBar()の前に呼ぶ必要あり
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

//        MobileAds.initialize(mContext, BuildConfig.ADMOB_APPLICATION_ID);
        // Application ID is defined in Manifest file instead.
        MobileAds.initialize(mContext);
        setBottomBannerAdView();
        prepareInterstitialAd();

        // Exercise Direction
        mImageViewExerciseDirection = findViewById(R.id.imageViewExerciseDirection);
        mImageViewExerciseDirection.setColorFilter(globalMgr.skinHeaderColor);
        mImageViewExerciseDirection.setOnClickListener(view -> {
            if (mFrontBackOrder == Constants.EXERCISE_FRONT_TO_BACK) {
                mFrontBackOrder = Constants.EXERCISE_BACK_TO_FRONT;
                mImageViewExerciseDirection.setImageResource(R.drawable.ic_arrow_back_40px);
                mTextViewFront.setText(R.string.back_mask);
                mTextViewBack.setText(mCard.getBackText());
            }
            else {
                mFrontBackOrder = Constants.EXERCISE_FRONT_TO_BACK;
                mImageViewExerciseDirection.setImageResource(R.drawable.ic_arrow_forward_40px);
                mTextViewFront.setText(mCard.getFrontText());
                mTextViewBack.setText(R.string.back_mask);
            }
        });

        // Filter handling
        mButtonFilterAll = findViewById(R.id.buttonFilterAll);
        mButtonFilterNotLearnedOnly = findViewById(R.id.buttonFilterLearned);
        mButtonFilterFusenOnly = findViewById(R.id.buttonFilterFusen);
        mButtonFilterAll.setOnClickListener(view -> {
            mButtonFilterAll.setBackgroundColor(globalMgr.skinHeaderColor);
            mButtonFilterAll.setTextColor(Color.WHITE);
            mButtonFilterNotLearnedOnly.setBackgroundColor(Color.WHITE);
            mButtonFilterNotLearnedOnly.setTextColor(Color.GRAY);
            mButtonFilterFusenOnly.setBackgroundColor(Color.WHITE);
            mButtonFilterFusenOnly.setTextColor(Color.GRAY);
            mFilter = Constants.EXERCISE_FILTER_ALL_CARDS;
            prepareToStartExercise();
        });
        mButtonFilterNotLearnedOnly.setOnClickListener(view -> {
            mButtonFilterAll.setBackgroundColor(Color.WHITE);
            mButtonFilterAll.setTextColor(Color.GRAY);
            mButtonFilterNotLearnedOnly.setBackgroundColor(globalMgr.skinHeaderColor);
            mButtonFilterNotLearnedOnly.setTextColor(Color.WHITE);
            mButtonFilterFusenOnly.setBackgroundColor(Color.WHITE);
            mButtonFilterFusenOnly.setTextColor(Color.GRAY);
            mFilter = Constants.EXERCISE_FILTER_NOT_LEARNED_ONLY;
            prepareToStartExercise();
        });
        mButtonFilterFusenOnly.setOnClickListener(view -> {
            mButtonFilterAll.setBackgroundColor(Color.WHITE);
            mButtonFilterAll.setTextColor(Color.GRAY);
            mButtonFilterNotLearnedOnly.setBackgroundColor(Color.WHITE);
            mButtonFilterNotLearnedOnly.setTextColor(Color.GRAY);
            mButtonFilterFusenOnly.setBackgroundColor(globalMgr.skinHeaderColor);
            mButtonFilterFusenOnly.setTextColor(Color.WHITE);
            mFilter = Constants.EXERCISE_FILTER_FUSEN_ONLY;
            prepareToStartExercise();
        });

        mCardViewFront = findViewById(R.id.cardViewFront);
        mCardViewBack = findViewById(R.id.cardViewBack);
        mTextViewFront = findViewById(R.id.textViewFront);
        mTextViewBack = findViewById(R.id.textViewBack);
        mImageViewIconFront = findViewById(R.id.imageViewIconFront);
        mImageViewIconBack = findViewById(R.id.imageViewIconBack);
        mImageViewLearned = findViewById(R.id.imageViewLearned);
        mImageViewLearned.setOnClickListener(view -> {
            if (mCard.isLearned()) {
                mCard.setLearned(false);
                ((ImageView) view).setImageResource(R.drawable.heart_off_grey);
            } else {
                mCard.setLearned(true);
                ((ImageView) view).setImageResource(R.drawable.heart_on);
            }
        });
        mImageViewFusen = findViewById(R.id.imageViewFusen);
        mImageViewFusen.setOnClickListener(view -> {
            FusenListDialogFragment fusenListDialogFragment = new FusenListDialogFragment(mCard.getImageFusenResId());
            fusenListDialogFragment.setOnChangeListener(newFusenResId -> {
                ((ImageView) view).setImageResource(newFusenResId);
                mCard.setImageFusenResId(newFusenResId);
            });
            fusenListDialogFragment.show(getSupportFragmentManager(), FusenListDialogFragment.class.getSimpleName());
        });

        // mTextToSpeech インスタンス生成
        mTextToSpeech = new TextToSpeech(this, this);
        findViewById(R.id.imageViewSpeacker).setOnClickListener(view -> {
            speechText(mTextViewFront.getText().toString());
        });

        mTextViewProgress = findViewById(R.id.textViewProgress);

        // Body背景色
        findViewById(R.id.relativeLayoutWhole).setBackgroundColor(globalMgr.skinBodyColor);

        // Filterのデフォルト（Allに色をつける）
        mButtonFilterAll.setBackgroundColor(globalMgr.skinHeaderColor);
        mButtonFilterAll.setTextColor(Color.WHITE);
        mButtonFilterNotLearnedOnly.setBackgroundColor(Color.WHITE);
        mButtonFilterNotLearnedOnly.setTextColor(Color.GRAY);
        mButtonFilterFusenOnly.setBackgroundColor(Color.WHITE);
        mButtonFilterFusenOnly.setTextColor(Color.GRAY);

        if (mExerciseMode == Constants.EXERCISE_MODE_SHUFFLE) {
            mCardModelLinkedListShuffle = new LinkedList<>(mCardModelLinkedList);
            Collections.shuffle(mCardModelLinkedListShuffle);
        }

        // 先頭カードの表示なので
        prepareToStartExercise();
    }

    @Override
    // ロケールをJAPANにすると、ローマ字読みになるので、英語の単語を英語で発音させるには、USにする必要がある。
    // よってFrontTextを何語で入力して読ませるかは、ユーザの使い方次第なので、一律にUSにもできない。
    // よって、設定のどこかで、音声読み上げの言語を選択できるようにする必要がある
    public void onInit(int status) {
        if (TextToSpeech.SUCCESS == status) {
            //言語選択
            Locale locale = Locale.US;
            mTextToSpeech.setLanguage(locale);
//            Locale locale = Locale.JAPAN;
//            if (mTextToSpeech.isLanguageAvailable(locale) >= TextToSpeech.LANG_AVAILABLE) {
//                mTextToSpeech.setLanguage(locale);
//            } else {
//                LogUtility.d("onInit");
//            }
        } else {
            LogUtility.d("onInit");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        LogUtility.d("onDestroy");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_help, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        // ActionBar（ToolBar）内のボタンのイベン処理
        switch (item.getItemId()) {
            case android.R.id.home:
                if (new Random().nextInt(100) % 3 == 0 && mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
                else {
                    finish();
                }
                return true;
            case R.id.action_menu_help:
                ExerciseHelpDialogFragment exerciseHelpDialogFragment = new ExerciseHelpDialogFragment();
                exerciseHelpDialogFragment.show(getSupportFragmentManager(),
                        ExerciseHelpDialogFragment.class.getSimpleName());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * @param event
     * @return
     */
    //簡単なタッチイベント処理
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        /**
         * 全てSimpleOnGestureListenerの方でカバーできるので
         * SimpleOnGestureListener側に引き継ぐ
         */
        return mGestureDetector.onTouchEvent(event);
    }

    /**
     * 複雑なタッチイベント処理
     * SingleTap      →　答え表示　→　SingleTap　→　次の問題
     * DoubleTap      →　次の問題
     * Swipe(LEFT)    →　次の問題
     * Swipe(RIGHT)   →　前の問題
     */
    private final GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDoubleTap(MotionEvent event) {
            //LogUtility.d("onDoubleTap");
            showCardContents(true);
            mExerciseHasStarted = true;
            return super.onDoubleTap(event);
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent event) {
            //LogUtility.d("onDoubleTapEvent");
            return super.onDoubleTapEvent(event);
        }

        @Override
        public boolean onDown(MotionEvent event) {
            //LogUtility.d("onDown");
            return super.onDown(event);
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY) {
            mExerciseHasStarted = true;
            float startTouchX = event1.getX();
            float endTouchX = event2.getX();
            LogUtility.d("onFling startTouchX:" + startTouchX + " endTouchX:" + endTouchX);
            if (startTouchX > endTouchX) showCardContents(true);
            else showCardContents(false);

            return super.onFling(event1, event2, velocityX, velocityY);
        }

        @Override
        public void onLongPress(MotionEvent event) {
            //LogUtility.d("onLongPress");
            super.onLongPress(event);
        }

        @Override
        public boolean onScroll(MotionEvent event1, MotionEvent event2, float distanceX, float distanceY) {
            //LogUtility.d("onScroll distanceX:" + distanceX + " distanceY:" + distanceY);
            return super.onScroll(event1, event2, distanceX, distanceY);
        }

        @Override
        public void onShowPress(MotionEvent event) {
            //LogUtility.d("onShowPress");
            super.onShowPress(event);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {
            //LogUtility.d("onSingleTapConfirmed");
            if (mSecondCardTextWasDisplayed) {
                showCardContents(true);
                mSecondCardTextWasDisplayed = false;
            } else {
                if (mFrontBackOrder == Constants.EXERCISE_FRONT_TO_BACK) {
                    mTextViewBack.setText(mCard.getBackText());
                }
                else {
                    mTextViewFront.setText(mCard.getFrontText());
                }
                mSecondCardTextWasDisplayed = true;
            }
            mExerciseHasStarted = true;

            return super.onSingleTapConfirmed(event);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            //LogUtility.d("onSingleTapUp");
            return super.onSingleTapUp(event);
        }

    };

    private void speechText(String string) {
        if (0 < string.length()) {
            if (mTextToSpeech.isSpeaking()) {
                mTextToSpeech.stop();
                return;
            }
            setSpeechRate(1.0f);
            setSpeechPitch(1.0f);

            if (Build.VERSION.SDK_INT >= 21) {
                // SDK 21 以上
                mTextToSpeech.speak(string, TextToSpeech.QUEUE_FLUSH, null, "messageID");
            } else {
                // mTextToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null) に
                // KEY_PARAM_UTTERANCE_ID を HasMap で設定
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "messageID");
                mTextToSpeech.speak(string, TextToSpeech.QUEUE_FLUSH, map);
            }

            setmTextToSpeechListener();
        }
    }

    // 読み上げのスピード
    private void setSpeechRate(float rate) {
        if (null != mTextToSpeech) {
            mTextToSpeech.setSpeechRate(rate);
        }
    }

    // 読み上げのピッチ
    private void setSpeechPitch(float pitch) {
        if (null != mTextToSpeech) {
            mTextToSpeech.setPitch(pitch);
        }
    }

    // 読み上げの始まりと終わりを取得
    private void setmTextToSpeechListener() {
        // android version more than 15th
        if (Build.VERSION.SDK_INT >= 15) {
            int listenerResult =
                    mTextToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                        @Override
                        public void onDone(String utteranceId) {
//                            Log.d(TAG,"progress on Done " + utteranceId);
                        }

                        @Override
                        public void onError(String utteranceId) {
//                            Log.d(TAG,"progress on Error " + utteranceId);
                        }

                        @Override
                        public void onStart(String utteranceId) {
//                            Log.d(TAG,"progress on Start " + utteranceId);
                        }
                    });

            if (listenerResult != TextToSpeech.SUCCESS) {
//                Log.e(TAG, "failed to add utterance progress listener");
            }
        } else {
//            Log.e(TAG, "Build VERSION is less than API 15");
        }
    }

    /**
     * 画面下部のバナー広告
     */
    private void setBottomBannerAdView() {
        // バナー広告
        RelativeLayout mRelativeLayout = findViewById(R.id.relativeLayoutWhole);
        AdView adView = new AdView(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        adView.setLayoutParams(params);
        mRelativeLayout.addView(adView);
        adView.setAdSize(AdSize.BANNER);      // 320×50
        //adView.setAdSize(AdSize.SMART_BANNER);  // 画面の幅×50
        adView.setAdUnitId(BuildConfig.ADMOB_BANNER_UNIT_ID_EXERCISE);

        // XMLで定義する場合は上記をコメントにし、以下の１行だけを有効にする
        // AdView adView = findViewById(R.id.adView);

        // 以下でなくてもEmulatorで動いた
        // AdRequest adRequest = new AdRequest.Builder().addTestDevice( AdRequest.DEVICE_ID_EMULATOR ).build();
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        // ad's lifecycle: loading, opening, closing, and so on
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                LogUtility.d("Code to be executed when an ad finishes loading.");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                LogUtility.d("Code to be executed when an ad request fails.");
            }

            @Override
            public void onAdOpened() {
                LogUtility.d("Code to be executed when an ad opens an overlay that covers the screen.");
            }

            @Override
            public void onAdLeftApplication() {
                LogUtility.d("Code to be executed when the user has left the app.");
            }

            @Override
            public void onAdClosed() {
                LogUtility.d("Code to be executed when when the user is about to return to the app after tapping on an ad.");
            }
        });
    }

    /**
     * InterstitialAdの準備
     * 読み込んでおくが表示はしない
     */
    public void prepareInterstitialAd() {
        mInterstitialAd = new InterstitialAd(this);
        // Release版では同じIDがセットされ、AdMob側で自動で静止画・動画が選択される
        String unitId = new Random().nextInt(100) % 2 == 0 ? BuildConfig.ADMOB_INTERSTITIAL_MOVIE_UNIT_ID : BuildConfig.ADMOB_INTERSTITIAL_UNIT_ID;
        mInterstitialAd.setAdUnitId(unitId);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                LogUtility.d("Code to be executed when an ad finishes loading.");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                LogUtility.d("Code to be executed when an ad request fails.");
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
                LogUtility.d("Code to be executed when the ad is displayed.");
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                LogUtility.d("Code to be executed when the user clicks on an ad.");
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                LogUtility.d("Code to be executed when the user has left the app.");
            }

            @Override
            public void onAdClosed() {
                LogUtility.d("Code to be executed when the interstitial ad is closed.");
                // ユーザが広告を閉じたのでFolder一覧に戻る
                finish();
            }
        });
    }

    public void prepareToStartExercise() {
        mCurrentCardIndex = -1;
        mNotLearnedCardNum = 0;
        mFusenCardNum = 0;
        mCardNumToExercise = mCardModelLinkedList.size();
        mExercisedCardNum = -1;

        for (CardModel card : mCardModelLinkedList) {
            if (!card.isLearned()) {
                mNotLearnedCardNum++;
            }
            if (card.getImageFusenResId() != R.drawable.fusen_00) {
                mFusenCardNum++;
            }
        }

        if (mFilter == Constants.EXERCISE_FILTER_ALL_CARDS) {
            mCardNumToExercise = mCardModelLinkedList.size();
        } else if (mFilter == Constants.EXERCISE_FILTER_NOT_LEARNED_ONLY) {
            mCardNumToExercise = mNotLearnedCardNum;
        } else if (mFilter == Constants.EXERCISE_FILTER_FUSEN_ONLY) {
            mCardNumToExercise = mFusenCardNum;
        }

        // 練習開始するが、対象カード数をチェックし0だったら、アラートダイアログを表示し、FilterをAllに強制的に変更する。
        if (mFilter == Constants.EXERCISE_FILTER_NOT_LEARNED_ONLY && mNotLearnedCardNum == 0 ||
                mFilter == Constants.EXERCISE_FILTER_FUSEN_ONLY && mFusenCardNum == 0) {
            mFilter = Constants.EXERCISE_FILTER_ALL_CARDS;
            showCardContents(true);
            mButtonFilterAll.setBackgroundColor(globalMgr.skinHeaderColor);
            mButtonFilterAll.setTextColor(Color.WHITE);
            mButtonFilterNotLearnedOnly.setBackgroundColor(Color.WHITE);
            mButtonFilterNotLearnedOnly.setTextColor(Color.GRAY);
            mButtonFilterFusenOnly.setBackgroundColor(Color.WHITE);
            mButtonFilterFusenOnly.setTextColor(Color.GRAY);
            new AlertDialog.Builder(mActivity)
                    .setIcon(IconManager.getResIdAtRandom(globalMgr.mCategory))
                    .setTitle(R.string.dlg_title_information)
                    .setMessage(R.string.dlg_msg_force_all_card_exercise)
                    .setPositiveButton(
                            android.R.string.ok,
                            (dialog, which) -> {
                                LogUtility.d("[OK]が選択されました");
                                // 単にダイアログが消えて何もしない
                            })
                    .show();
        } else {
            showCardContents(true);
        }
    }

    /**
     * 表示すべき前後のカードを見つけて表示する
     *
     * @param next 　true: 次に進む場合　false: 前に戻る場合
     */
    public void showCardContents(boolean next) {
        if (next) mCurrentCardIndex++;
        else mCurrentCardIndex--;

        while (mCurrentCardIndex < mTotalCardNum) {
            if (mCurrentCardIndex == -1) {
                // 先頭に戻りきっているのでアラートを出す
                new AlertDialog.Builder(mActivity)
                        .setIcon(IconManager.getResIdAtRandom(globalMgr.mCategory))
                        .setTitle(R.string.dlg_title_information)
                        .setMessage(R.string.dlg_msg_first_card_now)
                        .setPositiveButton(
                                android.R.string.ok,
                                (dialog, which) -> {
                                    LogUtility.d("[OK]が選択されました");
                                    // 単にダイアログが消えて何もしない
                                })
                        .show();
                return;
            }

            if (mExerciseMode == Constants.EXERCISE_MODE_SHUFFLE)
                mCard = mCardModelLinkedListShuffle.get(mCurrentCardIndex);
            else
                mCard = mCardModelLinkedList.get(mCurrentCardIndex);

            // フィルターチェック
            if (mFilter == Constants.EXERCISE_FILTER_ALL_CARDS) {
                break;
            } else if (mFilter == Constants.EXERCISE_FILTER_NOT_LEARNED_ONLY) {
                if (!mCard.isLearned()) {
                    break;
                }
            } else if (mFilter == Constants.EXERCISE_FILTER_FUSEN_ONLY) {
                if (mCard.getImageFusenResId() != R.drawable.fusen_00) {
                    break;
                }
            }

            // 上記でフィルターにかからなかったのでスキップして次をチェックする
            if (next) mCurrentCardIndex++;
            else mCurrentCardIndex--;
        }

        if (mCurrentCardIndex >= mTotalCardNum) {
            // ここに来るのはカード全体を超えた場合
            // 終了の表示をする
            new AlertDialog.Builder(mActivity)
                    .setIcon(IconManager.getResIdAtRandom(globalMgr.mCategory))
                    .setTitle(R.string.dlg_title_information)
                    .setMessage(R.string.dlg_msg_exercise_end)
                    .setPositiveButton(
                            R.string.go_back_list,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    LogUtility.d("[一覧に戻る]が選択されました");
//                                    finish();
                                    if (mInterstitialAd.isLoaded()) {
                                        mInterstitialAd.show();
                                    } else {
                                        LogUtility.d("InterstitialAdが準備できてないので広告表示をスキップする");
                                        finish();
                                    }
                                }
                            })
                    .show();
            return;
        }

        // 表示すべきカードが見つかったので表示する
        mCardViewFront.setCardBackgroundColor(mFolder.getFrontBackgroundColor());
        mCardViewBack.setCardBackgroundColor(mFolder.getBackBackgroundColor());
        mTextViewFront.setTextColor(mFolder.getFrontTextColor());
        mTextViewBack.setTextColor(mFolder.getBackTextColor());
        if (mFrontBackOrder == Constants.EXERCISE_FRONT_TO_BACK) {
            mTextViewFront.setText(mCard.getFrontText());
            mTextViewBack.setText(R.string.back_mask);
        }
        else {
            mTextViewFront.setText(R.string.back_mask);
            mTextViewBack.setText(mCard.getBackText());
        }
        int resourceId = globalMgr.isIconAuto ? IconManager.getResIdAtRandom(mCard.getIconCategory()) : mCard.getImageIconResId();
        mImageViewIconFront.setImageResource(resourceId);
        mImageViewIconBack.setImageResource(resourceId);
        if (mCard.isLearned()) mImageViewLearned.setImageResource(R.drawable.heart_on);
        else mImageViewLearned.setImageResource(R.drawable.heart_off_grey);
        mImageViewFusen.setImageResource(mCard.getImageFusenResId());
        if (next) {
            mExercisedCardNum++;
        } else {
            mExercisedCardNum--;
        }
        mTextViewProgress.setText(String.format("%d問中 %d問目", mCardNumToExercise, mExercisedCardNum + 1));

        mSecondCardTextWasDisplayed = false;            // 新しいカードに移ったので裏面表示のステータスを「未表示」にする
        return;
    }

    /**
     * interstitial広告表示
     */

}
