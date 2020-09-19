package net.somethingnew.kawatan.flower;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.clockbyte.admobadapter.AdmobAdapterCalculator;
import com.clockbyte.admobadapter.bannerads.AdmobBannerRecyclerAdapterWrapper;
import com.clockbyte.admobadapter.bannerads.BannerAdViewWrappingStrategyBase;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import net.somethingnew.kawatan.flower.db.dao.CardDao;
import net.somethingnew.kawatan.flower.db.dao.FolderDao;
import net.somethingnew.kawatan.flower.model.CardModel;
import net.somethingnew.kawatan.flower.util.LogUtility;

import java.util.LinkedList;

/**
 * 特定Folderの詳細画面（つまり、当該Folderが保持するCard一覧画面）
 */
public class CardListActivity extends AppCompatActivity {

    private Context mContext;
    private Activity mActivity;
    private GlobalManager globalMgr = GlobalManager.getInstance();
    private SearchView mSearchView;
    private RecyclerView mRecyclerView;
    private CardListRecyclerViewAdapter mRecyclerViewAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private AdmobBannerRecyclerAdapterWrapper mBannerRecyclerAdapterWrapper;
    private AdmobAdapterCalculator mAdapterCalc;

    private LinkedList<CardModel> mCardLinkedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);

        // Get the application context
        mContext = getApplicationContext();
        mActivity = CardListActivity.this;

        // Card総数増減、Learned数増減時に更新
        globalMgr.mCardStatsChanged = false;

        mCardLinkedList = globalMgr.mCardListMap.get(globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).getId());

        // 標準のActionBarの代わりにToolbarをActionBarとしてセットして利用する
        // ToolBar内の中央にタイトルを表示したいのでTextViewを配置してCenterにレイアウトする
        // (Toolbarに対する直接のsetTitleでは中央表示はできないため）
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        toolbar.setBackground(new ColorDrawable(globalMgr.skinHeaderColor));
        RecyclerView recyclerView = findViewById(R.id.my_recycler_view);
        recyclerView.setBackground(new ColorDrawable(globalMgr.skinBodyColor));
        TextView title = findViewById(R.id.toolbar_title);
        title.setText(globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).getTitleName());    // setSupportActionBar()の前に呼ぶ必要あり

        // ToolbarでもActionBar機能が使えるように設定
        setSupportActionBar(toolbar);

        // ActionBarの機能にあるBackボタン処理を有効にする
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        buildRecyclerViewWithBannerRecyclerWrapper();

        findViewById(R.id.fab).setOnClickListener(v -> {
            // 新規登録モードでダイアログを開く（Positionの-1は使われることはないので特に意味はなし）
            CardSettingsDialogFragment cardSettingsDialogFragment = new CardSettingsDialogFragment(mRecyclerViewAdapter, null, -1);
            Bundle args = new Bundle();
            args.putInt(Constants.CARD_SETTINGS_DIALOG_ARG_KEY_MODE, Constants.CARD_SETTINGS_FOR_NEW);
            cardSettingsDialogFragment.setArguments(args);
            cardSettingsDialogFragment.show(getSupportFragmentManager(),
                    FolderSettingsDialogFragment.class.getSimpleName());
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        //LogUtility.d("onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        //LogUtility.d("onResume");
        mBannerRecyclerAdapterWrapper.resumeAll();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        //LogUtility.d("onRestart");

        // ExerciseActivityから戻った時に習得済みや付箋の更新を反映させる
        mRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBannerRecyclerAdapterWrapper.pauseAll();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //LogUtility.d("onDestroy");
        mBannerRecyclerAdapterWrapper.release();
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
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_menu_help:
                CardListHelpDialogFragment cardListHelpDialogFragment = new CardListHelpDialogFragment();
                cardListHelpDialogFragment.show(getSupportFragmentManager(),
                        FolderSettingsDialogFragment.class.getSimpleName());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * RecyclerView関連準備
     * AdmobBannerRecyclerAdapterWrapper()を使ってのNativeAdの表示
     */
    public void buildRecyclerViewWithBannerRecyclerWrapper() {
        mRecyclerView = findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);        //RecyclerView内の表示形式にLinearLayoutを指定
        mRecyclerViewAdapter = new CardListRecyclerViewAdapter(mCardLinkedList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        String[] testDevicesIds = new String[]{BuildConfig.TEST_DEVICE_ID, AdRequest.DEVICE_ID_EMULATOR};
        mBannerRecyclerAdapterWrapper = AdmobBannerRecyclerAdapterWrapper.builder(this)
                .setLimitOfAds(Constants.ADMOB_AD_LIMITS)
                .setFirstAdIndex(Constants.ADMOB_FIRST_AD_INDEX)
                .setNoOfDataBetweenAds(Constants.ADMOB_NUM_OF_DATA_BETWEEN_ADS)
                .setTestDeviceIds(testDevicesIds)
                .setAdapter((RecyclerView.Adapter) mRecyclerViewAdapter)
                //Use the following for the default Wrapping behaviour
//                .setAdViewWrappingStrategy(new BannerAdViewWrappingStrategy())
                // Or implement your own custom wrapping behaviour:
                .setAdViewWrappingStrategy(new BannerAdViewWrappingStrategyBase() {
                    @NonNull
                    @Override
                    protected ViewGroup getAdViewWrapper(ViewGroup parent) {
                        return (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.native_express_ad_container,
                                parent, false);
                    }

                    @Override
                    protected void recycleAdViewWrapper(@NonNull ViewGroup wrapper, @NonNull AdView ad) {
                        //get the view which directly will contain ad
                        ViewGroup container = wrapper.findViewById(R.id.ad_container);
                        //iterating through all children of the container view and remove the first occured {@link NativeExpressAdView}. It could be different with {@param ad}!!!*//*
                        for (int i = 0; i < container.getChildCount(); i++) {
                            View v = container.getChildAt(i);
                            if (v instanceof AdView) {
                                container.removeViewAt(i);
                                break;
                            }
                        }
                    }

                    @Override
                    protected void addAdViewToWrapper(@NonNull ViewGroup wrapper, @NonNull AdView ad) {
                        //get the view which directly will contain ad
                        ViewGroup container = wrapper.findViewById(R.id.ad_container);
                        //add the {@param ad} directly to the end of container*//*
                        container.addView(ad);
                    }
                })
                .build();

        mRecyclerView.setAdapter(mBannerRecyclerAdapterWrapper);

        mAdapterCalc = mBannerRecyclerAdapterWrapper.getAdapterCalculator();

        // リスナーの実装
        mRecyclerViewAdapter.setOnItemClickListener(new CardListRecyclerViewAdapter.OnItemClickListener() {
            CardModel card;

            @Override
            public void onFrontClick(int position) {
                int fetchedAdsCnt = mBannerRecyclerAdapterWrapper.getFetchedAdsCount();
                int sourceCnt = mBannerRecyclerAdapterWrapper.getAdapter().getItemCount();
                globalMgr.mCurrentCardIndex = mAdapterCalc.getOriginalContentPosition(position, fetchedAdsCnt, sourceCnt);
                card = mCardLinkedList.get(globalMgr.mCurrentCardIndex);

                // 編集モードでダイアログを開く
                CardSettingsDialogFragment cardSettingsDialogFragment = new CardSettingsDialogFragment(mRecyclerViewAdapter, card, globalMgr.mCurrentCardIndex);
                Bundle args = new Bundle();
                args.putInt(Constants.CARD_SETTINGS_DIALOG_ARG_KEY_MODE, Constants.CARD_SETTINGS_FOR_EDIT);
                cardSettingsDialogFragment.setArguments(args);
                cardSettingsDialogFragment.show(getSupportFragmentManager(), FolderSettingsDialogFragment.class.getSimpleName());
            }

            @Override
            public void onLearnedClick(int position) {
                int fetchedAdsCnt = mBannerRecyclerAdapterWrapper.getFetchedAdsCount();
                int sourceCnt = mBannerRecyclerAdapterWrapper.getAdapter().getItemCount();
                globalMgr.mCurrentCardIndex = mAdapterCalc.getOriginalContentPosition(position, fetchedAdsCnt, sourceCnt);
                card = mCardLinkedList.get(globalMgr.mCurrentCardIndex);

                // 現状LearnedがOnだったらOffに、OffだったらOnにセットする
                if (card.isLearned()) {
                    card.setLearned(false);
                    globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).decrementNumOfLearnedCards();
                } else {
                    card.setLearned(true);
                    globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).incrementNumOfLearnedCards();
                }

                // DBに反映
                FolderDao folderDao = new FolderDao(getApplicationContext());
                CardDao cardDao = new CardDao(getApplicationContext());
                folderDao.update(globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex));
                cardDao.update(card);

                // 変更を表示に反映させる
                mRecyclerViewAdapter.notifyItemChanged(globalMgr.mCurrentCardIndex);

                globalMgr.mCardStatsChanged = true;         // Folder一覧表示時のリフレッシュ動作で参照
            }

            @Override
            public void onFusenClick(int position) {
                int fetchedAdsCnt = mBannerRecyclerAdapterWrapper.getFetchedAdsCount();
                int sourceCnt = mBannerRecyclerAdapterWrapper.getAdapter().getItemCount();
                globalMgr.mCurrentCardIndex = mAdapterCalc.getOriginalContentPosition(position, fetchedAdsCnt, sourceCnt);
                card = mCardLinkedList.get(globalMgr.mCurrentCardIndex);

                FusenListDialogFragment fusenListDialogFragment = new FusenListDialogFragment(card.getImageFusenResId());
                fusenListDialogFragment.setOnChangeListener(newFusenResId -> {
                    card.setImageFusenResId(newFusenResId);
                    mRecyclerViewAdapter.notifyItemChanged(globalMgr.mCurrentCardIndex);        // 変更を表示に反映させる
                });
                fusenListDialogFragment.show(getSupportFragmentManager(), FusenListDialogFragment.class.getSimpleName());
            }

            @Override
            public void onTrashClick(final int position) {
                int fetchedAdsCnt = mBannerRecyclerAdapterWrapper.getFetchedAdsCount();
                int sourceCnt = mBannerRecyclerAdapterWrapper.getAdapter().getItemCount();
                globalMgr.mCurrentCardIndex = mAdapterCalc.getOriginalContentPosition(position, fetchedAdsCnt, sourceCnt);
                card = mCardLinkedList.get(globalMgr.mCurrentCardIndex);

                new AlertDialog.Builder(mActivity)
                        .setIcon(IconManager.getResIdAtRandom(globalMgr.mCategory))
                        .setMessage(R.string.dlg_msg_delete_card)
                        .setTitle(R.string.dlg_title_delete_confirm)
                        .setPositiveButton(
                                R.string.delete,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        LogUtility.d("[削除]が選択されました");

                                        // LinkedListから削除
                                        mCardLinkedList.remove(globalMgr.mCurrentCardIndex);

                                        // Folderで管理しているカード数などを更新
                                        globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).decrementNumOfAllCards();
                                        if (card.isLearned())
                                            globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).decrementNumOfLearnedCards();

                                        // DBに反映
                                        FolderDao folderDao = new FolderDao(getApplicationContext());
                                        CardDao cardDao = new CardDao(getApplicationContext());
                                        folderDao.update(globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex));
                                        cardDao.deleteByCardId(card.getId());

                                        globalMgr.mCardStatsChanged = true;

                                        mRecyclerViewAdapter.notifyItemRemoved(globalMgr.mCurrentCardIndex);
                                    }
                                })
                        .setNegativeButton(
                                R.string.no_delete,
                                (dialog, which) -> {
                                    LogUtility.d("[キャンセル]が選択されました");
                                    // このAlertDialogだけが消え、親のダイアログ表示状態に戻る
                                })
                        .show();
            }

        });
    }

    // 以下は、AdMobの無い純正処理の場合で、現在は未使用
    /**
     * 　カード一枚毎の中に含まれる各種ViewのonClickリスナなどの登録処理（Override）
     * 　onClickのイベント自体はRecyclerViewのAdapterであるFolderRecyclerViewAdapterの方で検出され、こちらに通知される
     */
//    public void buildRecyclerView() {
//        mRecyclerView = findViewById(R.id.my_recycler_view);
//        mLayoutManager = new LinearLayoutManager(this);        //RecyclerView内の表示形式にLinearLayoutを指定
//        mRecyclerViewAdapter = new FolderRecyclerViewAdapter(mCardLinkedList);
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        mRecyclerView.setAdapter(mRecyclerViewAdapter);
//
//        // リスナーの実装
//        mRecyclerViewAdapter.setOnItemClickListener(new FolderRecyclerViewAdapter.OnItemClickListener() {
//            CardModel card;
//
//            @Override
//            public void onFrontClick(int position) {
//                globalMgr.mCurrentCardIndex = position;
//
//                // 編集モードでダイアログを開く
//                card = mCardLinkedList.get(position);
//                CardSettingsDialogFragment cardSettingsDialogFragment = new CardSettingsDialogFragment(mRecyclerViewAdapter, card, position);
//                Bundle args = new Bundle();
//                args.putInt(Constants.CARD_SETTINGS_DIALOG_ARG_KEY_MODE, Constants.CARD_SETTINGS_FOR_EDIT);
//                cardSettingsDialogFragment.setArguments(args);
//                cardSettingsDialogFragment.show(getSupportFragmentManager(), FolderSettingsDialogFragment.class.getSimpleName());
//            }
//
//
//            @Override
//            public void onLearnedClick(int position) {
//                card = mCardLinkedList.get(position);
//
//                // 現状LearnedがOnだったらOffに、OffだったらOnにセットする
//                if (card.isLearned()) {
//                    card.setLearned(false);
//                    globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).decrementNumOfLearnedCards();
//                } else {
//                    card.setLearned(true);
//                    globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).incrementNumOfLearnedCards();
//                }
//
//                // DBに反映
//                FolderDao folderDao = new FolderDao(getApplicationContext());
//                CardDao cardDao = new CardDao(getApplicationContext());
//                folderDao.update(globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex));
//                cardDao.update(card);
//
//                // 変更を表示に反映させる
//                mRecyclerViewAdapter.notifyItemChanged(position);
//
//                globalMgr.mCardStatsChanged = true;         // Folder一覧表示時のリフレッシュ動作で参照
//            }
//
//            @Override
//            public void onFusenClick(int position) {
//                card = mCardLinkedList.get(position);
//
//                // 現状FusenがOnだったらOffに、OffだったらOnにセットする
//                if (card.isFusenTag()) {
//                    card.setFusenTag(false);
//                } else {
//                    card.setFusenTag(true);
//                }
//                // 変更を表示に反映させる
//                mRecyclerViewAdapter.notifyItemChanged(position);
//            }
//
//            @Override
//            public void onTrashClick(final int position) {
//                card = mCardLinkedList.get(position);
//
//                new AlertDialog.Builder(mActivity)
//                        .setIcon(IconManager.getResIdAtRandom(globalMgr.mCategory))
//                        .setTitle(R.string.dlg_title_delete_confirm)
//                        .setMessage(R.string.dlg_msg_delete_card)
//                        .setPositiveButton(
//                                R.string.delete,
//                                new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        LogUtility.d("[削除]が選択されました");
//
//                                        // LinkedListから削除
//                                        mCardLinkedList.remove(position);
//
//                                        // Folderで管理しているカード数などを更新
//                                        globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).decrementNumOfAllCards();
//                                        if (card.isLearned())
//                                            globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).decrementNumOfLearnedCards();
//
//                                        // DBに反映
//                                        FolderDao folderDao = new FolderDao(getApplicationContext());
//                                        CardDao cardDao = new CardDao(getApplicationContext());
//                                        folderDao.update(globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex));
//                                        cardDao.deleteByCardId(card.getId());
//
//                                        globalMgr.mCardStatsChanged = true;
//
//                                        mRecyclerViewAdapter.notifyItemRemoved(position);
//                                    }
//                                })
//                        .setNegativeButton(
//                                R.string.cancel,
//                                new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        LogUtility.d("[キャンセル]が選択されました");
//                                        // このAlertDialogだけが消え、親のダイアログ表示状態に戻る
//                                    }
//                                })
//                        .show();
//
//            }
//
//        });
//    }

}
