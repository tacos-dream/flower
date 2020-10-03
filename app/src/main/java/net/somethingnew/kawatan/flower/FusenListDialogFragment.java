package net.somethingnew.kawatan.flower;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.fragment.app.DialogFragment;

import net.somethingnew.kawatan.flower.util.LogUtility;

public class FusenListDialogFragment extends DialogFragment {

    GlobalManager globalMgr = GlobalManager.getInstance();
    View mView;
    private int currentFusenResId;
    private OnFusenChangeListener mListener;

    FusenListDialogFragment(int currentFusenResId) {
        // 初期値
        this.currentFusenResId = currentFusenResId;
    }

    public interface OnFusenChangeListener {
        void onFusenChange(int newFusenResId);
    }

    public void setOnChangeListener(OnFusenChangeListener listener) {
        mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.dialog_fusen_list, container, false);

        mView.findViewById(R.id.imageViewFusen00).setOnClickListener(view -> {
            if (currentFusenResId != R.drawable.fusen_00) {
                mListener.onFusenChange(R.drawable.fusen_00);
            }
            getDialog().dismiss();
            return;
        });

        for (int col = 0; col < Constants.FUSEN_NUM_OF_DESIGN_TYPE; col++) {
            for (int row = 0; row < Constants.FUSEN_NUM_OF_COLOR_TYPE; row++) {
                int finalRow = row;
                int finalCol = col;
                mView.findViewById(Constants.FUSEN_IMAGEVIEW_ID_ARRAY[col][row]).setOnClickListener(view -> {
                    if (currentFusenResId != Constants.FUSEN_RESOURCE_ID_ARRAY[finalCol][finalRow]) {
                        mListener.onFusenChange(Constants.FUSEN_RESOURCE_ID_ARRAY[finalCol][finalRow]);
                    }
                    getDialog().dismiss();
                    return;
                });
            }
        }

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        // ダイアログの外でのタップでダイアログ表示を消すには以下が必要だが、同時に、
        // this.setCancelable()もtrueで実行しないと効かない。
        // つまり、ダイアログのキャンセル処理は、ダイアログ外でのタップと、Android標準の戻りボタンの両方で
        // 効かすか、無視するか、の選択になる
        getDialog().setCanceledOnTouchOutside(true);

        // Android標準戻るボタンの無効化
        // dialog.setCancelableは効かない。DialogFragmentのsetCancelableを使用する
        //getDialog().setCancelable();
        this.setCancelable(true);

        return mView;
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

}
