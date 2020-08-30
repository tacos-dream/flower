package net.somethingnew.kawatan.flower;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import net.somethingnew.kawatan.flower.util.LogUtility;

public class SkinSettingsDialogFragment extends DialogFragment {

    GlobalManager globalMgr = GlobalManager.getInstance();
    private View mView;
    private SkinSettingsDialogFragment.OnSkinChangeListener mListener;
    private int mNewSkinHeaderColor;
    private int mNewSkinBodyColor;
    Toolbar mSampleHeader;
    LinearLayout mSampleBody;

    /**
     * Interfaceを定義し、Activity側にoverrideでメソッドを実装させ、Adapter側からそれをCallback的に呼び出せるようにする
     */
    public interface OnSkinChangeListener {
        void onSkinChange();
    }

    public void setOnSkinChangeListener(SkinSettingsDialogFragment.OnSkinChangeListener listener) {
        mListener = listener;
    }

    SkinSettingsDialogFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.dialog_skin_settings, container, false);
        mSampleHeader = mView.findViewById(R.id.toolbarSampleHeader);
        mSampleHeader.setBackground(new ColorDrawable(globalMgr.skinHeaderColor));
        mSampleBody = mView.findViewById(R.id.linearLayoutSampleBody);
        mSampleBody.setBackground(new ColorDrawable(globalMgr.skinBodyColor));
        TextView textView = mView.findViewById(R.id.textViewSampleTitle);
        textView.setText(Constants.CATEGORY_NAME[globalMgr.mCategory] + " Version");
        ColorSliderView colorSliderViewHeader = mView.findViewById(R.id.colorSliderHeader);
        colorSliderViewHeader.setListener((position, color) -> {
            mNewSkinHeaderColor = color;
            mSampleHeader.setBackground(new ColorDrawable(color));
        });
        ColorSliderView colorSliderViewBody = mView.findViewById(R.id.colorSliderBody);
        colorSliderViewBody.setListener((position, color) -> {
            mNewSkinBodyColor = color;
            mSampleBody.setBackground(new ColorDrawable(color));
        });

        buildBaseColorEventListener();
        buildCloseEventListener();

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
        LogUtility.d("SkinSettingsDialogFragment: W" + dialogWidth + " x H" + dialogHeight);
    }

    /**
     * ベースカラー選択のButtonイベントリスナ
     */
    public void buildBaseColorEventListener() {

        mView.findViewById(R.id.buttonPalette1A).setOnClickListener(view -> {
            ColorDrawable colorDrawable = (ColorDrawable) view.getBackground();
            mNewSkinBodyColor = colorDrawable.getColor();
            mNewSkinHeaderColor = Color.parseColor(Constants.PASTEL_PALETTE_DEEP[0]);
            mSampleBody.setBackground(colorDrawable);
            mSampleHeader.setBackground(new ColorDrawable(mNewSkinHeaderColor));
        });
        mView.findViewById(R.id.buttonPalette1B).setOnClickListener(view -> {
            ColorDrawable colorDrawable = (ColorDrawable) view.getBackground();
            mNewSkinBodyColor = colorDrawable.getColor();
            mNewSkinHeaderColor = Color.parseColor(Constants.PASTEL_PALETTE_DEEP[1]);
            mSampleBody.setBackground(colorDrawable);
            mSampleHeader.setBackground(new ColorDrawable(mNewSkinHeaderColor));
        });
        mView.findViewById(R.id.buttonPalette1C).setOnClickListener(view -> {
            ColorDrawable colorDrawable = (ColorDrawable) view.getBackground();
            mNewSkinBodyColor = colorDrawable.getColor();
            mNewSkinHeaderColor = Color.parseColor(Constants.PASTEL_PALETTE_DEEP[2]);
            mSampleBody.setBackground(colorDrawable);
            mSampleHeader.setBackground(new ColorDrawable(mNewSkinHeaderColor));
        });
        mView.findViewById(R.id.buttonPalette1D).setOnClickListener(view -> {
            ColorDrawable colorDrawable = (ColorDrawable) view.getBackground();
            mNewSkinBodyColor = colorDrawable.getColor();
            mNewSkinHeaderColor = Color.parseColor(Constants.PASTEL_PALETTE_DEEP[3]);
            mSampleBody.setBackground(colorDrawable);
            mSampleHeader.setBackground(new ColorDrawable(mNewSkinHeaderColor));
        });
        mView.findViewById(R.id.buttonPalette1E).setOnClickListener(view -> {
            ColorDrawable colorDrawable = (ColorDrawable) view.getBackground();
            mNewSkinBodyColor = colorDrawable.getColor();
            mNewSkinHeaderColor = Color.parseColor(Constants.PASTEL_PALETTE_DEEP[4]);
            mSampleBody.setBackground(colorDrawable);
            mSampleHeader.setBackground(new ColorDrawable(mNewSkinHeaderColor));
        });
        mView.findViewById(R.id.buttonPalette1F).setOnClickListener(view -> {
            ColorDrawable colorDrawable = (ColorDrawable) view.getBackground();
            mNewSkinBodyColor = colorDrawable.getColor();
            mNewSkinHeaderColor = Color.parseColor(Constants.PASTEL_PALETTE_DEEP[5]);
            mSampleBody.setBackground(colorDrawable);
            mSampleHeader.setBackground(new ColorDrawable(mNewSkinHeaderColor));
        });
        mView.findViewById(R.id.buttonPalette2A).setOnClickListener(view -> {
            ColorDrawable colorDrawable = (ColorDrawable) view.getBackground();
            mNewSkinBodyColor = colorDrawable.getColor();
            mNewSkinHeaderColor = Color.parseColor(Constants.PASTEL_PALETTE_DEEP[6]);
            mSampleBody.setBackground(colorDrawable);
            mSampleHeader.setBackground(new ColorDrawable(mNewSkinHeaderColor));
        });
        mView.findViewById(R.id.buttonPalette2B).setOnClickListener(view -> {
            ColorDrawable colorDrawable = (ColorDrawable) view.getBackground();
            mNewSkinBodyColor = colorDrawable.getColor();
            mNewSkinHeaderColor = Color.parseColor(Constants.PASTEL_PALETTE_DEEP[7]);
            mSampleBody.setBackground(colorDrawable);
            mSampleHeader.setBackground(new ColorDrawable(mNewSkinHeaderColor));
        });
        mView.findViewById(R.id.buttonPalette2C).setOnClickListener(view -> {
            ColorDrawable colorDrawable = (ColorDrawable) view.getBackground();
            mNewSkinBodyColor = colorDrawable.getColor();
            mNewSkinHeaderColor = Color.parseColor(Constants.PASTEL_PALETTE_DEEP[8]);
            mSampleBody.setBackground(colorDrawable);
            mSampleHeader.setBackground(new ColorDrawable(mNewSkinHeaderColor));
        });
        mView.findViewById(R.id.buttonPalette2D).setOnClickListener(view -> {
            ColorDrawable colorDrawable = (ColorDrawable) view.getBackground();
            mNewSkinBodyColor = colorDrawable.getColor();
            mNewSkinHeaderColor = Color.parseColor(Constants.PASTEL_PALETTE_DEEP[9]);
            mSampleBody.setBackground(colorDrawable);
            mSampleHeader.setBackground(new ColorDrawable(mNewSkinHeaderColor));
        });
        mView.findViewById(R.id.buttonPalette2E).setOnClickListener(view -> {
            ColorDrawable colorDrawable = (ColorDrawable) view.getBackground();
            mNewSkinBodyColor = colorDrawable.getColor();
            mNewSkinHeaderColor = Color.parseColor(Constants.PASTEL_PALETTE_DEEP[10]);
            mSampleBody.setBackground(colorDrawable);
            mSampleHeader.setBackground(new ColorDrawable(mNewSkinHeaderColor));
        });
        mView.findViewById(R.id.buttonPalette2F).setOnClickListener(view -> {
            ColorDrawable colorDrawable = (ColorDrawable) view.getBackground();
            mNewSkinBodyColor = colorDrawable.getColor();
            mNewSkinHeaderColor = Color.parseColor(Constants.PASTEL_PALETTE_DEEP[11]);
            mSampleBody.setBackground(colorDrawable);
            mSampleHeader.setBackground(new ColorDrawable(mNewSkinHeaderColor));
        });
    }

    /**
     * 閉じるボタンのイベントリスナ
     */
    public void buildCloseEventListener() {
        mView.findViewById(R.id.buttonSaveClose).setOnClickListener(view -> {
            if (mNewSkinBodyColor == globalMgr.skinBodyColor && mNewSkinHeaderColor == globalMgr.skinHeaderColor) {
                new AlertDialog.Builder(getContext())
                        .setIcon(IconManager.getResIdAtRandom(globalMgr.mCategory))
                        .setTitle(R.string.dlg_title_information)
                        .setMessage(R.string.dlg_msg_no_change)
                        .setPositiveButton(
                                android.R.string.ok,
                                (dialog, which) -> {
                                    // AlertDialogを閉じるのみ
                                    return;
                                })
                        .show();
                return;
            }

            SharedPreferences mSharedPref = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mSharedPref.edit();
            editor.putInt(Constants.SHARED_PREF_KEY_SKIN_HEADER_COLOR, mNewSkinHeaderColor);
            editor.putInt(Constants.SHARED_PREF_KEY_SKIN_BODY_COLOR, mNewSkinBodyColor);
            editor.apply();
            globalMgr.skinHeaderColor = mNewSkinHeaderColor;
            globalMgr.skinBodyColor = mNewSkinBodyColor;
            // タイトルを再描画するためにActivity側のOverrideメソッドを呼び出す
            mListener.onSkinChange();
            new AlertDialog.Builder(getContext())
                    .setIcon(IconManager.getResIdAtRandom(globalMgr.mCategory))
                    .setTitle(R.string.dlg_title_information)
                    .setMessage(R.string.dlg_msg_change_skin)
                    .setPositiveButton(
                            R.string.close,
                            (dialog, which) -> {
                                // AlertDialogを閉じるのみ
                                return;
                            })
                    .show();
            return;
        });
        mView.findViewById(R.id.buttonCancelClose).setOnClickListener(view -> {
            getDialog().dismiss();
            return;
        });
    }

}
