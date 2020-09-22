package net.somethingnew.kawatan.flower;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

import net.somethingnew.kawatan.flower.util.LogUtility;

import java.util.ArrayList;

/**
 * アイコンのカテゴリーごとのFragment
 * 以下の２か所で使われる。mParentに保持して制御する。
 * 　FolderSettingsDialogでの表紙用アイコン選択
 * 　CardSettingsDialogでのおもてカード用アイコン選択
 */
public class CategoryIconFragment extends Fragment {

    GlobalManager globalMgr = GlobalManager.getInstance();
    View mView;
    GridView mGridViewIcon;
    IconGridAdapter mIconGridAdapter;
    int mCategory;
    int mHost;                  // FolderSettings内かCardSettings内での表示かの識別
    ArrayList<Integer> mIconResourceIdList;

    public CategoryIconFragment(int category, int host) {
        LogUtility.d("constructor: category: " + category);
        mCategory = category;
        mHost = host;
        mIconResourceIdList = IconManager.getIconResourceIdList(category);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtility.d("onCreateView: category: " + mCategory);
        mView = inflater.inflate(R.layout.fragment_category_icon, container, false);

        // アイコンのGridView
        mIconGridAdapter = new IconGridAdapter(getActivity().getApplicationContext(), R.layout.gridview_item_icon, mIconResourceIdList, mHost);
        mGridViewIcon = mView.findViewById(R.id.gridViewIcon);
        mGridViewIcon.setAdapter(mIconGridAdapter);

        // FolderSettingsDialog上か、CardSettingsDialog上かでセットする内容を変える
        mGridViewIcon.setBackgroundColor(
                (mHost == Constants.CATEGORY_ICON_IN_FOLDER_SETTINGS) ?
                        globalMgr.mTempFolder.getCoverBackgroundColor() : globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).getFrontBackgroundColor()
        );
        mGridViewIcon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mHost == Constants.CATEGORY_ICON_IN_FOLDER_SETTINGS) {
                    // FolderSettingsの場合　→　表紙のアイコン表示に反映
                    globalMgr.mFolderSettings.imageViewIcon.setImageResource(mIconResourceIdList.get(position));
                    globalMgr.mTempFolder.setImageIconResId(mIconResourceIdList.get(position));
                    globalMgr.mTempFolder.setIconCategory(mCategory);
                    globalMgr.mChangedFolderSettings = true;
                } else {
                    // CardSettingsの場合　→　アイコン表示に反映
                    globalMgr.mCardSettings.imageViewIconFront.setImageResource(mIconResourceIdList.get(position));
                    globalMgr.mCardSettings.imageViewIconBack.setImageResource(mIconResourceIdList.get(position));
                    globalMgr.mTempCard.setImageIconResId(mIconResourceIdList.get(position));
                    globalMgr.mTempCard.setIconCategory(mCategory);
                    globalMgr.mChangedCardSettings = true;
                }
            }
        });

        return mView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtility.d("onDestroy");
    }

}
