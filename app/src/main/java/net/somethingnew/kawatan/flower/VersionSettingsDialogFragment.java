package net.somethingnew.kawatan.flower;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import net.somethingnew.kawatan.flower.util.LogUtility;

public class VersionSettingsDialogFragment extends DialogFragment {

    GlobalManager globalMgr = GlobalManager.getInstance();
    private View mView;
    private RadioButton mLastCheckedRadioButton = null;
    private int mNewVersion;
    private VersionSettingsDialogFragment.OnVersionChangeListener mListener;
    private ImageView mImageViewIconFlower1;
    private ImageView mImageViewIconFlower2;
    private ImageView mImageViewIconFlower3;
    private ImageView mImageViewIconFlower4;
    private ImageView mImageViewIconJewelry1;
    private ImageView mImageViewIconJewelry2;
    private ImageView mImageViewIconJewelry3;
    private ImageView mImageViewIconJewelry4;
    private ImageView mImageViewIconCosme1;
    private ImageView mImageViewIconCosme2;
    private ImageView mImageViewIconCosme3;
    private ImageView mImageViewIconCosme4;
    private ImageView mImageViewIconFashion1;
    private ImageView mImageViewIconFashion2;
    private ImageView mImageViewIconFashion3;
    private ImageView mImageViewIconFashion4;
    private ImageView mImageViewIconFood1;
    private ImageView mImageViewIconFood2;
    private ImageView mImageViewIconFood3;
    private ImageView mImageViewIconFood4;
    private ImageView mImageViewIconOthers1;
    private ImageView mImageViewIconOthers2;
    private ImageView mImageViewIconOthers3;
    private ImageView mImageViewIconOthers4;


    /**
     * Interfaceを定義し、Activity側にoverrideでメソッドを実装させ、Adapter側からそれをCallback的に呼び出せるようにする
     */
    public interface OnVersionChangeListener {
        void onVersionChange();
    }

    public void setOnVersionChangeListener(VersionSettingsDialogFragment.OnVersionChangeListener listener) {
        mListener = listener;
    }

    VersionSettingsDialogFragment() {
        // 初期値
        mNewVersion = globalMgr.mCategory;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.dialog_version_settings, container, false);

        buildVersionEventListener();
        buildCloseEventListener();

        // 初期表示
        RadioButton radioButton;
        switch (globalMgr.mCategory) {
            case Constants.CATEGORY_INDEX_FLOWER:
                radioButton = mView.findViewById(R.id.radioButtonFlower);
                radioButton.setChecked(true);
                break;
            case Constants.CATEGORY_INDEX_JEWELRY:
                radioButton = mView.findViewById(R.id.radioButtonJewelry);
                radioButton.setChecked(true);
                break;
            case Constants.CATEGORY_INDEX_COSME:
                radioButton = mView.findViewById(R.id.radioButtonCosme);
                radioButton.setChecked(true);
                break;
            case Constants.CATEGORY_INDEX_FASHION:
                radioButton = mView.findViewById(R.id.radioButtonFashion);
                radioButton.setChecked(true);
                break;
            case Constants.CATEGORY_INDEX_FOOD:
                radioButton = mView.findViewById(R.id.radioButtonFood);
                radioButton.setChecked(true);
                break;
            case Constants.CATEGORY_INDEX_OTHERS:
                radioButton = mView.findViewById(R.id.radioButtonOthers);
                radioButton.setChecked(true);
                break;
        }

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);

        return mView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtility.d("onDestroy");
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        //以下を呼ぶと親のダイアログが消えてしまうので呼ばないようにする
        //super.onCancel(dialog);
        LogUtility.d("onCancel");
    }

    /**
     * 上記の通り、onCloseの後に呼び出されることはなくなったが、
     * 確認AlertDialogでgetDialog().dismiss()が呼ばれたときはここに来る
     */
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        LogUtility.d("onDismiss");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Dialog dialog = getDialog();

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int dialogWidth = (int) (metrics.widthPixels * Constants.DIALOG_FRAGMENT_WIDTH_RATIO);
        int dialogHeight = (int) (metrics.heightPixels * Constants.DIALOG_FRAGMENT_HEIGHT_RATIO);

        lp.width = dialogWidth;
        lp.height = dialogHeight;
        dialog.getWindow().setAttributes(lp);
        LogUtility.d("VersionSettingsDialogFragment: W" + dialogWidth + " x H" + dialogHeight);
    }

    /**
     * Version選択RadioButtonのイベントリスナ
     */
    public void buildVersionEventListener() {
        mImageViewIconFlower1 = mView.findViewById(R.id.imageViewIconFlower1);
        mImageViewIconFlower2 = mView.findViewById(R.id.imageViewIconFlower2);
        mImageViewIconFlower3 = mView.findViewById(R.id.imageViewIconFlower3);
        mImageViewIconFlower4 = mView.findViewById(R.id.imageViewIconFlower4);
        mImageViewIconJewelry1 = mView.findViewById(R.id.imageViewIconJewelry1);
        mImageViewIconJewelry2 = mView.findViewById(R.id.imageViewIconJewelry2);
        mImageViewIconJewelry3 = mView.findViewById(R.id.imageViewIconJewelry3);
        mImageViewIconJewelry4 = mView.findViewById(R.id.imageViewIconJewelry4);
        mImageViewIconCosme1 = mView.findViewById(R.id.imageViewIconCosme1);
        mImageViewIconCosme2 = mView.findViewById(R.id.imageViewIconCosme2);
        mImageViewIconCosme3 = mView.findViewById(R.id.imageViewIconCosme3);
        mImageViewIconCosme4 = mView.findViewById(R.id.imageViewIconCosme4);
        mImageViewIconFashion1 = mView.findViewById(R.id.imageViewIconFashion1);
        mImageViewIconFashion2 = mView.findViewById(R.id.imageViewIconFashion2);
        mImageViewIconFashion3 = mView.findViewById(R.id.imageViewIconFashion3);
        mImageViewIconFashion4 = mView.findViewById(R.id.imageViewIconFashion4);
        mImageViewIconFood1 = mView.findViewById(R.id.imageViewIconFood1);
        mImageViewIconFood2 = mView.findViewById(R.id.imageViewIconFood2);
        mImageViewIconFood3 = mView.findViewById(R.id.imageViewIconFood3);
        mImageViewIconFood4 = mView.findViewById(R.id.imageViewIconFood4);
        mImageViewIconOthers1 = mView.findViewById(R.id.imageViewIconOthers1);
        mImageViewIconOthers2 = mView.findViewById(R.id.imageViewIconOthers2);
        mImageViewIconOthers3 = mView.findViewById(R.id.imageViewIconOthers3);
        mImageViewIconOthers4 = mView.findViewById(R.id.imageViewIconOthers4);

        mImageViewIconFlower1.setImageResource(IconManager.getResIdAtRandom(Constants.CATEGORY_INDEX_FLOWER));
        mImageViewIconFlower2.setImageResource(IconManager.getResIdAtRandom(Constants.CATEGORY_INDEX_FLOWER));
        mImageViewIconFlower3.setImageResource(IconManager.getResIdAtRandom(Constants.CATEGORY_INDEX_FLOWER));
        mImageViewIconFlower4.setImageResource(IconManager.getResIdAtRandom(Constants.CATEGORY_INDEX_FLOWER));
        mImageViewIconJewelry1.setImageResource(IconManager.getResIdAtRandom(Constants.CATEGORY_INDEX_JEWELRY));
        mImageViewIconJewelry2.setImageResource(IconManager.getResIdAtRandom(Constants.CATEGORY_INDEX_JEWELRY));
        mImageViewIconJewelry3.setImageResource(IconManager.getResIdAtRandom(Constants.CATEGORY_INDEX_JEWELRY));
        mImageViewIconJewelry4.setImageResource(IconManager.getResIdAtRandom(Constants.CATEGORY_INDEX_JEWELRY));
        mImageViewIconCosme1.setImageResource(IconManager.getResIdAtRandom(Constants.CATEGORY_INDEX_COSME));
        mImageViewIconCosme2.setImageResource(IconManager.getResIdAtRandom(Constants.CATEGORY_INDEX_COSME));
        mImageViewIconCosme3.setImageResource(IconManager.getResIdAtRandom(Constants.CATEGORY_INDEX_COSME));
        mImageViewIconCosme4.setImageResource(IconManager.getResIdAtRandom(Constants.CATEGORY_INDEX_COSME));
        mImageViewIconFashion1.setImageResource(IconManager.getResIdAtRandom(Constants.CATEGORY_INDEX_FASHION));
        mImageViewIconFashion2.setImageResource(IconManager.getResIdAtRandom(Constants.CATEGORY_INDEX_FASHION));
        mImageViewIconFashion3.setImageResource(IconManager.getResIdAtRandom(Constants.CATEGORY_INDEX_FASHION));
        mImageViewIconFashion4.setImageResource(IconManager.getResIdAtRandom(Constants.CATEGORY_INDEX_FASHION));
        mImageViewIconFood1.setImageResource(IconManager.getResIdAtRandom(Constants.CATEGORY_INDEX_FOOD));
        mImageViewIconFood2.setImageResource(IconManager.getResIdAtRandom(Constants.CATEGORY_INDEX_FOOD));
        mImageViewIconFood3.setImageResource(IconManager.getResIdAtRandom(Constants.CATEGORY_INDEX_FOOD));
        mImageViewIconFood4.setImageResource(IconManager.getResIdAtRandom(Constants.CATEGORY_INDEX_FOOD));
        mImageViewIconOthers1.setImageResource(IconManager.getResIdAtRandom(Constants.CATEGORY_INDEX_OTHERS));
        mImageViewIconOthers2.setImageResource(IconManager.getResIdAtRandom(Constants.CATEGORY_INDEX_OTHERS));
        mImageViewIconOthers3.setImageResource(IconManager.getResIdAtRandom(Constants.CATEGORY_INDEX_OTHERS));
        mImageViewIconOthers4.setImageResource(IconManager.getResIdAtRandom(Constants.CATEGORY_INDEX_OTHERS));

        RadioGroup radioGroup = mView.findViewById(R.id.radioGroupVersion);

        // overrideすべき式がonCheckedChanged()の１つだけなので、lambda式でカッコよく実装
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                    RadioButton radioButton = mView.findViewById(checkedId);
                    switch (checkedId) {
                        case R.id.radioButtonFlower:
                            if (radioButton.isChecked()) {
                                if (mLastCheckedRadioButton != null)
                                    mLastCheckedRadioButton.setChecked(false);
                                mNewVersion = Constants.CATEGORY_INDEX_FLOWER;
                                mLastCheckedRadioButton = radioButton;
                            }
                            break;
                        case R.id.radioButtonJewelry:
                            if (radioButton.isChecked()) {
                                if (mLastCheckedRadioButton != null)
                                    mLastCheckedRadioButton.setChecked(false);
                                mNewVersion = Constants.CATEGORY_INDEX_JEWELRY;
                                mLastCheckedRadioButton = radioButton;
                            }
                            break;
                        case R.id.radioButtonCosme:
                            if (radioButton.isChecked()) {
                                if (mLastCheckedRadioButton != null)
                                    mLastCheckedRadioButton.setChecked(false);
                                mNewVersion = Constants.CATEGORY_INDEX_COSME;
                                mLastCheckedRadioButton = radioButton;
                            }
                            break;
                        case R.id.radioButtonFashion:
                            if (radioButton.isChecked()) {
                                if (mLastCheckedRadioButton != null)
                                    mLastCheckedRadioButton.setChecked(false);
                                mNewVersion = Constants.CATEGORY_INDEX_FASHION;
                                mLastCheckedRadioButton = radioButton;
                            }
                            break;
                        case R.id.radioButtonFood:
                            if (radioButton.isChecked()) {
                                if (mLastCheckedRadioButton != null)
                                    mLastCheckedRadioButton.setChecked(false);
                                mNewVersion = Constants.CATEGORY_INDEX_FOOD;
                                mLastCheckedRadioButton = radioButton;
                            }
                            break;
                        case R.id.radioButtonOthers:
                            if (radioButton.isChecked()) {
                                if (mLastCheckedRadioButton != null)
                                    mLastCheckedRadioButton.setChecked(false);
                                mNewVersion = Constants.CATEGORY_INDEX_OTHERS;
                                mLastCheckedRadioButton = radioButton;
                            }
                            break;
                    }
                }
        );

        // 従来通りの無名関数での実装例
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                RadioButton radioButton = mView.findViewById(checkedId);
//                switch (checkedId) {
//                    case R.id.radioButtonFood:
//                        if (radioButton.isChecked()) {
//                            if (mLastCheckedRadioButton != null) mLastCheckedRadioButton.setChecked(false);
//                            mNewVersion                 = Constants.CATEGORY_INDEX_FOOD;
//                            mLastCheckedRadioButton     = radioButton;
//                            setImageViewIconSample(mNewVersion);
//                        }
//                        break;
//                }
//            }
//        });
    }

    /**
     * 更新、一覧に戻るボタンのイベントリスナ
     */
    public void buildCloseEventListener() {
        mView.findViewById(R.id.buttonSaveClose).setOnClickListener(view -> {
            if (mNewVersion == globalMgr.mCategory) {
                new AlertDialog.Builder(getContext())
                        .setIcon(IconManager.getResIdAtRandom(globalMgr.mCategory))
                        .setTitle(R.string.dlg_title_information)
                        .setMessage(R.string.dlg_msg_no_change)
                        .setPositiveButton(
                                android.R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // AlertDialogを閉じるのみ
                                        return;
                                    }
                                })
                        .show();
                return;
            }
            SharedPreferences mSharedPref = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mSharedPref.edit();
            editor.putInt(Constants.SHARED_PREF_KEY_CATEGORY, mNewVersion);
            editor.apply();
            globalMgr.mCategory = mNewVersion;
            // タイトルを再描画するためにActivity側のOverrideメソッドを呼び出す
            mListener.onVersionChange();
            new AlertDialog.Builder(getContext())
                    .setIcon(IconManager.getResIdAtRandom(globalMgr.mCategory))
                    .setTitle(R.string.dlg_title_information)
                    .setMessage(R.string.dlg_msg_change_version)
                    .setPositiveButton(
                            R.string.close,
                            (dialog, which) -> {
                                // AlertDialogを閉じるのみ
                                return;
                            })
                    .show();
            return;
        });
        mView.findViewById(R.id.buttonCancelClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
                return;
            }
        });
    }

}
