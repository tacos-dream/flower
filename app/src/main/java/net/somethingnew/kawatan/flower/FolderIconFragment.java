package net.somethingnew.kawatan.flower;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;


import net.somethingnew.kawatan.flower.util.LogUtility;


public class FolderIconFragment extends Fragment implements AdapterView.OnItemClickListener {

    GlobalManager                   globalMgr = GlobalManager.getInstance();
    View                            mView;

    public FolderIconFragment() {
        LogUtility.d("FolderIconFragment: ");
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtility.d("onCreateView: ");
        mView = inflater.inflate(R.layout.fragment_folder_icon, container, false);

        // 初期表示
        // アイコン選択は表紙への処理なので、カラーなどは表紙のものを設定する
        globalMgr.mCardViewInFolderSettings.setCardBackgroundColor(globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).getCoverBackgroundColor());

        return mView;
    }

    /**
     * アイコンのGridView選択時のハンドラ
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        //textView.setText(il.getName(position));
        //Toast.makeText(getActivity().getApplicationContext(), "Pastel Color Hex: "+pastelColorArrayList.get(position), Toast.LENGTH_LONG).show();

        // TODO ハンドラ
        globalMgr.mChangedFolderSettings = true;
    }

}
