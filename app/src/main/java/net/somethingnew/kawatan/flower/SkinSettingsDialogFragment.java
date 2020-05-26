package net.somethingnew.kawatan.flower;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Layout;
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

import net.somethingnew.kawatan.flower.db.dao.CardDao;
import net.somethingnew.kawatan.flower.db.dao.FolderDao;
import net.somethingnew.kawatan.flower.model.FolderModel;
import net.somethingnew.kawatan.flower.util.LogUtility;

public class SkinSettingsDialogFragment extends DialogFragment {

    GlobalManager               globalMgr = GlobalManager.getInstance();
    private View                mView;
    private RadioButton         mLastCheckedRadioButton = null;
    private int                 mNewVersion;
    private int                 mNewBaseColor;
    private ImageView           mImageViewIconSample1;
    private ImageView           mImageViewIconSample2;
    private ImageView           mImageViewIconSample3;
    private ImageView           mImageViewIconSample4;
    private View                mRelativeLayout;

    SkinSettingsDialogFragment() {
        // 初期値
        mNewVersion             = globalMgr.mCategory;
        mNewBaseColor           = globalMgr.mBaseColor;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.dialog_skin_settings, container, false);

        buildVersionEventListener();
        buildBaseColorEventListener();
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
            case Constants.CATEGORY_INDEX_HEART:
                radioButton = mView.findViewById(R.id.radioButtonHeart);
                radioButton.setChecked(true);
                break;
            case Constants.CATEGORY_INDEX_PARTS:
                radioButton = mView.findViewById(R.id.radioButtonParts);
                radioButton.setChecked(true);
                break;
            case Constants.CATEGORY_INDEX_CHARACTER:
                radioButton = mView.findViewById(R.id.radioButtonCharacter);
                radioButton.setChecked(true);
                break;
            case Constants.CATEGORY_INDEX_FOOD:
                radioButton = mView.findViewById(R.id.radioButtonFood);
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
        int dialogWidth = (int) (metrics.widthPixels * 0.9);
        int dialogHeight = (int) (metrics.heightPixels * 0.9);

        lp.width = dialogWidth;
        lp.height = dialogHeight;
        dialog.getWindow().setAttributes(lp);
        LogUtility.d("AboutIconImageDialogFragment: W" + dialogWidth + " x H" + dialogHeight);
    }

    /**
     * Version選択RadioButtonのイベントリスナ
     */
    public void buildVersionEventListener() {
        mImageViewIconSample1           = mView.findViewById(R.id.imageViewIconSample1);
        mImageViewIconSample2           = mView.findViewById(R.id.imageViewIconSample2);
        mImageViewIconSample3           = mView.findViewById(R.id.imageViewIconSample3);
        mImageViewIconSample4           = mView.findViewById(R.id.imageViewIconSample4);

        RadioGroup radioGroup1          = mView.findViewById(R.id.radioGroupVersion1);

        // overrideすべき式がonCheckedChanged()の１つだけなので、lambda式でカッコよく実装可能
        radioGroup1.setOnCheckedChangeListener((group, checkedId) -> {
                RadioButton radioButton = mView.findViewById(checkedId);
                switch (checkedId) {
                    case R.id.radioButtonFlower:
                        if (radioButton.isChecked()) {
                            if (mLastCheckedRadioButton != null) mLastCheckedRadioButton.setChecked(false);
                            mNewVersion                 = Constants.CATEGORY_INDEX_FLOWER;
                            mLastCheckedRadioButton     = radioButton;
                            setImageViewIconSample(mNewVersion);
                        }
                        break;
                    case R.id.radioButtonJewelry:
                        if (radioButton.isChecked()) {
                            if (mLastCheckedRadioButton != null) mLastCheckedRadioButton.setChecked(false);
                            mNewVersion                 = Constants.CATEGORY_INDEX_JEWELRY;
                            mLastCheckedRadioButton     = radioButton;
                            setImageViewIconSample(mNewVersion);
                        }
                        break;
                    case R.id.radioButtonCosme:
                        if (radioButton.isChecked()) {
                            if (mLastCheckedRadioButton != null) mLastCheckedRadioButton.setChecked(false);
                            mNewVersion                 = Constants.CATEGORY_INDEX_COSME;
                            mLastCheckedRadioButton     = radioButton;
                            setImageViewIconSample(mNewVersion);
                        }
                        break;
                    case R.id.radioButtonHeart:
                        if (radioButton.isChecked()) {
                            if (mLastCheckedRadioButton != null) mLastCheckedRadioButton.setChecked(false);
                            mNewVersion                 = Constants.CATEGORY_INDEX_HEART;
                            mLastCheckedRadioButton     = radioButton;
                            setImageViewIconSample(mNewVersion);
                        }
                        break;
                }
            }
        );

        RadioGroup radioGroup2 = mView.findViewById(R.id.radioGroupVersion2);
        // こっちは従来通りの無名関数で
        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = mView.findViewById(checkedId);
                switch (checkedId) {
                    case R.id.radioButtonParts:
                        if (radioButton.isChecked()) {
                            if (mLastCheckedRadioButton != null) mLastCheckedRadioButton.setChecked(false);
                            mNewVersion                 = Constants.CATEGORY_INDEX_PARTS;
                            mLastCheckedRadioButton     = radioButton;
                            setImageViewIconSample(mNewVersion);
                        }
                        break;
                    case R.id.radioButtonCharacter:
                        if (radioButton.isChecked()) {
                            if (mLastCheckedRadioButton != null) mLastCheckedRadioButton.setChecked(false);
                            mNewVersion                 = Constants.CATEGORY_INDEX_CHARACTER;
                            mLastCheckedRadioButton     = radioButton;
                            setImageViewIconSample(mNewVersion);
                        }
                        break;
                    case R.id.radioButtonFood:
                        if (radioButton.isChecked()) {
                            if (mLastCheckedRadioButton != null) mLastCheckedRadioButton.setChecked(false);
                            mNewVersion                 = Constants.CATEGORY_INDEX_FOOD;
                            mLastCheckedRadioButton     = radioButton;
                            setImageViewIconSample(mNewVersion);
                        }
                        break;
                }
            }
        });
    }

    private void setImageViewIconSample(int category) {
        mImageViewIconSample1.setImageResource(IconManager.getResIdAtRandom(category));
        mImageViewIconSample2.setImageResource(IconManager.getResIdAtRandom(category));
        mImageViewIconSample3.setImageResource(IconManager.getResIdAtRandom(category));
        mImageViewIconSample4.setImageResource(IconManager.getResIdAtRandom(category));
    }

    /**
     * ベースカラー選択のButtonイベントリスナ
     */
    public void buildBaseColorEventListener() {

        mView.findViewById(R.id.buttonWhite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GradientDrawable drawable   = (GradientDrawable) view.getBackground();
                mNewBaseColor               = view.getSolidColor();
                drawable.setStroke(2, Color.RED);
            }
        });
        mView.findViewById(R.id.buttonPink).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GradientDrawable drawable   = (GradientDrawable) view.getBackground();
                mNewBaseColor               = view.getSolidColor();
                drawable.setStroke(2, Color.RED);
            }
        });
        mView.findViewById(R.id.buttonGreen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GradientDrawable drawable   = (GradientDrawable) view.getBackground();
                mNewBaseColor               = view.getSolidColor();
                drawable.setStroke(2, Color.RED);
            }
        });
        mView.findViewById(R.id.buttonBlue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GradientDrawable drawable   = (GradientDrawable) view.getBackground();
                mNewBaseColor               = view.getSolidColor();
                drawable.setStroke(2, Color.RED);
            }
        });
        mView.findViewById(R.id.buttonViolet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GradientDrawable drawable   = (GradientDrawable) view.getBackground();
                mNewBaseColor               = view.getSolidColor();
                drawable.setStroke(2, Color.RED);
            }
        });
        mView.findViewById(R.id.buttonYellow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GradientDrawable drawable   = (GradientDrawable) view.getBackground();
                mNewBaseColor               = view.getSolidColor();
                drawable.setStroke(2, Color.RED);
            }
        });
        mView.findViewById(R.id.buttonGrey).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GradientDrawable drawable   = (GradientDrawable) view.getBackground();
                mNewBaseColor               = view.getSolidColor();
                drawable.setStroke(2, Color.RED);
            }
        });
        mView.findViewById(R.id.buttonDarkGrey).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GradientDrawable drawable   = (GradientDrawable) view.getBackground();
                mNewBaseColor               = view.getSolidColor();
                drawable.setStroke(2, Color.RED);
            }
        });
    }

    /**
     * 閉じるボタンのイベントリスナ
     */
    public void buildCloseEventListener() {
        mView.findViewById(R.id.buttonSaveClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mNewVersion == globalMgr.mCategory && mNewBaseColor == globalMgr.mBaseColor) {
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

                new AlertDialog.Builder(getContext())
                        .setIcon(IconManager.getResIdAtRandom(globalMgr.mCategory))
                        .setTitle(R.string.dlg_title_change_skin_confirm)
                        .setMessage(R.string.dlg_msg_change_skin)
                        .setPositiveButton(
                                R.string.change_restart,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        SharedPreferences mSharedPref           = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor         = mSharedPref.edit();
                                        // 新しい設定を反映させる
                                        if (mNewVersion != globalMgr.mCategory) {
                                            globalMgr.mCategory         = mNewVersion;
                                            editor.putInt(Constants.SHARED_PREF_KEY_CATEGORY, mNewVersion);
                                            editor.apply();
                                        }
                                        if (mNewBaseColor != globalMgr.mBaseColor) {
                                            globalMgr.mBaseColor        = mNewBaseColor;
                                            editor.putInt(Constants.SHARED_PREF_KEY_BASE_COLOR, mNewBaseColor);
                                            editor.apply();
                                        }

                                        // RestartActivity を起動（AndroidManifest.xml での宣言により別プロセスで起動し、アプリを再起動する
                                        Intent intent = RestartActivity.createIntent(getContext());
                                        getContext().startActivity(intent);
                                        return;
                                    }
                                })
                        .setNegativeButton(
                                R.string.cancel,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // 何もしない。AlertDialogを閉じるのみ
                                        return;
                                    }
                                })
                        .show();

                return;
            }
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
