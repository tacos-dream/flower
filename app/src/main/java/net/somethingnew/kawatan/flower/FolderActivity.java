package net.somethingnew.kawatan.flower;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
    GlobalManager                           globalMgr = GlobalManager.getInstance();

    private SearchView                      mSearchView;

    private static RecyclerView.Adapter     mRecyclerViewAdapter;
    private static RecyclerView             mRecyclerView;

    private RecyclerView.LayoutManager      mLayoutManager;
    View.OnClickListener                    mCardOnClickListener;
    View.OnClickListener                    mLearnedIconOnClickListener;
    View.OnClickListener                    mFusenIconOnClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        // 標準のActionBarの代わりにToolbarをActionBarとしてセットして利用する
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 新規登録モードでダイアログを開く
                CardSettingsDialogFragment cardSettingsDialogFragment = new CardSettingsDialogFragment();
                Bundle args     = new Bundle();
                args.putInt(Constants.CARD_SETTINGS_DIALOG_ARG_KEY_MODE, Constants.CARD_SETTINGS_FOR_NEW);
                cardSettingsDialogFragment.setArguments(args);
                cardSettingsDialogFragment.show(getSupportFragmentManager(),
                        FolderSettingsDialogFragment.class.getSimpleName());
            }
        });

        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mCardOnClickListener        = new FolderActivity.CardOnClickListener(this);
        mLearnedIconOnClickListener = new FolderActivity.LearnedIconOnClickListener(this);
        mFusenIconOnClickListener   = new FolderActivity.FusenIconOnClickListener(this);

        mRecyclerViewAdapter        = new FolderRecyclerViewAdapter(mCardOnClickListener, mLearnedIconOnClickListener, mFusenIconOnClickListener);
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

                        TextView textViewName   = viewHolder.itemView.findViewById(R.id.textViewSurface);
                        String selectedName     = (String) textViewName.getText();
                        LogUtility.d("fromPos: " + fromPos + " toPos: " + toPos + " name: " + selectedName);

                        // fromPosとtoPosは隣同士を指している想定だが、とりあえずswap()を使って入れ替える
                        LinkedList cardLinkedList = globalMgr.mCardListMap.get(globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).getId());
                        Collections.swap(cardLinkedList, fromPos, toPos);

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

    /**
     * 個別カードの選択時のハンドラ
     * 当該カードの詳細ダイアログを表示する
     */
    private class CardOnClickListener implements View.OnClickListener {
        private final Context context;

        private CardOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            int selectedItemPositionInAdapter   = mRecyclerView.getChildAdapterPosition(v);
            int selectedItemPositionInLayout    = mRecyclerView.getChildLayoutPosition(v);
            RecyclerView.ViewHolder viewHolder  = mRecyclerView.getChildViewHolder(v);
            TextView textViewSurface            = viewHolder.itemView.findViewById(R.id.textViewSurface);
            String selectedSurface              = (String) textViewSurface.getText();
            LogUtility.d("selectedItemPositionInAdapter: " + selectedItemPositionInAdapter);
            LogUtility.d("selectedItemPositionInLayout: " + selectedItemPositionInLayout);
            LogUtility.d("selectedName: " + selectedSurface);

            globalMgr.mCurrentCardIndex         = selectedItemPositionInAdapter;

            // 編集モードでダイアログを開く
            CardSettingsDialogFragment cardSettingsDialogFragment = new CardSettingsDialogFragment();
            Bundle args     = new Bundle();
            args.putInt(Constants.CARD_SETTINGS_DIALOG_ARG_KEY_MODE, Constants.CARD_SETTINGS_FOR_EDIT);
            cardSettingsDialogFragment.setArguments(args);
            cardSettingsDialogFragment.show(getSupportFragmentManager(), FolderSettingsDialogFragment.class.getSimpleName());
        }
    }

    /**
     * LearnedのImageアイコンをクリックしたときのハンドラ
     *
     */
    private class LearnedIconOnClickListener implements View.OnClickListener {
        private final Context context;

        private LearnedIconOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, "習得済みアイコンのクリック", Toast.LENGTH_LONG).show();

            // tagにCardのidが保持されているので取り出しマップを検索する
            String cardId                       = v.getTag().toString();
            LinkedList cardLinkedList           = globalMgr.mCardListMap.get(globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).getId());
            CardModel card                      = null;
            for (int i = 0; i < cardLinkedList.size(); i++) {
                card = (CardModel)cardLinkedList.get(i);
                if (card.getId().equals(cardId)) {
                    break;
                }
            }

            // 現状LearnedがOnだったらOffに、OffだったらOnにセットする
            if (card.isLearned()) {
                LogUtility.d("Learned On → Off");
                ((ImageView)v).setImageResource(R.drawable.heart_off);
                card.setLearned(false);
            }
            else {
                LogUtility.d("Learned Off → On");
                ((ImageView)v).setImageResource(R.drawable.heart_on);
                card.setLearned(true);
            }
        }
    }

    /**
     * FusenのImageアイコンをクリックしたときのハンドラ
     *
     */
    private class FusenIconOnClickListener implements View.OnClickListener {
        private final Context context;

        private FusenIconOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            // どのImageアイコンがクリックされたかを識別するためにViewにTag付けされたFolderのidを引数にセットして編集モードでダイアログを開く
            Toast.makeText(context, "付箋アイコンのクリック", Toast.LENGTH_LONG).show();

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
}
