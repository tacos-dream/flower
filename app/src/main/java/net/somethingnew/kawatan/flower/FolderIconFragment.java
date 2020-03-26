package net.somethingnew.kawatan.flower;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

import com.jaredrummler.android.colorpicker.ColorPickerView;

import net.somethingnew.kawatan.flower.util.LogUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FolderIconFragment extends Fragment {

    GlobalManager                   globalMgr = GlobalManager.getInstance();
    View                            mView;
    GridView                        gridViewIcon;
    IconGridAdapter                 iconGridAdapter;

    public FolderIconFragment() {
        //LogUtility.d("FolderSurfaceFragment: ");
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtility.d("onCreateView: ");
        mView = inflater.inflate(R.layout.fragment_folder_icon, container, false);

        // for-each Resource名をR.drawable.名前としてintに変換してarrayに登録
        if (globalMgr.mIconResourceIdList == null) {
            globalMgr.mIconResourceIdList = new ArrayList<>();
            for (String iconName: MyData.iconNameArray){
                int imageId = getResources().getIdentifier(iconName,"drawable", getActivity().getPackageName());
                globalMgr.mIconResourceIdList.add(imageId);
            }
        }

        // アイコンのGridView
        iconGridAdapter = new IconGridAdapter(
                getActivity().getApplicationContext(), R.layout.gridview_item_icon);
        gridViewIcon = mView.findViewById(R.id.gridViewIcon);
        gridViewIcon.setAdapter(iconGridAdapter);
        gridViewIcon.setBackgroundColor(globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).getCoverBackgroundColor());
        gridViewIcon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 単語帳のアイコンを入れ替える
                globalMgr.mFolderSettings.imageViewIcon.setImageResource(globalMgr.mIconResourceIdList.get(position));
                globalMgr.mTempFolder.setImageIconResId(globalMgr.mIconResourceIdList.get(position));
                globalMgr.mChangedFolderSettings = true;
            }
        });

        // CardViewの初期表示
        globalMgr.mFolderSettings.cardView.setCardBackgroundColor(globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).getCoverBackgroundColor());
        globalMgr.mFolderSettings.imageViewIcon.setImageResource(globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).getImageIconResId());
        globalMgr.mFolderSettings.imageViewFusen.setVisibility(View.INVISIBLE);
        globalMgr.mFolderSettings.editTextTitle.setEnabled(false);

        return mView;
    }

}
