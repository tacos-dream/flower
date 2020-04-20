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

    GlobalManager                   globalMgr = GlobalManager.getInstance();
    View                            mView;
    GridView                        mGridViewIcon;
    IconGridAdapter                 mIconGridAdapter;
    int                             mCategory;
    int                             mHost;
    ArrayList<Integer>              mIconResourceIdList;

    public CategoryIconFragment(int category, int host) {
        LogUtility.d("constructor: category: " + category);
        mCategory               = category;
        mHost                   = host;
        mIconResourceIdList     = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtility.d("onCreateView: category: " + mCategory);
        mView = inflater.inflate(R.layout.fragment_category_icon, container, false);

        // Resource名をR.drawable.名前としてintに変換してarrayに登録
        for (int i = 0; i < Constants.NUM_OF_ICONS_IN_CATEGORY[mCategory]; i++) {
            String iconName = Constants.ICON_TAB_ARRAY[mCategory] + "_" +  String.format("%03d", i+1);
            int imageId = getResources().getIdentifier(iconName,"drawable", getActivity().getPackageName());
            mIconResourceIdList.add(imageId);
        }

        // アイコンのGridView
        mIconGridAdapter        = new IconGridAdapter(getActivity().getApplicationContext(), R.layout.gridview_item_icon, mIconResourceIdList, mHost);
        mGridViewIcon           = mView.findViewById(R.id.gridViewIcon);
        mGridViewIcon.setAdapter(mIconGridAdapter);
        if (mHost == Constants.CATEGORY_ICON_IN_FOLDER_SETTINGS) {
            mGridViewIcon.setBackgroundColor(globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).getCoverBackgroundColor());
        }
        else {
            mGridViewIcon.setBackgroundColor(globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).getFrontBackgroundColor());
        }
        mGridViewIcon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mHost == Constants.CATEGORY_ICON_IN_FOLDER_SETTINGS) {
                    // FolderSettingsの場合　→　表紙のアイコン表示に反映
                    globalMgr.mFolderSettings.imageViewIcon.setImageResource(mIconResourceIdList.get(position));
                    globalMgr.mTempFolder.setImageIconResId(mIconResourceIdList.get(position));
                    globalMgr.mChangedFolderSettings = true;
                }
                else {
                    // CardSettingsの場合　→　おもてのアイコン表示に反映
                    globalMgr.mCardSettings.imageViewIcon.setImageResource(mIconResourceIdList.get(position));
                    globalMgr.mTempCard.setImageIconResId(mIconResourceIdList.get(position));
                    globalMgr.mChangedCardSettings = true;
                }
            }
        });

        return mView;
    }

}
