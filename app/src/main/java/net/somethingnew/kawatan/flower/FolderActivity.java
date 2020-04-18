package net.somethingnew.kawatan.flower;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.somethingnew.kawatan.flower.model.CardModel;
import net.somethingnew.kawatan.flower.util.LogUtility;

import java.util.Collections;
import java.util.LinkedList;

/**
 * 特定Folderの詳細画面（つまり、当該Folderが保持するCard一覧画面）
 */
public class FolderActivity extends AppCompatActivity {

    private Context                         mContext;
    private Activity                        mActivity;
    private GlobalManager                   globalMgr = GlobalManager.getInstance();
    private SearchView                      mSearchView;
    private RecyclerView                    mRecyclerView;
    private FolderRecyclerViewAdapter       mRecyclerViewAdapter;
    private RecyclerView.LayoutManager      mLayoutManager;

    private LinkedList<CardModel>           mCardLinkedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        // Get the application context
        mContext        = getApplicationContext();
        mActivity       = FolderActivity.this;

        mCardLinkedList = globalMgr.mCardListMap.get(globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).getId());

        // 標準のActionBarの代わりにToolbarをActionBarとしてセットして利用する
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        buildRecyclerView();
        setupListeners();

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

                    TextView textViewName   = viewHolder.itemView.findViewById(R.id.textViewFront);
                    String selectedName     = (String) textViewName.getText();
                    LogUtility.d("fromPos: " + fromPos + " toPos: " + toPos + " name: " + selectedName);

                    // fromPosとtoPosは隣同士を指している想定だが、とりあえずswap()を使って入れ替える

                    Collections.swap(mCardLinkedList, fromPos, toPos);

                    return true;// true if moved, false otherwise
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                    final int fromPos = viewHolder.getAdapterPosition();
                    //globalMgr.mFolderArrayList.remove(fromPos);
                    //mRecyclerViewAdapter.notifyItemRemoved(fromPos);
                }
            });
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtility.d("onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtility.d("onResume");
    }

    @Override
    public void onRestart() {
        super.onRestart();
        LogUtility.d("onRestart");

        // ExerciseActivityから戻った時に習得済みや付箋の更新を反映させる
        mRecyclerViewAdapter.notifyDataSetChanged();
    }


    /**
     *
     */
    public void buildRecyclerView() {
        mRecyclerView           = findViewById(R.id.my_recycler_view);
        mLayoutManager          = new LinearLayoutManager(this);        //RecyclerView内の表示形式にLinearLayoutを指定
        mRecyclerViewAdapter    = new FolderRecyclerViewAdapter(mCardLinkedList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mRecyclerViewAdapter);

        // リスナーの実装
        mRecyclerViewAdapter.setOnItemClickListener(new FolderRecyclerViewAdapter.OnItemClickListener() {
            CardModel card;

            @Override
            public void onFrontClick(int position) {
                globalMgr.mCurrentCardIndex         = position;

                // 編集モードでダイアログを開く
                CardModel cardModel                 = mCardLinkedList.get(position);
                CardSettingsDialogFragment cardSettingsDialogFragment = new CardSettingsDialogFragment(mRecyclerViewAdapter, cardModel, position);
                Bundle args     = new Bundle();
                args.putInt(Constants.CARD_SETTINGS_DIALOG_ARG_KEY_MODE, Constants.CARD_SETTINGS_FOR_EDIT);
                cardSettingsDialogFragment.setArguments(args);
                cardSettingsDialogFragment.show(getSupportFragmentManager(), FolderSettingsDialogFragment.class.getSimpleName());
            }


            @Override
            public void onLearnedClick(int position) {
                 card = mCardLinkedList.get(position);

                // 現状LearnedがOnだったらOffに、OffだったらOnにセットする
                if (card.isLearned()) {
                    card.setLearned(false);
                } else {
                    card.setLearned(true);
                }
                // 変更を表示に反映させる
                mRecyclerViewAdapter.notifyItemChanged(position);
            }

            @Override
            public void onFusenClick(int position) {
                card = mCardLinkedList.get(position);

                // 現状FusenがOnだったらOffに、OffだったらOnにセットする
                if (card.isFusenTag()) {
                    card.setFusenTag(false);
                } else {
                    card.setFusenTag(true);
                }
                // 変更を表示に反映させる
                mRecyclerViewAdapter.notifyItemChanged(position);
            }

            @Override
            public void onTrashClick(final int position) {
                card = mCardLinkedList.get(position);

                new AlertDialog.Builder(mActivity)
                        .setIcon(R.drawable.flower_024_19)
                        .setMessage(R.string.dlg_msg_delete_card)
                        .setPositiveButton(
                                R.string.delete,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        LogUtility.d("[削除]が選択されました");

                                        // LinkedListから削除し一覧に戻る
                                        mCardLinkedList.remove(position);
                                        mRecyclerViewAdapter.notifyItemRemoved(position);
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

    /**
     * 上部メニュー領域の各アイコン画像、FloatingActionButtonのハンドラ
     */
    public void setupListeners() {
        // 戻る
        findViewById(R.id.imageViewGoBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "imageViewGoBack", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        // Exercise
        findViewById(R.id.imageViewExercise).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("EXERCISE_MODE", Constants.EXERCISE_MODE_NORMAL);
                intent.setClass(mContext, ExerciseActivity.class);
                startActivity(intent);
                return;
            }
        });

        // ShuffleExercise
        findViewById(R.id.imageViewShuffleExercise).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(Constants.EXERCISE_MODE_KEY_NAME, Constants.EXERCISE_MODE_SHUFFLE);
                intent.setClass(mContext, ExerciseActivity.class);
                startActivity(intent);
                return;
            }
        });

        // ヘルプ
        findViewById(R.id.imageViewHelp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "imageViewHelp", Toast.LENGTH_LONG).show();

            }
        });

        // FloatingActionButton
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 新規登録モードでダイアログを開く（Positionの-1は使われることはないので特に意味はなし）
                CardSettingsDialogFragment cardSettingsDialogFragment = new CardSettingsDialogFragment(mRecyclerViewAdapter, null, -1);
                Bundle args     = new Bundle();
                args.putInt(Constants.CARD_SETTINGS_DIALOG_ARG_KEY_MODE, Constants.CARD_SETTINGS_FOR_NEW);
                cardSettingsDialogFragment.setArguments(args);
                cardSettingsDialogFragment.show(getSupportFragmentManager(),
                        FolderSettingsDialogFragment.class.getSimpleName());
            }
        });
    }
}
