package net.somethingnew.kawatan.flower;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import net.somethingnew.kawatan.flower.util.FlickTapCheck;
import net.somethingnew.kawatan.flower.util.LogUtility;

public class ExerciseDialogFragment extends DialogFragment {

    private GlobalManager                       globalMgr = GlobalManager.getInstance();
    private View                                mView;
    private LayoutInflater                      mInflater;
    private Boolean                             mUnderExercise = false;
    private int                                 currentCardIndex = 0;


    ExerciseDialogFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mInflater   = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView       = inflater.inflate(R.layout.activity_exercise, container, false);

        buildEventListener();

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);

        // OSの戻るボタンの無効化
        // メニュー内の戻るボタンを実装するまではとりあえずコメント
        //this.setCancelable(false);

        return mView;
    }

    /**
     * 上記の通り、onCloseの後に呼び出されることはなくなったが、
     * 確認AlertDialogでgetDialog().dismiss()が呼ばれたときはここに来る
     */
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        //Toast.makeText(getActivity().getApplicationContext(), "onDismiss", Toast.LENGTH_LONG).show();
        LogUtility.d("onDismiss");
    }

    /**
     * 上部メニュー領域の各アイコン画像やカードの編集箇所のイベントハンドラ
     */
    public void buildEventListener() {
        // 戻る
        mView.findViewById(R.id.imageViewGoBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                //Toast.makeText(getActivity().getApplicationContext(), "キャンセル", Toast.LENGTH_LONG).show();

                if (!mUnderExercise) {
                    // 練習開始前の場合は確認ダイアログ無しですぐに一覧に戻る
                    getDialog().dismiss();
                    return;
                }

                new AlertDialog.Builder(getContext())
                        .setIcon(R.drawable.flower_024_19)
                        .setMessage(R.string.dlg_msg_go_back_to_list)
                        .setPositiveButton(
                                R.string.go_back_list,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        LogUtility.d("[一覧に戻る]が選択されました");
                                        getDialog().dismiss();      // 親も消す
                                    }
                                })
                        .setNegativeButton(
                                R.string.cancel,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        LogUtility.d("[キャンセル]が選択されました");
                                        // このAlertDialogだけが消え、親のダイアログ表示状態に戻る
                                    }
                                })
                        .show();
            }
        });
        // 学習済み
        mView.findViewById(R.id.imageViewLearned).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                Toast.makeText(getActivity().getApplicationContext(), "imageViewLearned", Toast.LENGTH_LONG).show();
            }
        });

        View flickView = getDialog().getWindow().getDecorView();
        float adjustX = 150.0f;
        float adjustY = 150.0f;
        new FlickTapCheck(flickView, adjustX, adjustY) {
            @Override
            public void onFlick(int flickData) {
                switch (flickData) {
                    case FlickTapCheck.LEFT_FLICK:
                        LogUtility.d("左フリック");
                        break;

                    case FlickTapCheck.RIGHT_FLICK:
                        LogUtility.d("右フリック");
                        break;

                    case FlickTapCheck.UP_FLICK:
                        LogUtility.d("上フリック");
                        break;

                    case FlickTapCheck.DOWN_FLICK:
                        LogUtility.d("下フリック");
                        break;
                }
            }

            @Override
            public void onTap() {
                LogUtility.d("タップ");
            }
        };
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
        LogUtility.d("CardSettingsDialog: W" + dialogWidth + " x H" + dialogHeight);
    }

}
