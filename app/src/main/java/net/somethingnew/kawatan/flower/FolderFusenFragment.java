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


public class FolderFusenFragment extends Fragment {

    GlobalManager                   globalMgr = GlobalManager.getInstance();
    View                            mView;
    GridView                        gridViewFusen;
    FusenGridAdapter                fusenGridAdapter;
    ArrayList<Integer>              mFusenResourceIdList;

    public FolderFusenFragment() {
        LogUtility.d("FolderFusenFragment: ");
        // Required empty public constructor
        mFusenResourceIdList     = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtility.d("onCreateView: ");
        mView               = inflater.inflate(R.layout.fragment_folder_fusen, container, false);

        // for-each Resource名をR.drawable.名前としてintに変換してarrayに登録
        for (String fusenName: MyData.fusenNameArray){
            int imageId = getResources().getIdentifier(fusenName,"drawable", getActivity().getPackageName());
            mFusenResourceIdList.add(imageId);
        }

        // 付箋のGridView
        fusenGridAdapter    = new FusenGridAdapter(getActivity().getApplicationContext(), R.layout.gridview_item_fusen, mFusenResourceIdList);
        gridViewFusen       = mView.findViewById(R.id.gridViewFusen);
        gridViewFusen.setAdapter(fusenGridAdapter);
        gridViewFusen.setBackgroundColor(globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).getFrontBackgroundColor());
        gridViewFusen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 単語帳のアイコンを入れ替える
                globalMgr.mFolderSettings.imageViewFusen.setImageResource(mFusenResourceIdList.get(position));
                globalMgr.mTempFolder.setImageFusenResId(mFusenResourceIdList.get(position));
                globalMgr.mChangedFolderSettings = true;
            }
        });

        return mView;
    }

}
