package net.somethingnew.kawatan.flower;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TabHost;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.jaredrummler.android.colorpicker.ColorPickerView;

import net.somethingnew.kawatan.flower.util.LogUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FolderIconFragment extends Fragment {

    GlobalManager                       globalMgr = GlobalManager.getInstance();
    View                                mView;
    FolderIconPagerAdapter              mFolderIconPagerAdapter;
    ViewPager                           mViewPager;
    RecyclerView.Adapter                mRecyclerViewAdapter;
    TabHost                             mTabHost;
    int                                 mCurrentPosition;

    public FolderIconFragment() {
        //LogUtility.d("FolderFrontFragment: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtility.d("onCreateView: ");
        mView = inflater.inflate(R.layout.fragment_folder_icon, container, false);

        mFolderIconPagerAdapter         = new FolderIconPagerAdapter(getChildFragmentManager());
        mViewPager                      = mView.findViewById(R.id.viewPager);
        mViewPager.setAdapter(mFolderIconPagerAdapter);
        mViewPager.setOffscreenPageLimit(Constants.NUM_OF_ICON_TAB);

        // 8つのアイコンカテゴリーのタブを作成
        buildTabMenu();

        // CardViewの初期表示
        globalMgr.mFolderSettings.cardView.setCardBackgroundColor(globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).getCoverBackgroundColor());
        globalMgr.mFolderSettings.imageViewIcon.setImageResource(globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).getImageIconResId());
        globalMgr.mFolderSettings.imageViewFusen.setVisibility(View.INVISIBLE);
        globalMgr.mFolderSettings.editTextTitle.setEnabled(false);

        return mView;
    }

    /**
     * Tabメニュー部分の構築とリスナー登録
     */
    public void buildTabMenu() {
        LogUtility.d("buildTabMenu: ");

        mTabHost = mView.findViewById(android.R.id.tabhost);
        mTabHost.setup();

        for(int i = 0; i < mFolderIconPagerAdapter.getCount(); i++){
            int imageId = getResources().getIdentifier(Constants.ICON_TAB_IMAGE_ID[i],"drawable", getActivity().getPackageName());
            View tabView = new CustomTabView(getActivity(), "dummy", imageId);
            mTabHost.addTab(mTabHost
                    .newTabSpec(Constants.ICON_TAB_ARRAY[i])
                    .setIndicator(tabView)
                    .setContent(android.R.id.tabcontent));
        }

        // Tabのイベントリスナ
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                int position = Arrays.asList(Constants.ICON_TAB_ARRAY).indexOf(tabId);
                LogUtility.d("mTabHost onTabChanged: " + tabId + " position: " + position);
                // Pagerに表示を指示
                mViewPager.setCurrentItem(position);
                mCurrentPosition = position;

                CategoryIconFragment categoryIconFragment = (CategoryIconFragment) mFolderIconPagerAdapter.getRegisteredFragment(position);
                categoryIconFragment.mGridViewIcon.setBackgroundColor(globalMgr.mTempFolder.getCoverBackgroundColor());
            }
        });

        // 以下の実装は、Page側のスワイプに合わせてタブもスライドさせるためのもの
        // 非推奨だが他の方法が不明なのでとりあえず。
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                LogUtility.d("mViewPager setOnPageChangeListener position: " + position);
                super.onPageSelected(position);

                // 以下の呼び出しで、上記のonTabChanged()がinvokeされる
                mTabHost.setCurrentTab(position);
            }
        });

        // 初期表示のタブ設定
        mCurrentPosition = 0;
        mTabHost.setCurrentTab(mCurrentPosition);
    }

    /**
     * 表紙のタブで色を変えた後にICONタブに戻ってきたときに、アイコンのGridViewの背景色をその表紙色に変えてあげる処理
     * ICONタブに戻ってきたときに当クラス内では何もイベントが起きないので、FolderSettingsDialogFragment側からこの関数を
     * 明示的に呼び出さなければならない
     */
    public void refreshCategoryIconFragment() {
        CategoryIconFragment categoryIconFragment = (CategoryIconFragment) mFolderIconPagerAdapter.getRegisteredFragment(mCurrentPosition);
        categoryIconFragment.mGridViewIcon.setBackgroundColor(globalMgr.mTempFolder.getCoverBackgroundColor());
    }

}
