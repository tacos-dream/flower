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

public class FolderCoverFragment extends Fragment {

    GlobalManager                   globalMgr = GlobalManager.getInstance();
    View                            mView;
    ArrayList<String>               textColorArrayList      = new ArrayList<>(Arrays.asList(MyData.textColorArray));
    ArrayList<String>               pastelColorArrayList    = new ArrayList<>(Arrays.asList(MyData.pastelColorArray));
    GridView                        gridViewText;

    public FolderCoverFragment() {
        //LogUtility.d("FolderSurfaceFragment: ");
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtility.d("onCreateView: ");
        mView = inflater.inflate(R.layout.fragment_folder_cover, container, false);

        // 文字色のGridView
        TextColorGridAdapter textColorGridAdapter = new TextColorGridAdapter(
                getActivity().getApplicationContext(), R.layout.gridview_item_text_color, textColorArrayList);
        gridViewText = mView.findViewById(R.id.gridViewText);
        gridViewText.setAdapter(textColorGridAdapter);
        gridViewText.setBackgroundColor(globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).getCoverBackgroundColor());
        gridViewText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 単語帳のおもて表紙の文字とを変える
                int color = Color.parseColor(textColorArrayList.get(position));
                globalMgr.mEditTextTitleNameInFolderSettings.setTextColor(color);
                globalMgr.mChangedFolderSettings = true;
            }
        });

        // 背景パステルカラーのGridView
        PastelColorGridAdapter pastelColorGridAdapter = new PastelColorGridAdapter(
                getActivity().getApplicationContext(), R.layout.gridview_item_pastel_color, pastelColorArrayList);
        GridView gridViewPastel = mView.findViewById(R.id.gridViewPastel);
        gridViewPastel.setAdapter(pastelColorGridAdapter);
        gridViewPastel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 単語帳のおもて表紙の色を変える
                int color = Color.parseColor(pastelColorArrayList.get(position));
                globalMgr.mCardViewInFolderSettings.setCardBackgroundColor(color);
                globalMgr.mTempFolder.setCoverBackgroundColor(color);
                globalMgr.mChangedFolderSettings = true;

                gridViewText.setBackgroundColor(color);
            }
        });

        // ColorPickerのクリックイベントを拾ってCardの色を動的に変更する
        ColorPickerView colorPicker     = mView.findViewById(R.id.colorPicker);
        colorPicker.setOnColorChangedListener(new ColorPickerView.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
                globalMgr.mCardViewInFolderSettings.setCardBackgroundColor(color);
                globalMgr.mTempFolder.setCoverBackgroundColor(color);
                globalMgr.mChangedFolderSettings = true;

                gridViewText.setBackgroundColor(color);
            }
        });

        // 初期表示
        globalMgr.mCardViewInFolderSettings.setCardBackgroundColor(globalMgr.mFolderLinkedList.get(globalMgr.mCurrentFolderIndex).getCoverBackgroundColor());

        return mView;
    }

}
