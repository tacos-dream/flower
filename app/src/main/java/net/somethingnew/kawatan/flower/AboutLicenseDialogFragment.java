package net.somethingnew.kawatan.flower;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.fragment.app.DialogFragment;

import net.somethingnew.kawatan.flower.util.LogUtility;

public class AboutLicenseDialogFragment extends DialogFragment {

    GlobalManager globalMgr = GlobalManager.getInstance();

    AboutLicenseDialogFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_about_license, container, false);

        view.findViewById(R.id.imageViewIconLogo).setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setClass(getActivity().getApplicationContext(), GeneralWebViewActivity.class);
            intent.putExtra("target_uri", Constants.EXTERNAL_LINK_URL_JSPUCHIKIRA);
            startActivity(intent);
            getDialog().dismiss();
        });

        if (!globalMgr.isJapanese) {
            LinearLayout linearLayout = (LinearLayout)view.findViewById(R.id.linearLayoutFont);
            linearLayout.removeAllViews();
        }

        view.findViewById(R.id.buttonGoBack).setOnClickListener(v -> {
            getDialog().dismiss();
        });

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);

        return view;
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
        LogUtility.d("AboutIconImageDialogFragment: W" + dialogWidth + " x H" + dialogHeight);
    }

}
