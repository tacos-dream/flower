package net.somethingnew.kawatan.flower;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import net.somethingnew.kawatan.flower.util.LogUtility;

public class FolderIconFragment extends Fragment {

    GlobalManager globalMgr = GlobalManager.getInstance();
    View mView;
    SparseArray<Fragment> mRegisteredFragments = new SparseArray<>();

    public FolderIconFragment() {
        //LogUtility.d("FolderFrontFragment: ");
        for (int category = 0; category < Constants.NUM_OF_CATEGORY; category++) {
            Fragment fragment = new CategoryIconFragment(category, Constants.CATEGORY_ICON_IN_FOLDER_SETTINGS);
            mRegisteredFragments.put(category, fragment);
        }
//        LogUtility.d("registeredFragments.size: " + registeredFragments.size());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtility.d("onCreateView: ");
        mView = inflater.inflate(R.layout.fragment_folder_icon, container, false);

        // BottomNavigationで切り替える５つのアイコンカテゴリーのFragmentを作成
        BottomNavigationView bottomNavigationView = (BottomNavigationView) mView.findViewById(R.id.icon_bottom_navigation);
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setBackgroundColor(globalMgr.skinHeaderColor);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_flower:
                    globalMgr.currentCategoryPosition = Constants.CATEGORY_INDEX_FLOWER;
                    showFragment(mRegisteredFragments.get(Constants.CATEGORY_INDEX_FLOWER));
                    break;
                case R.id.navigation_jewelry:
                    globalMgr.currentCategoryPosition = Constants.CATEGORY_INDEX_JEWELRY;
                    showFragment(mRegisteredFragments.get(Constants.CATEGORY_INDEX_JEWELRY));
                    break;
                case R.id.navigation_fashion:
                    globalMgr.currentCategoryPosition = Constants.CATEGORY_INDEX_FASHION;
                    showFragment(mRegisteredFragments.get(Constants.CATEGORY_INDEX_FASHION));
                    break;
                case R.id.navigation_food:
                    globalMgr.currentCategoryPosition = Constants.CATEGORY_INDEX_FOOD;
                    showFragment(mRegisteredFragments.get(Constants.CATEGORY_INDEX_FOOD));
                    break;
                case R.id.navigation_others:
                    globalMgr.currentCategoryPosition = Constants.CATEGORY_INDEX_OTHERS;
                    showFragment(mRegisteredFragments.get(Constants.CATEGORY_INDEX_OTHERS));
                    break;
            }
            return true;
        });

        // 初期表示
        showFragment(mRegisteredFragments.get(globalMgr.currentCategoryPosition));

        // CardViewの初期表示
        globalMgr.mFolderSettings.cardView.setCardBackgroundColor(globalMgr.mTempFolder.getCoverBackgroundColor());
        globalMgr.mFolderSettings.imageViewIcon.setImageResource(globalMgr.mTempFolder.getImageIconResId());

        return mView;
    }

    public void showFragment(Fragment fragment) {
//        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.addToBackStack(null)
                .replace(R.id.icon_container, fragment)
                .commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtility.d("onDestroy");
    }

    /**
     * 表紙のタブで色を変えた後にICONタブに戻ってきたときに、アイコンのGridViewの背景色をその表紙色に変えてあげる処理
     * ICONタブに戻ってきたときに当クラス内では何もイベントが起きないので、FolderSettingsDialogFragment側からこの関数を
     * 明示的に呼び出さなければならない
     */
    public void refreshCategoryIconFragment() {
        CategoryIconFragment categoryIconFragment = (CategoryIconFragment) mRegisteredFragments.get(globalMgr.currentCategoryPosition);
        categoryIconFragment.mGridViewIcon.setBackgroundColor(globalMgr.mTempFolder.getCoverBackgroundColor());
    }

}
