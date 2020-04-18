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
 */
public class CategoryIconFragment extends Fragment {

    GlobalManager                   globalMgr = GlobalManager.getInstance();
    View                            mView;
    GridView                        mGridViewIcon;
    IconGridAdapter                 mIconGridAdapter;
    int                             mCategory;
    ArrayList<Integer>              mIconResourceIdList;

    public CategoryIconFragment(int category) {
        mCategory               = category;
        mIconResourceIdList     = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtility.d("onCreateView: ");
        mView = inflater.inflate(R.layout.fragment_category_icon, container, false);

        // for-each Resource名をR.drawable.名前としてintに変換してarrayに登録
        for (String iconName: Constants.ICON_NAME_ARRAY[mCategory]){
            int imageId = getResources().getIdentifier(iconName,"drawable", getActivity().getPackageName());
            mIconResourceIdList.add(imageId);
        }

        // アイコンのGridView
        mIconGridAdapter        = new IconGridAdapter(getActivity().getApplicationContext(), R.layout.gridview_item_icon, mIconResourceIdList);
        mGridViewIcon           = mView.findViewById(R.id.gridViewIcon);
        mGridViewIcon.setAdapter(mIconGridAdapter);
        mGridViewIcon.setBackgroundColor(globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).getFrontBackgroundColor());
        mGridViewIcon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // おもてのアイコン表示に反映
                globalMgr.mCardSettings.imageViewIcon.setImageResource(mIconResourceIdList.get(position));
                globalMgr.mTempCard.setImageIconResId(mIconResourceIdList.get(position));
                globalMgr.mChangedCardSettings = true;
            }
        });

        return mView;
    }

}
