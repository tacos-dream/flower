package net.somethingnew.kawatan.flower;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.somethingnew.kawatan.flower.db.dao.CardDao;
import net.somethingnew.kawatan.flower.model.CardModel;
import net.somethingnew.kawatan.flower.model.SearchResultModel;
import net.somethingnew.kawatan.flower.util.LogUtility;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Main画面：主なコンテンツはFolder一覧
 */
public class SearchResultActivity extends AppCompatActivity {
    private Context mContext;
    private Activity mActivity;
    private GlobalManager globalMgr = GlobalManager.getInstance();
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private SearchResultRecyclerViewAdapter mSearchResultRecyclerViewAdapter;
    private LinkedList<SearchResultModel> mSearchResultModelLinkedList = new LinkedList<>();
    private String mSearchWord;

    private TextView mTitle;
    private Toolbar mToolbar;
    private CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 検索の実行と結果のリスト(mSearchResultModelLinkedList)を作成
        mSearchWord = getIntent().getStringExtra(Constants.SEARCH_WORD_KEY_NAME);
        CardDao cardDao = new CardDao(getApplicationContext());
        ArrayList<CardModel> cardModelArrayList = cardDao.selectByText(mSearchWord);
        for (CardModel cardModel : cardModelArrayList) {
            SearchResultModel searchResultModel = new SearchResultModel();
            searchResultModel.setFolderId(cardModel.getFolderId());
            searchResultModel.setFrontText(cardModel.getFrontText());
            searchResultModel.setBackText(cardModel.getBackText());
            int folderIndex = 0;
            for (folderIndex = 0; folderIndex < globalMgr.mFolderLinkedList.size(); folderIndex++) {
                if (cardModel.getFolderId().equals(globalMgr.mFolderLinkedList.get(folderIndex).getId())) {
                    searchResultModel.setFolderName(globalMgr.mFolderLinkedList.get(folderIndex).getTitleName());
                    searchResultModel.setFolderIndex(folderIndex);
                    break;
                }
            }
            if (folderIndex >= globalMgr.mFolderLinkedList.size()) {
                continue;
            }
            mSearchResultModelLinkedList.add(searchResultModel);
        }
        LogUtility.d("Searched Folder counts: " + mSearchResultModelLinkedList.size());

        setContentView(R.layout.activity_search_result);

        // Get the application context
        mContext = getApplicationContext();
        mActivity = SearchResultActivity.this;

        // 標準のActionBarの代わりにToolbarをActionBarとしてセットして利用する
        mToolbar = findViewById(R.id.main_toolbar);
        mToolbar.setBackground(new ColorDrawable(globalMgr.skinHeaderColor));
        mCoordinatorLayout = findViewById(R.id.coordinatorLayout);
        mCoordinatorLayout.setBackground(new ColorDrawable(globalMgr.skinBodyColor));
        mTitle = findViewById(R.id.toolbar_title);
        mTitle.setText("Search Result: " + mSearchWord);
        setSupportActionBar(mToolbar);

        // ActionBarの機能にあるBackボタン処理を有効にする
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        buildRecyclerView();

        // 検索結果一覧ではD＆Dはやらない

    }

    @Override
    protected void onStart() {
        super.onStart();
//        LogUtility.d("onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
//        LogUtility.d("onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        LogUtility.d("onDestroy");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        // ActionBar（ToolBar）内のボタンのイベン処理
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * RecyclerView関連準備
     */
    public void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        mSearchResultRecyclerViewAdapter = new SearchResultRecyclerViewAdapter(mSearchResultModelLinkedList, mSearchWord);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mSearchResultRecyclerViewAdapter);

        mSearchResultRecyclerViewAdapter.setOnItemClickListener(position -> {
            // クリックされたItem(SearchResultModel)にglobalMgr.mFolderLinkedList上のpositionを保持しているので
            // globalMgr.mCurrentFolderIndexにセットしたあとにCardList画面に遷移させる
            // データがまだ未ロードの場合はロードする
            globalMgr.mCurrentFolderIndex = mSearchResultModelLinkedList.get(position).getFolderIndex();
            if (!globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).isCardLoaded()) {
                ObjectMapper mObjectMapper = new ObjectMapper();
                CardDao cardDao = new CardDao(getApplicationContext());
                ArrayList<CardModel> cardModelArrayList = cardDao.selectByFolderId(globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).getId());
                LogUtility.d("Loading CARD_TBL counts: " + cardModelArrayList.size());
                for (CardModel card : cardModelArrayList) {
                    try {
                        LogUtility.d("CardModel: " + mObjectMapper.writeValueAsString(card));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    globalMgr.mCardListMap.get(card.getFolderId()).add(card);
                }
                globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).setCardLoaded(true);
            }

            Intent intent = new Intent();
            intent.setClass(mContext, CardListActivity.class);
            startActivity(intent);
        });
    }
}
