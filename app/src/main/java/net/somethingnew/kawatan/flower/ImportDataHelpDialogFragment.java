package net.somethingnew.kawatan.flower;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.fragment.app.DialogFragment;

import net.somethingnew.kawatan.flower.util.LogUtility;

public class ImportDataHelpDialogFragment extends DialogFragment {

    GlobalManager globalMgr = GlobalManager.getInstance();
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.dialog_import_data_help, container, false);

        view.findViewById(R.id.buttonGoBack).setOnClickListener(view -> {
            getDialog().dismiss();
            return;
        });

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);

        // Android標準戻るボタンの無効化
        // dialog.setCancelableは効かない。DialogFragmentのsetCancelableを使用する
        //getDialog().setCancelable();
        this.setCancelable(false);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtility.d("onDestroy");
    }

    /**
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

        // ダイアログの縦横を最大限まで広げる（何もしないと縦長で横幅の狭いデフォルトのダイアログが表示されるため
        Dialog dialog = getDialog();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int dialogWidth = (int) (metrics.widthPixels * Constants.DIALOG_FRAGMENT_WIDTH_RATIO);
        int dialogHeight = (int) (metrics.heightPixels * Constants.DIALOG_FRAGMENT_HEIGHT_RATIO);
        lp.width = dialogWidth;
        lp.height = dialogHeight;
        dialog.getWindow().setAttributes(lp);
        LogUtility.d("FolderSettingsHelpDialogFragment: W" + dialogWidth + " x H" + dialogHeight);
    }

}
