package net.somethingnew.kawatan.flower;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

import net.somethingnew.kawatan.flower.util.LogUtility;

import java.util.ArrayList;
import java.util.Arrays;

public class FolderBackFragment extends Fragment {

    GlobalManager globalMgr = GlobalManager.getInstance();
    View mView;
    ArrayList<String> pastelColorArrayList = new ArrayList<>(Arrays.asList(Constants.PASTEL_PALETTE_LIGHT_BASE));
    ArrayList<Button> textColorButtonArray = new ArrayList<>();

    public FolderBackFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtility.d("onCreateView: ");
        mView = inflater.inflate(R.layout.fragment_foldersetting_color, container, false);

        // 文字色変更用ボタン群
        for (int resourceId: Constants.TEXT_COLOR_BUTTON_RESOURCE_ID) {
            Button btn = mView.findViewById(resourceId);
            textColorButtonArray.add(btn);
            btn.setOnClickListener(view -> {
                // 単語帳の表紙の文字色を変える
                int color = ((Button)view).getCurrentTextColor();
                globalMgr.mFolderSettings.editTextTitle.setTextColor(color);
                globalMgr.mTempFolder.setBackTextColor(color);
                globalMgr.mChangedFolderSettings = true;
            });
        }

        // 背景パステルカラーのGridView（規定色）
        PastelColorGridAdapter pastelColorGridAdapter = new PastelColorGridAdapter(
                getActivity().getApplicationContext(), R.layout.gridview_item_pastel_color, pastelColorArrayList);
        GridView gridViewPastel = mView.findViewById(R.id.gridViewPastel);
        gridViewPastel.setAdapter(pastelColorGridAdapter);
        gridViewPastel.setOnItemClickListener((parent, view, position, id) -> {
            // 単語帳のおもて面の色を変える
            int color = Color.parseColor(pastelColorArrayList.get(position));
            globalMgr.mFolderSettings.cardView.setCardBackgroundColor(color);
            globalMgr.mTempFolder.setBackBackgroundColor(color);
            globalMgr.mChangedFolderSettings = true;
            changeTextColorButtonsBackground(color);
        });

        // ColorPickerのクリックイベントを拾ってCardの色を動的に変更する
        ColorSliderView colorSliderViewHeader = mView.findViewById(R.id.colorSliderDeep);
        colorSliderViewHeader.setListener((position, color) -> {
            globalMgr.mFolderSettings.cardView.setCardBackgroundColor(color);
            globalMgr.mTempFolder.setBackBackgroundColor(color);
            globalMgr.mChangedFolderSettings = true;
            changeTextColorButtonsBackground(color);
        });
        ColorSliderView colorSliderViewBody = mView.findViewById(R.id.colorSliderLight);
        colorSliderViewBody.setListener((position, color) -> {
            globalMgr.mFolderSettings.cardView.setCardBackgroundColor(color);
            globalMgr.mTempFolder.setBackBackgroundColor(color);
            globalMgr.mChangedFolderSettings = true;
            changeTextColorButtonsBackground(color);
        });

        mView.findViewById(R.id.constraintLayoutWhole).setBackgroundColor(globalMgr.skinBodyColor);

        return mView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtility.d("onDestroy");
    }

    public void changeTextColorButtonsBackground(int backgroundColor) {
        for (Button textButton: textColorButtonArray) {
            GradientDrawable bgShape = (GradientDrawable)textButton.getBackground();
            bgShape.setColor(backgroundColor);
            bgShape.setStroke(1, Color.BLACK);
        }
    }

}
