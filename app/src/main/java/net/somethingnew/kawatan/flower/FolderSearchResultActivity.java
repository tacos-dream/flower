package net.somethingnew.kawatan.flower;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.somethingnew.kawatan.flower.db.dao.CardDao;
import net.somethingnew.kawatan.flower.db.dao.FolderDao;
import net.somethingnew.kawatan.flower.model.FolderModel;
import net.somethingnew.kawatan.flower.util.LogUtility;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Main画面：主なコンテンツはFolder一覧
 */
public class FolderSearchResultActivity extends AppCompatActivity
{
    private Context                         mContext;
    private Activity                        mActivity;
    private GlobalManager                   globalMgr = GlobalManager.getInstance();
    private RecyclerView                    mRecyclerView;
    private RecyclerView.LayoutManager      mLayoutManager;
    private MainRecyclerViewAdapter         mRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String searchWord                           = getIntent().getStringExtra(Constants.SEARCH_WORD_KEY_NAME);
        FolderDao folderDao                         = new FolderDao(getApplicationContext());
        ArrayList<FolderModel> folderModelArrayList = folderDao.selectByTitle(searchWord);
        globalMgr.mSearchedFolderLinkedList         = new LinkedList<>(folderModelArrayList);
        LogUtility.d("Searched Folder counts: " + globalMgr.mSearchedFolderLinkedList.size());

        setContentView(R.layout.activity_folder_search_result);

        // Get the application context
        mContext        = getApplicationContext();
        mActivity       = FolderSearchResultActivity.this;

        // 標準のActionBarの代わりにToolbarをActionBarとしてセットして利用する
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        TextView title  = findViewById(R.id.toolbar_title);
        title.setText(R.string.app_name);
        setSupportActionBar(toolbar);

        buildRecyclerView();

        findViewById(R.id.buttonGoBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Drag & Drop Handling
        // DB反映処理があまりにもコスト高なので、Drag&Dropはやめて、Swipのみ対応する
        // ItemTouchHelper.UP | ItemTouchHelper.DOWN
        ItemTouchHelper itemTouchHelper  = new ItemTouchHelper(
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

                @Override
                public boolean onMove(@NonNull RecyclerView mRecyclerView,
                                      @NonNull RecyclerView.ViewHolder viewHolder,
                                      @NonNull RecyclerView.ViewHolder target) {
                    return true;// true if moved, false otherwise
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    final int swipedPosition = viewHolder.getAdapterPosition();

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
                                            String folderId     = globalMgr.mFolderLinkedList.get(swipedPosition).getId();
                                            globalMgr.mCardListMap.get(folderId).clear();
                                            globalMgr.mCardListMap.remove(folderId);
                                            CardDao cardDao     = new CardDao(getApplicationContext());
                                            cardDao.deleteByFolderId(folderId);

                                            // Folderのorderの付け替え処理は不要（Drag&Dropをやめたので）

                                            // FolderのLinkedListから削除
                                            globalMgr.mFolderLinkedList.remove(swipedPosition);
                                            FolderDao folderDao     = new FolderDao(getApplicationContext());
                                            folderDao.deleteByFolderId(folderId);

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtility.d("onDestroy");
    }

    /**
     * RecyclerView関連準備
     */
    public void buildRecyclerView() {
        mRecyclerView               = findViewById(R.id.my_recycler_view);
        mLayoutManager              = new LinearLayoutManager(this);
        mRecyclerViewAdapter        = new MainRecyclerViewAdapter(globalMgr.mSearchedFolderLinkedList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mRecyclerViewAdapter);

        mRecyclerViewAdapter.setOnItemClickListener(new MainRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // クリックされたItemのpositionはSearch結果だけのLinkedList上のpositionなので、全LinkedList上の位置に変換する
                globalMgr.mCurrentFolderIndex       = globalMgr.mSearchedFolderLinkedList.get(position).getOrder();
                Intent intent = new Intent();
                intent.setClass(mContext, CardListActivity.class);
                startActivity(intent);
            }

            @Override
            public void onIconClick(int position) {
                // クリックされたItemのpositionはSearch結果だけのLinkedList上のpositionなので、全LinkedList上の位置に変換する
                globalMgr.mCurrentFolderIndex       = globalMgr.mSearchedFolderLinkedList.get(position).getOrder();

                // ダイアログ表示（ダイアログ内の操作でItemの変更が発生するのでAdapterを渡しておき、notifyDataSetChanged()を呼べるようにする
                FolderSettingsDialogFragment folderSettingsDialogFragment = new FolderSettingsDialogFragment(mRecyclerViewAdapter);
                Bundle args     = new Bundle();
                args.putInt(Constants.FOLDER_SETTINGS_DIALOG_ARG_KEY_MODE, Constants.FOLDER_SETTINGS_FOR_EDIT);
                folderSettingsDialogFragment.setArguments(args);
                folderSettingsDialogFragment.show(getSupportFragmentManager(), FolderSettingsDialogFragment.class.getSimpleName());
            }

            @Override
            public void onExerciseClick(int position) {
                // クリックされたItemのpositionはSearch結果だけのLinkedList上のpositionなので、全LinkedList上の位置に変換する
                globalMgr.mCurrentFolderIndex       = globalMgr.mSearchedFolderLinkedList.get(position).getOrder();
                Intent intent = new Intent();
                intent.putExtra(Constants.EXERCISE_MODE_KEY_NAME, Constants.EXERCISE_MODE_NORMAL);
                intent.setClass(mContext, ExerciseActivity.class);
                startActivity(intent);
            }

            @Override
            public void onShuffleExerciseClick(int position) {
                // クリックされたItemのpositionはSearch結果だけのLinkedList上のpositionなので、全LinkedList上の位置に変換する
                globalMgr.mCurrentFolderIndex       = globalMgr.mSearchedFolderLinkedList.get(position).getOrder();
                Intent intent = new Intent();
                intent.putExtra("EXERCISE_MODE", Constants.EXERCISE_MODE_SHUFFLE);
                intent.setClass(mContext, ExerciseActivity.class);
                startActivity(intent);
            }

        });
    }

}
