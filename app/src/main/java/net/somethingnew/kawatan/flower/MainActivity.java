package net.somethingnew.kawatan.flower;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ShareCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.clockbyte.admobadapter.AdmobAdapterCalculator;
import com.clockbyte.admobadapter.AdmobRecyclerAdapterWrapper;
import com.clockbyte.admobadapter.bannerads.AdmobBannerRecyclerAdapterWrapper;
import com.clockbyte.admobadapter.bannerads.BannerAdViewWrappingStrategyBase;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.navigation.NavigationView;

import net.somethingnew.kawatan.flower.db.dao.CardDao;
import net.somethingnew.kawatan.flower.db.dao.FolderDao;
import net.somethingnew.kawatan.flower.model.CardModel;
import net.somethingnew.kawatan.flower.model.FolderModel;
import net.somethingnew.kawatan.flower.util.LogUtility;

import java.util.LinkedList;

/**
 * Main画面：主なコンテンツはFolder一覧
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Context mContext;
    private Activity mActivity;
    private LayoutInflater mInflater;
    private GlobalManager globalMgr = GlobalManager.getInstance();
    private SearchView mSearchView;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MainRecyclerViewAdapter mRecyclerViewAdapter;
    private AdmobRecyclerAdapterWrapper mAdapterWrapper;
    private AdmobBannerRecyclerAdapterWrapper mBannerRecyclerAdapterWrapper;
    private AdmobAdapterCalculator mAdapterCalc;

    private ImageView mDrawerHeaderImageView1;
    private ImageView mDrawerHeaderImageView2;
    private ImageView mDrawerHeaderImageView3;
    private ImageView mDrawerHeaderImageView4;

    private TextView mTitle;
    private Toolbar mToolbar;
    private CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtility.d("onCreate");

        setContentView(R.layout.activity_main);

        // Get the application context
        mContext = getApplicationContext();
        mActivity = MainActivity.this;

        mInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 標準のActionBarの代わりにToolbarをActionBarとしてセットして利用する
        mToolbar = findViewById(R.id.main_toolbar);
        mCoordinatorLayout = findViewById(R.id.cordinatorLayoutMain);
        mTitle = findViewById(R.id.toolbar_title);
        mToolbar.setBackground(new ColorDrawable(globalMgr.skinHeaderColor));
        mCoordinatorLayout.setBackground(new ColorDrawable(globalMgr.skinBodyColor));
        mTitle.setText(Constants.CATEGORY_NAME[globalMgr.mCategory] + " Version");

        setSupportActionBar(mToolbar);

        // ハンバーガーメニューの準備
        setDrawer(mToolbar);

        // FloatingActionButton
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 新規登録モードでダイアログを開く
                FolderSettingsDialogFragment folderSettingsDialogFragment = new FolderSettingsDialogFragment(mRecyclerViewAdapter);
                Bundle args = new Bundle();
                args.putInt(Constants.FOLDER_SETTINGS_DIALOG_ARG_KEY_MODE, Constants.FOLDER_SETTINGS_FOR_NEW);
                folderSettingsDialogFragment.setArguments(args);
                folderSettingsDialogFragment.show(getSupportFragmentManager(),
                        FolderSettingsDialogFragment.class.getSimpleName());
            }
        });

        // テストデータ投入
        //createExampleList();

        MobileAds.initialize(mContext, BuildConfig.ADMOB_APPLICATION_ID);

        //buildRecyclerView();
        //buildRecyclerViewWithAdWrapper();
        buildRecyclerViewWithBannerRecyclerWrapper();

        //setBottomBannerAdView();

        // Drag & Drop Handling
        // DB反映処理があまりにもコスト高なので、Drag&Dropはやめて、Swipのみ対応する
        // ItemTouchHelper.UP | ItemTouchHelper.DOWN
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

                    @Override
                    public boolean onMove(@NonNull RecyclerView mRecyclerView,
                                          @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {

                    /* Drag&Dropはやらない
                    final int fromPos   = viewHolder.getAdapterPosition();
                    final int toPos     = target.getAdapterPosition();
                    mRecyclerViewAdapter.notifyItemMoved(fromPos, toPos);

                    TextView textViewName   = viewHolder.itemView.findViewById(R.id.textViewTitleName);
                    String selectedName     = textViewName.toString();
                    LogUtility.d("fromPos: " + fromPos + " toPos: " + toPos + " name: " + selectedName);

                    // fromPosとtoPosは隣同士を指している想定だが、とりあえずswap()を使って入れ替える
                    Collections.swap(globalMgr.mFolderLinkedList, fromPos, toPos);

                    // 更新範囲を保持しておく
                    if (globalMgr.mOrderChangedStartFolderIndex > fromPos) globalMgr.mOrderChangedStartFolderIndex = fromPos;
                    if (globalMgr.mOrderChangedEndFolderIndex < toPos) globalMgr.mOrderChangedEndFolderIndex = toPos;

                    // TODO orderの付け替え処理とそれのDBへの反映が必要・・・

                     */

                        return true;// true if moved, false otherwise
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        //final int swipedPosition = viewHolder.getAdapterPosition();
                        int fetchedAdsCnt = mBannerRecyclerAdapterWrapper.getFetchedAdsCount();
                        int sourceCnt = mBannerRecyclerAdapterWrapper.getAdapter().getItemCount();
                        final int swipedPosition = mAdapterCalc.getOriginalContentPosition(viewHolder.getAdapterPosition(), fetchedAdsCnt, sourceCnt);

                        new AlertDialog.Builder(mActivity)
                                .setIcon(IconManager.getResIdAtRandom(globalMgr.mCategory))
                                .setTitle(R.string.dlg_title_delete_confirm)
                                .setMessage(R.string.dlg_msg_delete_folder)
                                .setPositiveButton(
                                        R.string.delete,
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                LogUtility.d("[削除]が選択されました");

                                                // Card関連
                                                //   LinkedList自体の削除
                                                //   管理しているmapから削除
                                                //   CARD_TBLからの削除
                                                String folderId = globalMgr.mFolderLinkedList.get(swipedPosition).getId();
                                                globalMgr.mCardListMap.get(folderId).clear();
                                                globalMgr.mCardListMap.remove(folderId);
                                                CardDao cardDao = new CardDao(getApplicationContext());
                                                cardDao.deleteByFolderId(folderId);

                                                // FolderのLinkedListから削除
                                                globalMgr.mFolderLinkedList.remove(swipedPosition);
                                                FolderDao folderDao = new FolderDao(getApplicationContext());
                                                folderDao.deleteByFolderId(folderId);

                                                // 削除したFolderの後ろのorderを更新する
                                                for (int index = swipedPosition; index < globalMgr.mFolderLinkedList.size(); index++) {
                                                    FolderModel folder = globalMgr.mFolderLinkedList.get(index);
                                                    folder.setOrder(index);
                                                    folderDao.update(folder);
                                                }

                                                // 再表示の通知
                                                mRecyclerViewAdapter.notifyDataSetChanged();
                                            }
                                        })
                                .setNegativeButton(
                                        R.string.cancel,
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                LogUtility.d("[キャンセル]が選択されました");
                                                // Swipeで消えたものを再描画するため
                                                mRecyclerViewAdapter.notifyDataSetChanged();
                                            }
                                        })
                                .show();
                    }

                });
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtility.d("onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtility.d("onResume");
        // Card数の増減などが発生していた場合は、リストを再表示する
        if (globalMgr.mCardStatsChanged) mRecyclerViewAdapter.notifyDataSetChanged();
        globalMgr.mCardStatsChanged = false;

        mBannerRecyclerAdapterWrapper.resumeAll();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtility.d("onPause");
        mBannerRecyclerAdapterWrapper.pauseAll();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtility.d("onDestroy");
        mBannerRecyclerAdapterWrapper.release();
    }

    /**
     * 画面下部のバナー広告
     */
    private void setBottomBannerAdView() {
        LogUtility.d("setBottomBannerAdView...");

        // バナー広告
        RelativeLayout mRelativeLayout = findViewById(R.id.main_relative_layout);
        AdView adView = new AdView(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        adView.setLayoutParams(params);
        mRelativeLayout.addView(adView);
        adView.setAdSize(AdSize.BANNER);      // 320×50
        //adView.setAdSize(AdSize.SMART_BANNER);  // 画面の幅×50
        adView.setAdUnitId(BuildConfig.ADMOB_BANNER_UNIT_ID_MAIN);

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
     * RecyclerView関連準備
     * AdMobを入れないRecyclerViewの場合
     */
    public void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerViewAdapter = new MainRecyclerViewAdapter(globalMgr.mFolderLinkedList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mRecyclerViewAdapter);

        mRecyclerViewAdapter.setOnItemClickListener(new MainRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                globalMgr.mCurrentFolderIndex = position;
                Intent intent = new Intent();
                intent.setClass(mContext, FolderActivity.class);
                startActivity(intent);
            }

            @Override
            public void onIconClick(int position) {
                globalMgr.mCurrentFolderIndex = position;

                // ダイアログ表示（ダイアログ内の操作でItemの変更が発生するのでAdapterを渡しておき、notifyDataSetChanged()を呼べるようにする
                FolderSettingsDialogFragment folderSettingsDialogFragment = new FolderSettingsDialogFragment(mRecyclerViewAdapter);
                Bundle args = new Bundle();
                args.putInt(Constants.FOLDER_SETTINGS_DIALOG_ARG_KEY_MODE, Constants.FOLDER_SETTINGS_FOR_EDIT);
                folderSettingsDialogFragment.setArguments(args);
                folderSettingsDialogFragment.show(getSupportFragmentManager(), FolderSettingsDialogFragment.class.getSimpleName());
            }

            @Override
            public void onExerciseClick(int position) {
                globalMgr.mCurrentFolderIndex = position;
                Intent intent = new Intent();
                intent.putExtra(Constants.EXERCISE_MODE_KEY_NAME, Constants.EXERCISE_MODE_NORMAL);
                intent.setClass(mContext, ExerciseActivity.class);
                startActivity(intent);
            }

            @Override
            public void onShuffleExerciseClick(int position) {
                globalMgr.mCurrentFolderIndex = position;
                Intent intent = new Intent();
                intent.putExtra("EXERCISE_MODE", Constants.EXERCISE_MODE_SHUFFLE);
                intent.setClass(mContext, ExerciseActivity.class);
                startActivity(intent);
            }

        });
    }

    /**
     * RecyclerView関連準備
     * AdmobBannerRecyclerAdapterWrapper()を使ってのNativeAdの表示
     */
    public void buildRecyclerViewWithBannerRecyclerWrapper() {
        mRecyclerView = findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerViewAdapter = new MainRecyclerViewAdapter(globalMgr.mFolderLinkedList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        String[] testDevicesIds = new String[]{BuildConfig.TEST_DEVICE_ID, AdRequest.DEVICE_ID_EMULATOR};
        mBannerRecyclerAdapterWrapper = AdmobBannerRecyclerAdapterWrapper.builder(this)
                .setLimitOfAds(Constants.ADMOB_AD_LIMITS)
                .setFirstAdIndex(Constants.ADMOB_FIRST_AD_INDEX)
                .setNoOfDataBetweenAds(Constants.ADMOB_NUM_OF_DATA_BETWEEN_ADS)
                .setTestDeviceIds(testDevicesIds)
                .setAdapter((RecyclerView.Adapter) mRecyclerViewAdapter)
                //.setSingleAdSize(new AdSize((int)dpWidth-10, 50))
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
        mRecyclerViewAdapter.setOnItemClickListener(new MainRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                int fetchedAdsCnt = mBannerRecyclerAdapterWrapper.getFetchedAdsCount();
                int sourceCnt = mBannerRecyclerAdapterWrapper.getAdapter().getItemCount();
                globalMgr.mCurrentFolderIndex = mAdapterCalc.getOriginalContentPosition(position, fetchedAdsCnt, sourceCnt);
                LogUtility.d("onItemClick position: " + position + " originalPosition:" + globalMgr.mCurrentFolderIndex);

                Intent intent = new Intent();
                intent.setClass(mContext, FolderActivity.class);
                startActivity(intent);
            }

            @Override
            public void onIconClick(int position) {
                int fetchedAdsCnt = mBannerRecyclerAdapterWrapper.getFetchedAdsCount();
                int sourceCnt = mBannerRecyclerAdapterWrapper.getAdapter().getItemCount();
                globalMgr.mCurrentFolderIndex = mAdapterCalc.getOriginalContentPosition(position, fetchedAdsCnt, sourceCnt);
                LogUtility.d("onIconClick position: " + position + " originalPosition:" + globalMgr.mCurrentFolderIndex);

                // ダイアログ表示（ダイアログ内の操作でItemの変更が発生するのでAdapterを渡しておき、notifyDataSetChanged()を呼べるようにする
                FolderSettingsDialogFragment folderSettingsDialogFragment = new FolderSettingsDialogFragment(mRecyclerViewAdapter);
                Bundle args = new Bundle();
                args.putInt(Constants.FOLDER_SETTINGS_DIALOG_ARG_KEY_MODE, Constants.FOLDER_SETTINGS_FOR_EDIT);
                folderSettingsDialogFragment.setArguments(args);
                folderSettingsDialogFragment.show(getSupportFragmentManager(), FolderSettingsDialogFragment.class.getSimpleName());
            }

            @Override
            public void onExerciseClick(int position) {
                int fetchedAdsCnt = mBannerRecyclerAdapterWrapper.getFetchedAdsCount();
                int sourceCnt = mBannerRecyclerAdapterWrapper.getAdapter().getItemCount();
                globalMgr.mCurrentFolderIndex = mAdapterCalc.getOriginalContentPosition(position, fetchedAdsCnt, sourceCnt);
                LogUtility.d("onExerciseClick position: " + position + " originalPosition:" + globalMgr.mCurrentFolderIndex);

                Intent intent = new Intent();
                intent.putExtra(Constants.EXERCISE_MODE_KEY_NAME, Constants.EXERCISE_MODE_NORMAL);
                intent.setClass(mContext, ExerciseActivity.class);
                startActivity(intent);
            }

            @Override
            public void onShuffleExerciseClick(int position) {
                int fetchedAdsCnt = mBannerRecyclerAdapterWrapper.getFetchedAdsCount();
                int sourceCnt = mBannerRecyclerAdapterWrapper.getAdapter().getItemCount();
                globalMgr.mCurrentFolderIndex = mAdapterCalc.getOriginalContentPosition(position, fetchedAdsCnt, sourceCnt);
                LogUtility.d("onShuffleExerciseClick position: " + position + " originalPosition:" + globalMgr.mCurrentFolderIndex);

                Intent intent = new Intent();
                intent.putExtra("EXERCISE_MODE", Constants.EXERCISE_MODE_SHUFFLE);
                intent.setClass(mContext, ExerciseActivity.class);
                startActivity(intent);
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem myActionMenuItem = menu.findItem(R.id.menu_search);
        mSearchView = (SearchView) myActionMenuItem.getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String searchWord) {
                // なぜか2回呼ばれてしまうことへの回避策
                mSearchView.clearFocus();

                if (searchWord != null && !searchWord.equals("")) {
                    LogUtility.d("onQueryTextSubmit searchWord: " + searchWord);
                    Intent intent = new Intent();
                    intent.putExtra(Constants.SEARCH_WORD_KEY_NAME, searchWord);
                    intent.setClass(mContext, FolderSearchResultActivity.class);
                    startActivity(intent);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //LogUtility.d("onQueryTextChange : " + s);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        return true;
    }

    /**
     * 横スライドメニュー（ハンバーガーメニュー）の表示
     */
    private void setDrawer(Toolbar toolbar) {
        // DrawerToggle
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayoutMain);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerOpened(View drawerView) {
                if (mDrawerHeaderImageView1 == null) {
                    mDrawerHeaderImageView1 = findViewById(R.id.igvFlower1);
                    mDrawerHeaderImageView2 = findViewById(R.id.igvFlower2);
                    mDrawerHeaderImageView3 = findViewById(R.id.igvFlower3);
                    mDrawerHeaderImageView4 = findViewById(R.id.igvFlower4);

                    mDrawerHeaderImageView1.setImageResource(IconManager.getResIdAtRandom(globalMgr.mCategory));
                    mDrawerHeaderImageView2.setImageResource(IconManager.getResIdAtRandom(globalMgr.mCategory));
                    mDrawerHeaderImageView3.setImageResource(IconManager.getResIdAtRandom(globalMgr.mCategory));
                    mDrawerHeaderImageView4.setImageResource(IconManager.getResIdAtRandom(globalMgr.mCategory));
                }

                // 描画指示
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerClosed(View drawerView) {
                // Openの際に画像を入れ替えると動きがぎこちないので、Closeしたときに次に表示する画像を設定しておく
                mDrawerHeaderImageView1.setImageResource(IconManager.getResIdAtRandom(globalMgr.mCategory));
                mDrawerHeaderImageView2.setImageResource(IconManager.getResIdAtRandom(globalMgr.mCategory));
                mDrawerHeaderImageView3.setImageResource(IconManager.getResIdAtRandom(globalMgr.mCategory));
                mDrawerHeaderImageView4.setImageResource(IconManager.getResIdAtRandom(globalMgr.mCategory));
            }

        };

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        // DrawerLayoutだけでもメニューは作れるが、表現をよりリッチにするためにNavigationViewを入れる。
        // NavigationViewでのItemが選択したときのリスナを設定する
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
    }


    /**
     * 横スライドメニュー内の項目選択時の処理ハンドラ
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.naviItemSkinDesign:
                SkinSettingsDialogFragment skinSettingsDialogFragment = new SkinSettingsDialogFragment();
                skinSettingsDialogFragment.setOnSkinChangeListener(() -> {
                    // We are going to reflect new skin color.
                    mToolbar.setBackground(new ColorDrawable(globalMgr.skinHeaderColor));
                    mCoordinatorLayout.setBackground(new ColorDrawable(globalMgr.skinBodyColor));
                });
                skinSettingsDialogFragment.show(getSupportFragmentManager(), SkinSettingsDialogFragment.class.getSimpleName());
                break;
            case R.id.naviItemVersion:
                VersionSettingsDialogFragment versionSettingsDialogFragment = new VersionSettingsDialogFragment();
                versionSettingsDialogFragment.setOnVersionChangeListener(() -> {
                    // We are going to reflect new version name for page title.
                    mTitle.setText(Constants.CATEGORY_NAME[globalMgr.mCategory] + " Version");
                    mDrawerHeaderImageView1.setImageResource(IconManager.getResIdAtRandom(globalMgr.mCategory));
                    mDrawerHeaderImageView2.setImageResource(IconManager.getResIdAtRandom(globalMgr.mCategory));
                    mDrawerHeaderImageView3.setImageResource(IconManager.getResIdAtRandom(globalMgr.mCategory));
                    mDrawerHeaderImageView4.setImageResource(IconManager.getResIdAtRandom(globalMgr.mCategory));
                });
                versionSettingsDialogFragment.show(getSupportFragmentManager(), VersionSettingsDialogFragment.class.getSimpleName());
                break;
            case R.id.naviItemImportData:
                intent = new Intent();
                intent.setClass(mContext, ImportDataActivity.class);
                startActivity(intent);
                break;
            case R.id.naviItemExportData:
                showBackupPolicyDialog();
                break;
            case R.id.naviItemHowToWeb:
                break;
            case R.id.naviItemInfo:
                intent = new Intent();
                intent.setClass(mContext, GeneralWebViewActivity.class);
                intent.putExtra("target_uri", Constants.EXTERNAL_LINK_URL_KAWATN_INFO);
                startActivity(intent);
                break;
            case R.id.naviItemQuestionUs:
                String[] toAddress = {Constants.MAIL_TO_ADDRESS};  // 複数のアドレスを入れらる
                intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, toAddress);
                intent.putExtra(Intent.EXTRA_SUBJECT, Constants.MAIL_SUBJECT);
                intent.putExtra(Intent.EXTRA_TEXT, Constants.MAIL_BODY);

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                break;
            case R.id.naviItemTerms:
                intent = new Intent();
                intent.setClass(mContext, GeneralWebViewActivity.class);
                intent.putExtra("target_uri", Constants.EXTERNAL_LINK_URL_KAWATN_TERMS);
                startActivity(intent);
                break;
            case R.id.naviItemPrivacyPolicy:
                intent = new Intent();
                intent.setClass(mContext, GeneralWebViewActivity.class);
                intent.putExtra("target_uri", Constants.EXTERNAL_LINK_URL_KAWATN_PRIVACY_POLICY);
                startActivity(intent);
                break;
            case R.id.naviItemReviewUs:
                intent = new Intent();
                intent.setClass(mContext, GeneralWebViewActivity.class);
                intent.putExtra("target_uri", Constants.EXTERNAL_LINK_URL_PLAY_STORE);
                startActivity(intent);
                break;
            case R.id.naviItemShareUs:
                ShareCompat.IntentBuilder builder = ShareCompat.IntentBuilder.from(mActivity);
                builder.setChooserTitle("シェアするアプリ")
                        .setSubject("かわいい単語帳【かわたん】")
                        .setText("紹介メッセージ")
                        .setType("text/plain")
                        .startChooser();
                break;
            case R.id.naviItemAboutIcon:
                AboutIconImageDialogFragment aboutIconImageDialogFragment = new AboutIconImageDialogFragment();
                aboutIconImageDialogFragment.show(getSupportFragmentManager(), AboutIconImageDialogFragment.class.getSimpleName());
                break;
            case R.id.naviItemAboutUs:
                break;
        }

        // NavigationDrawerを消す
        DrawerLayout drawer = findViewById(R.id.drawerLayoutMain);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * バックアップポリシーをダイアログ表示
     */
    public void showBackupPolicyDialog() {
        new AlertDialog.Builder(mActivity)
                .setIcon(IconManager.getResIdAtRandom(globalMgr.mCategory))
                .setTitle(R.string.dlg_title_backup)
                .setMessage(R.string.dlg_msg_backup_policy)
                .setPositiveButton(
                        R.string.close,
                        (dialog, which) -> {
                            return;
                        }
                )
                .show();
    }

    /**
     * RecyclerView内の各CardViewをクリックしたときのハンドラ
     * （ただし、CardView内の画像アイコンのクリック時は別のIconOnClickListenerで処理する）
     *
     * 選択されたFolderのCard一覧画面（FolderActivity）へ遷移する
     *
     * 参考にしたコードでは以下のFolderOnClickListenerはstaticメソッドで作成していたが、それだと
     * この中からnon-staticなメソッドの呼び出し（例えば、startActivity()などの呼び出しができないので
     * FolderOnClickListenerはNon-staticのクラスに変更し、これを呼び出すMyRecyclerViewAdapterには、それをnewした時点で
     * インスタンスを渡しておくことにする
     */
    /* ------------------ interfaceの実装による方法に変更したので、以下は未使用 --------------------------
    private class FolderOnClickListener implements View.OnClickListener {
        private final Context context;

        private FolderOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            int selectedItemPositionInAdapter   = mRecyclerView.getChildAdapterPosition(v);
            int selectedItemPositionInLayout    = mRecyclerView.getChildLayoutPosition(v);
            RecyclerView.ViewHolder viewHolder  = mRecyclerView.getChildViewHolder(v);
            TextView textViewName               = viewHolder.itemView.findViewById(R.id.textViewTitleName);
            String selectedName                 = (String)textViewName.getText();
            LogUtility.d("selectedItemPositionInAdapter: " + selectedItemPositionInAdapter);
            LogUtility.d("selectedItemPositionInLayout: " + selectedItemPositionInLayout);
            LogUtility.d("selectedName: " + selectedName);

            // どのデータがクリックされたのかは、selectedItemPositionInAdapterを頼りにglobalMgr.mFolderLinkedListの該当Indexを調べればいい。
            // あるいは、viewHolderのImageViewにFolderDataModelのkeyIdをタグ付けしてあるので
            // それをもとにSQLiteを検索する、とかでもいいのだが、とりあえず前者で。
            globalMgr.mCurrentFolderIndex       = selectedItemPositionInAdapter;

            Intent intent = new Intent();
            intent.setClass(context, FolderActivity.class);
            startActivity(intent);
        }
    }
    */

    /**
     * Imageアイコンをクリックしたときのハンドラ
     * FolderSettingsのダイアログを開く
     */
     /* ------------------ interfaceの実装による方法に変更したので、以下は未使用 --------------------------
    private class IconOnClickListener implements View.OnClickListener {
        private final Context context;

        private IconOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            // どのImageアイコンがクリックされたかを識別するためにViewにTag付けされたFolderのidを引数にセットして編集モードでダイアログを開く
            FolderSettingsDialogFragment folderSettingsDialogFragment = new FolderSettingsDialogFragment(mRecyclerViewAdapter);
            Bundle args     = new Bundle();
            args.putInt(Constants.FOLDER_SETTINGS_DIALOG_ARG_KEY_MODE, Constants.FOLDER_SETTINGS_FOR_EDIT);
            args.putString(Constants.FOLDER_SETTINGS_DIALOG_ARG_KEY_FOLDER_ID, v.getTag().toString());
            folderSettingsDialogFragment.setArguments(args);
            folderSettingsDialogFragment.show(getSupportFragmentManager(), FolderSettingsDialogFragment.class.getSimpleName());

            // tagをたよりにmFolderLinkedList上のpositionを求め、globalMgrに持たせておく
            for (int i = 0; i < globalMgr.mFolderLinkedList.size(); i++) {
                if (globalMgr.mFolderLinkedList.get(i).getId().equals((String)v.getTag())) {
                    globalMgr.mCurrentFolderIndex = i;
                    break;
                }
            }
        }
    }

      */

}
