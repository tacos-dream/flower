package net.somethingnew.kawatan.flower;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import net.somethingnew.kawatan.flower.model.CardModel;
import net.somethingnew.kawatan.flower.model.FolderModel;
import net.somethingnew.kawatan.flower.util.LogUtility;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Main画面：主なコンテンツはFolder一覧
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    private Context             mContext;
    private Activity            mActivity;
    GlobalManager               globalMgr = GlobalManager.getInstance();

    private SearchView          mSearchView;

    private static RecyclerView.Adapter             mRecyclerViewAdapter;
    private static RecyclerView                     mRecyclerView;

    private RecyclerView.LayoutManager              mLayoutManager;
    View.OnClickListener                            mFolderOnClickListener;
    View.OnClickListener                            mIconOnClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtility.d("onCreate");

        setContentView(R.layout.activity_main);

        // Get the application context
        mContext        = getApplicationContext();
        mActivity       = MainActivity.this;

        // 標準のActionBarの代わりにToolbarをActionBarとしてセットして利用する
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        // ハンバーガーメニューの準備
        setDrawer(toolbar);

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 新規登録モードでダイアログを開く
                FolderSettingsDialogFragment folderSettingsDialogFragment = new FolderSettingsDialogFragment(mRecyclerViewAdapter);
                Bundle args     = new Bundle();
                args.putInt(Constants.FOLDER_SETTINGS_DIALOG_ARG_KEY_MODE, Constants.FOLDER_SETTINGS_FOR_NEW);
                folderSettingsDialogFragment.setArguments(args);
                folderSettingsDialogFragment.show(getSupportFragmentManager(),
                        FolderSettingsDialogFragment.class.getSimpleName());
            }
        });

        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // 最終的には、SQLiteから取得したFolder一覧からArrayListを生成する。
        // とりあえずは、テストデータでModelを作成
        globalMgr.mFolderLinkedList = new LinkedList<>();
        for (int i = 0; i < MyData.folderNameArray.length; i++) {
            globalMgr.mFolderLinkedList.add(new FolderModel(
                    MyData.folderNameArray[i],
                    MyData.folderDrawableArray[i]
            ));
        }

        // とりあえず、各フォルダに同じCard群テストデータをセットする
        for (int iFolder = 0; iFolder < MyData.folderNameArray.length; iFolder++) {
            String key = globalMgr.mFolderLinkedList.get(iFolder).getId();
            LinkedList<CardModel> cardLinkedList = new LinkedList<>();
            for (int iCard = 0; iCard < MyData.cardSurfaceArray.length; iCard++) {
                cardLinkedList.add(new CardModel(
                        MyData.cardSurfaceArray[iCard],
                        MyData.cardBackArray[iCard]
                ));
            }
            globalMgr.mCardListMap.put(key, cardLinkedList);
        }

        /*
        for (int iFolder = 0; iFolder < MyData.folderNameArray.length; iFolder++) {
            LinkedList<CardModel> cardLinkedList = globalMgr.mFolderLinkedList.get(iFolder).getCardLinkedList();
            for (int iCard = 0; iCard < MyData.cardSurfaceArray.length; iCard++) {
                cardLinkedList.add(new CardModel(
                        MyData.cardSurfaceArray[iCard],
                        MyData.cardBackArray[iCard]
                ));
            }
        }
         */

        /*
        // とりあえず、各フォルダに同じCard群テストデータをセットする
        for (int iFolder = 0; iFolder < MyData.folderNameArray.length; iFolder++) {
            Map<String, CardModel> cardMap = globalMgr.mFolderLinkedList.get(iFolder).getCardMap();
            for (int iCard = 0; iCard < MyData.cardSurfaceArray.length; iCard++) {
                cardMap.put(UUID.randomUUID().toString(), new CardModel(
                        MyData.cardSurfaceArray[iCard],
                        MyData.cardBackArray[iCard]
                ));
            }
        }

         */

        /**
         * Non-staticのリスナーをnewして、あとでItem（個別Folder）をクリック時に呼び出されるようAdapterに渡しておく
         */
        mFolderOnClickListener       = new FolderOnClickListener(this);
        mIconOnClickListener         = new IconOnClickListener(this);
        mRecyclerViewAdapter         = new MainRecyclerViewAdapter(mFolderOnClickListener, mIconOnClickListener);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);

        // Drag & Drop Handling
        ItemTouchHelper itemTouchHelper  = new ItemTouchHelper(
            new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN ,
                    ItemTouchHelper.LEFT) {

                @Override
                public boolean onMove(@NonNull RecyclerView mRecyclerView,
                                      @NonNull RecyclerView.ViewHolder viewHolder,
                                      @NonNull RecyclerView.ViewHolder target) {

                    final int fromPos   = viewHolder.getAdapterPosition();
                    final int toPos     = target.getAdapterPosition();
                    mRecyclerViewAdapter.notifyItemMoved(fromPos, toPos);

                    TextView textViewName   = viewHolder.itemView.findViewById(R.id.textViewTitleName);
                    String selectedName     = textViewName.toString();
                    LogUtility.d("fromPos: " + fromPos + " toPos: " + toPos + " name: " + selectedName);

                    // fromPosとtoPosは隣同士を指している想定だが、とりあえずswap()を使って入れ替える
                    Collections.swap(globalMgr.mFolderLinkedList, fromPos, toPos);

                    return true;// true if moved, false otherwise
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    final int fromPos = viewHolder.getAdapterPosition();
                    //globalMgr.mFolderLinkedList.remove(fromPos);
                    //mRecyclerViewAdapter.notifyItemRemoved(fromPos);
                }


            });
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
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

    /**
     * Imageアイコンをクリックしたときのハンドラ
     * FolderSettingsのダイアログを開く
     */
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem myActionMenuItem       = menu.findItem(R.id.menu_search);
        mSearchView                     = (SearchView)myActionMenuItem.getActionView();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String searchWord) {
                // なぜか2回呼ばれてしまうことへの回避策
                mSearchView.clearFocus();

                if (searchWord != null && !searchWord.equals("")) {
                    LogUtility.d("onQueryTextSubmit searchWord: " + searchWord);
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
        DrawerLayout drawerLayout           = findViewById(R.id.main_drawer);
        ActionBarDrawerToggle drawerToggle  = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerOpened(View drawerView) {
                // 描画指示
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        drawerLayout.addDrawerListener(drawerToggle );
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

        switch(item.getItemId()){
            case R.id.naviItemSkinDesign:
                LogUtility.d("スキンデザインが選択されました");
                break;
            case R.id.naviItemIconDesign:
                LogUtility.d("アイコンデザインが選択されました");
                break;
            case R.id.naviItemSplashDesign:
                LogUtility.d("スプラッシュデザインが選択されました");
                break;
            case R.id.naviItemImportData:
                LogUtility.d("データインポートが選択されました");
                break;
            case R.id.naviItemExportData:
                LogUtility.d("データバックアップが選択されました");
                break;
            case R.id.naviItemHowToVideo:
                LogUtility.d("操作説明動画が選択されました");
                break;
            case R.id.naviItemHowToWeb:
                LogUtility.d("操作説明ページが選択されました");
                break;
            case R.id.naviItemInfo:
                LogUtility.d("新機能／更新情報が選択されました");
                break;
            case R.id.naviItemQuestionUs:
                LogUtility.d("お問い合わせが選択されました");
                break;
            case R.id.naviItemTerms:
                LogUtility.d("利用規約が選択されました");
                break;
            case R.id.naviItemReviewUs:
                LogUtility.d("レビューするが選択されました");
                break;
            case R.id.naviItemShareUs:
                LogUtility.d("アプリをシェアするが選択されました");
                break;
            case R.id.naviItemAboutUs:
                LogUtility.d("このアプリについてが選択されました");
                break;
        }

        // NavigationDrawerを消す
        DrawerLayout drawer = findViewById(R.id.main_drawer);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
