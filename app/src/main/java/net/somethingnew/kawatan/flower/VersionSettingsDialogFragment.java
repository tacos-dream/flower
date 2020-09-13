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
import android.view.MotionEvent;
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
        ImageView imageViewIconFlower1 = mView.findViewById(R.id.imageViewIconFlower1);
        ImageView imageViewIconFlower2 = mView.findViewById(R.id.imageViewIconFlower2);
        ImageView imageViewIconFlower3 = mView.findViewById(R.id.imageViewIconFlower3);
        ImageView imageViewIconFlower4 = mView.findViewById(R.id.imageViewIconFlower4);
        ImageView imageViewIconJewelry1 = mView.findViewById(R.id.imageViewIconJewelry1);
        ImageView imageViewIconJewelry2 = mView.findViewById(R.id.imageViewIconJewelry2);
        ImageView imageViewIconJewelry3 = mView.findViewById(R.id.imageViewIconJewelry3);
        ImageView imageViewIconJewelry4 = mView.findViewById(R.id.imageViewIconJewelry4);
        ImageView imageViewIconFashion1 = mView.findViewById(R.id.imageViewIconFashion1);
        ImageView imageViewIconFashion2 = mView.findViewById(R.id.imageViewIconFashion2);
        ImageView imageViewIconFashion3 = mView.findViewById(R.id.imageViewIconFashion3);
        ImageView imageViewIconFashion4 = mView.findViewById(R.id.imageViewIconFashion4);
        ImageView imageViewIconFood1 = mView.findViewById(R.id.imageViewIconFood1);
        ImageView imageViewIconFood2 = mView.findViewById(R.id.imageViewIconFood2);
        ImageView imageViewIconFood3 = mView.findViewById(R.id.imageViewIconFood3);
        ImageView imageViewIconFood4 = mView.findViewById(R.id.imageViewIconFood4);
        ImageView imageViewIconOthers1 = mView.findViewById(R.id.imageViewIconOthers1);
        ImageView imageViewIconOthers2 = mView.findViewById(R.id.imageViewIconOthers2);
        ImageView imageViewIconOthers3 = mView.findViewById(R.id.imageViewIconOthers3);
        ImageView imageViewIconOthers4 = mView.findViewById(R.id.imageViewIconOthers4);

        imageViewIconFlower1.setImageResource(IconManager.getResIdAtRandom(Constants.CATEGORY_INDEX_FLOWER));
        imageViewIconFlower2.setImageResource(IconManager.getResIdAtRandom(Constants.CATEGORY_INDEX_FLOWER));
        imageViewIconFlower3.setImageResource(IconManager.getResIdAtRandom(Constants.CATEGORY_INDEX_FLOWER));
        imageViewIconFlower4.setImageResource(IconManager.getResIdAtRandom(Constants.CATEGORY_INDEX_FLOWER));
        imageViewIconJewelry1.setImageResource(IconManager.getResIdAtRandom(Constants.CATEGORY_INDEX_JEWELRY));
        imageViewIconJewelry2.setImageResource(IconManager.getResIdAtRandom(Constants.CATEGORY_INDEX_JEWELRY));
        imageViewIconJewelry3.setImageResource(IconManager.getResIdAtRandom(Constants.CATEGORY_INDEX_JEWELRY));
        imageViewIconJewelry4.setImageResource(IconManager.getResIdAtRandom(Constants.CATEGORY_INDEX_JEWELRY));
        imageViewIconFashion1.setImageResource(IconManager.getResIdAtRandom(Constants.CATEGORY_INDEX_FASHION));
        imageViewIconFashion2.setImageResource(IconManager.getResIdAtRandom(Constants.CATEGORY_INDEX_FASHION));
        imageViewIconFashion3.setImageResource(IconManager.getResIdAtRandom(Constants.CATEGORY_INDEX_FASHION));
        imageViewIconFashion4.setImageResource(IconManager.getResIdAtRandom(Constants.CATEGORY_INDEX_FASHION));
        imageViewIconFood1.setImageResource(IconManager.getResIdAtRandom(Constants.CATEGORY_INDEX_FOOD));
        imageViewIconFood2.setImageResource(IconManager.getResIdAtRandom(Constants.CATEGORY_INDEX_FOOD));
        imageViewIconFood3.setImageResource(IconManager.getResIdAtRandom(Constants.CATEGORY_INDEX_FOOD));
        imageViewIconFood4.setImageResource(IconManager.getResIdAtRandom(Constants.CATEGORY_INDEX_FOOD));
        imageViewIconOthers1.setImageResource(IconManager.getResIdAtRandom(Constants.CATEGORY_INDEX_OTHERS));
        imageViewIconOthers2.setImageResource(IconManager.getResIdAtRandom(Constants.CATEGORY_INDEX_OTHERS));
        imageViewIconOthers3.setImageResource(IconManager.getResIdAtRandom(Constants.CATEGORY_INDEX_OTHERS));
        imageViewIconOthers4.setImageResource(IconManager.getResIdAtRandom(Constants.CATEGORY_INDEX_OTHERS));

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
