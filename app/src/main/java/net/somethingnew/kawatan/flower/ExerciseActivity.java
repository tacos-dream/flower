package net.somethingnew.kawatan.flower;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.google.android.material.chip.Chip;

import net.somethingnew.kawatan.flower.model.CardModel;
import net.somethingnew.kawatan.flower.model.FolderModel;
import net.somethingnew.kawatan.flower.util.LogUtility;

import java.util.Collections;
import java.util.LinkedList;

public class ExerciseActivity extends AppCompatActivity {

    private GlobalManager                       globalMgr = GlobalManager.getInstance();
    private Context                             mContext;
    private Activity                            mActivity;
    private GestureDetector                     mGestureDetector;
    private FolderModel                         mFolder;
    private CardModel                           mCard;
    private LinkedList<CardModel>               mCardModelLinkedList;
    private LinkedList<CardModel>               mCardModelLinkedListShuffle;
    private Boolean                             mUnderExercise = false;
    private int                                 mTotalCardNum;
    private int                                 mCurrentCardIndex;
    private int                                 mLearnedFilterState;
    private int                                 mFusenFilterState;
    private int                                 mExerciseMode;

    private CardView                            mCardViewFront;
    private CardView                            mCardViewBack;
    private TextView                            mTextViewFront;
    private TextView                            mTextViewBack;
    private ImageView                           mImageViewIcon;
    private ImageView                           mImageViewLearned;
    private ImageView                           mImageViewFusen;
    private Chip                                mChipLearned;
    private Chip                                mChipNotLearned;
    private Chip                                mChipFusen;
    private Chip                                mChipNoFusen;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        mContext                = getApplicationContext();
        mActivity               = ExerciseActivity.this;

        // キーを元にデータを取得する(データがない場合、第２引数の 0 が返る)
        mExerciseMode           = getIntent().getIntExtra(Constants.EXERCISE_MODE_KEY_NAME, Constants.EXERCISE_MODE_NORMAL);
        mFolder                 = globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex);
        mCardModelLinkedList    = globalMgr.mCardListMap.get(mFolder.getId());

        mGestureDetector        = new GestureDetector(this, simpleOnGestureListener);
        mCurrentCardIndex       = 0;
        mTotalCardNum           = mCardModelLinkedList.size();

        mCardViewFront          = findViewById(R.id.card_view_front);
        mCardViewBack           = findViewById(R.id.card_view_back);
        mTextViewFront          = findViewById(R.id.textViewFront);
        mTextViewBack           = findViewById(R.id.textViewBack);
        mImageViewIcon          = findViewById(R.id.imageViewIcon);
        mImageViewLearned       = findViewById(R.id.imageViewLearned);
        mImageViewFusen         = findViewById(R.id.imageViewFusen);
        mChipLearned            = findViewById(R.id.chipLearned);
        mChipNotLearned         = findViewById(R.id.chipNotLearned);
        mChipFusen              = findViewById(R.id.chipFusen);
        mChipNoFusen            = findViewById(R.id.chipNoFusen);

        Toolbar toolbar         = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        buildEventListener();

        // Filterのデフォルト（未収得＆付箋あり＆付箋なし）
        // setChecked()の実行に伴い各ChipのonCheckedChanged()が呼び出される
        mLearnedFilterState     = 0;
        mFusenFilterState       = 0;
        mChipNotLearned.setChecked(true);
        mChipFusen.setChecked(true);
        mChipNoFusen.setChecked(true);

        if (mExerciseMode == Constants.EXERCISE_MODE_SHUFFLE) {
            mCardModelLinkedListShuffle = new LinkedList<>(mCardModelLinkedList);
            Collections.shuffle(mCardModelLinkedListShuffle);
        }

        showCardContents();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtility.d("onDestroy");
    }

    /**
     *
     * @param event
     * @return
     */
    //簡単なタッチイベント処理
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        /**
         * 全てSimpleOnGestureListenerの方でカバーできるのでこちらはすべてコメントにして
         * SimpleOnGestureListener側に引き継ぐ
         */

        /*
        switch ( event.getAction() ) {

            case MotionEvent.ACTION_DOWN:
                //画面がタッチされたときの動作
                LogUtility.d("ACTION_DOWN");
                break;

            case MotionEvent.ACTION_MOVE:
                //タッチしたまま移動したときの動作
                LogUtility.d("ACTION_MOVE");
                break;

            case MotionEvent.ACTION_UP:
                //タッチが離されたときの動作
                LogUtility.d("ACTION_UP");
                break;

            case MotionEvent.ACTION_CANCEL:
                LogUtility.d("ACTION_CANCEL");
                //他の要因によってタッチがキャンセルされたときの動作
                break;

        }

         */

        //return super.onTouchEvent(event);
        return mGestureDetector.onTouchEvent(event);
    }

    //複雑なタッチイベント処理
    private final GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDoubleTap(MotionEvent event) {
            //LogUtility.d("onDoubleTap");
            mUnderExercise          = true;
            mCurrentCardIndex++;
            showCardContents();
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
            mUnderExercise          = true;
            float startTouchX       = event1.getX();
            float endTouchX         = event2.getX();
            //LogUtility.d("onFling startTouchX:" + startTouchX + " endTouchX:" + endTouchX);
            mCurrentCardIndex++;
            showCardContents();
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
            mUnderExercise          = true;
            mTextViewBack.setText(mCard.getBackText());
            return super.onSingleTapConfirmed(event);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            //LogUtility.d("onSingleTapUp");
            return super.onSingleTapUp(event);
        }

    };

    /**
     * 上部メニュー領域の各アイコン、表示フィルターのChip、カードの編集箇所のイベントハンドラ
     */
    public void buildEventListener() {
        // 戻る
        findViewById(R.id.imageViewGoBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity().getApplicationContext(), "キャンセル", Toast.LENGTH_LONG).show();

                if (!mUnderExercise) {
                    // 練習開始前の場合は確認ダイアログ無しですぐに一覧に戻る
                    finish();
                    return;
                }

                new AlertDialog.Builder(mActivity)
                        .setIcon(R.drawable.flower_024_19)
                        .setMessage(R.string.dlg_msg_go_back_to_list_simple)
                        .setPositiveButton(
                                R.string.go_back_list,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        LogUtility.d("[一覧に戻る]が選択されました");
                                        finish();
                                    }
                                })
                        .setNegativeButton(
                                R.string.cancel,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        LogUtility.d("[キャンセル]が選択されました");
                                        // このAlertDialogだけが消え、親のダイアログ表示状態に戻る
                                    }
                                })
                        .show();

            }
        });

        // 表示フィルター　Chip 関連
        mChipLearned.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                LogUtility.d("mChipLearned onCheckedChanged");
                if (isChecked) {
                    mLearnedFilterState += Constants.FILTER_STATE_LEARNED;
                }
                else {
                    if (mLearnedFilterState == Constants.FILTER_STATE_LEARNED) {
                        // 未習得の方のチェックがはずれている時に習得済のチェックをはずそうとした場合は、
                        // 未習得をActiveに変更し、両方チェック無しの状態にはさせない
                        mChipNotLearned.setChecked(true);
                    }
                    mLearnedFilterState -= Constants.FILTER_STATE_LEARNED;
                }
            }
        });
        mChipNotLearned.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                LogUtility.d("mChipNotLearned onCheckedChanged");
                if (isChecked) {
                    mLearnedFilterState += Constants.FILTER_STATE_NOT_LEARNED;
                }
                else {
                    if (mLearnedFilterState == Constants.FILTER_STATE_NOT_LEARNED) {
                        // 習得済の方のチェックがはずれている時に未習得のチェックをはずそうとした場合は、
                        // 習得済をActiveに変更し、両方チェック無しの状態にはさせない
                        mChipLearned.setChecked(true);
                    }
                    mLearnedFilterState -= Constants.FILTER_STATE_NOT_LEARNED;
                }
            }
        });
        mChipFusen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                LogUtility.d("mChipFusen onCheckedChanged");
                if (isChecked) {
                    mFusenFilterState += Constants.FILTER_STATE_FUSEN;
                }
                else {
                    if (mFusenFilterState == Constants.FILTER_STATE_FUSEN) {
                        // 付箋なしの方のチェックがはずれている時に付箋ありのチェックをはずそうとした場合は、
                        // 「付箋無し」をActiveに変更し、両方チェック無しの状態にはさせない
                        mChipNoFusen.setChecked(true);
                    }
                    mFusenFilterState -= Constants.FILTER_STATE_FUSEN;
                }
            }
        });
        mChipNoFusen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                LogUtility.d("mChipNoFusen onCheckedChanged");
                if (isChecked) {
                    mFusenFilterState += Constants.FILTER_STATE_NO_FUSEN;
                }
                else {
                    if (mFusenFilterState == Constants.FILTER_STATE_NO_FUSEN) {
                        // 付箋ありの方のチェックがはずれている時に付箋なしのチェックをはずそうとした場合は、
                        // 「付箋あり」をActiveに変更し、両方チェック無しの状態にはさせない
                        mChipFusen.setChecked(true);
                    }
                    mFusenFilterState -= Constants.FILTER_STATE_NO_FUSEN;
                }
            }
        });

        // おもてカード内　学習済み
        findViewById(R.id.imageViewLearned).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtility.d("imageViewLearned");
                if (mCard.isLearned()) {
                    mCard.setLearned(false);
                    mImageViewLearned.setImageResource(R.drawable.heart_off_grey);
                }
                else {
                    mCard.setLearned(true);
                    mImageViewLearned.setImageResource(R.drawable.heart_on);
                }
            }
        });

        // おもてカード内　付箋
        findViewById(R.id.imageViewFusen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtility.d("imageViewFusen");
                if (mCard.isFusenTag()) {
                    mCard.setFusenTag(false);
                    mImageViewFusen.setImageResource(R.drawable.fusen_00);
                }
                else {
                    mCard.setFusenTag(true);
                    mImageViewFusen.setImageResource(mFolder.getImageFusenResId());
                }
            }
        });


    }

    public void showCardContents() {
        while (mCurrentCardIndex < mTotalCardNum) {
            //mCard           = mCardModelLinkedList.get(mCurrentCardIndex);
            if (mExerciseMode == Constants.EXERCISE_MODE_SHUFFLE) {
                mCard           = mCardModelLinkedListShuffle.get(mCurrentCardIndex);
            }
            else {
                mCard           = mCardModelLinkedList.get(mCurrentCardIndex);
            }

            // フィルターチェック
            if (mLearnedFilterState == Constants.FILTER_STATE_LEARNED) {
                // フィルターが習得済のみの場合
                if (mCard.isLearned()) {
                    // 習得フィルターは「習得済」で一致したので、付箋フィルターをチェック
                    if (mFusenFilterState == Constants.FILTER_STATE_FUSEN) {
                        if (mCard.isFusenTag()) {
                            // 付箋フィルターが「付箋あり」で一致したので対象
                            break;
                        }
                    }
                    else if (mFusenFilterState == Constants.FILTER_STATE_NO_FUSEN) {
                        if (!mCard.isFusenTag()) {
                            // 付箋フィルターが「付箋なし」で一致したので対象
                            break;
                        }
                    }
                    else {
                        // 付箋あり・なしすべてなので対象
                        break;
                    }
                }
            }
            else if (mLearnedFilterState == Constants.FILTER_STATE_NOT_LEARNED) {
                // フィルターが未習得のみの場合
                if (!mCard.isLearned()) {
                    // 習得フィルターは「未習得」で一致したので、付箋フィルターをチェック
                    if (mFusenFilterState == Constants.FILTER_STATE_FUSEN) {
                        if (mCard.isFusenTag()) {
                            // 付箋フィルターが「付箋あり」で一致したので対象
                            break;
                        }
                    }
                    else if (mFusenFilterState == Constants.FILTER_STATE_NO_FUSEN) {
                        if (!mCard.isFusenTag()) {
                            // 付箋フィルターが「付箋なし」で一致したので対象
                            break;
                        }
                    }
                    else {
                        // 付箋あり・なしすべてなので対象
                        break;
                    }
                }
            }
            else {
                // 習得済・未習得すべて
                // 付箋フィルターをチェックする
                if (mFusenFilterState == Constants.FILTER_STATE_FUSEN) {
                    if (mCard.isFusenTag()) {
                        // 付箋フィルターが「付箋あり」で一致したので対象
                        break;
                    }
                }
                else if (mFusenFilterState == Constants.FILTER_STATE_NO_FUSEN) {
                    if (!mCard.isFusenTag()) {
                        // 付箋フィルターが「付箋なし」で一致したので対象
                        break;
                    }
                }
                else {
                    // 付箋あり・なしすべてなので対象
                    break;
                }
            }

            // 上記でフィルターにかからなかったのでスキップして次をチェックする
            mCurrentCardIndex++;
        }

        if (mCurrentCardIndex >= mTotalCardNum) {
            // TODO 終了の表示をする
            new AlertDialog.Builder(mActivity)
                    .setIcon(R.drawable.flower_024_19)
                    .setMessage(R.string.dlg_msg_exercise_end)
                    .setPositiveButton(
                            R.string.go_back_list,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    LogUtility.d("[一覧に戻る]が選択されました");
                                    finish();
                                }
                            })
                    .setNegativeButton(
                            R.string.try_again,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    LogUtility.d("[もう一度]が選択されました");
                                    mCurrentCardIndex = 0;
                                    showCardContents();
                                }
                            })
                    .show();
            return;
        }

        mCardViewFront.setCardBackgroundColor(mFolder.getFrontBackgroundColor());
        mCardViewBack.setCardBackgroundColor(mFolder.getBackBackgroundColor());
        mTextViewFront.setTextColor(mFolder.getFrontTextColor());
        mTextViewFront.setText(mCard.getFrontText());
        mTextViewBack.setTextColor(mFolder.getBackTextColor());
        mTextViewBack.setText(R.string.back_mask);
        mImageViewIcon.setImageResource(mCard.getImageIconResId());

        if (mCard.isLearned()) {
            mImageViewLearned.setImageResource(R.drawable.heart_on);
        }
        else {
            mImageViewLearned.setImageResource(R.drawable.heart_off_grey);
        }

        if (mCard.isFusenTag()) {
            mImageViewFusen.setImageResource(mFolder.getImageFusenResId());
        }
        else {
            mImageViewFusen.setImageResource(R.drawable.fusen_00);
        }
    }

}
