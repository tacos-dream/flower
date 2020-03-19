package net.somethingnew.kawatan.flower;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class CardSettingsDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater     = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view                   = inflater.inflate(R.layout.dialog_card_settings, null);
        TextView titleTextView     = view.findViewById(R.id.title);

        // 呼び出し側からの引数の受け取り
        int mode = getArguments().getInt(Constants.CARD_SETTINGS_DIALOG_ARG_KEY_MODE);

        if (mode == Constants.CARD_SETTINGS_FOR_NEW) {
            Toast.makeText(getActivity().getApplicationContext(), "新規登録用", Toast.LENGTH_LONG).show();
            titleTextView.setText(R.string.dlg_title_new);
        }
        else {
            String cardId = getArguments().getString(Constants.CARD_SETTINGS_DIALOG_ARG_KEY_CARD_ID);
            Toast.makeText(getActivity().getApplicationContext(), "編集用(" + cardId + ")", Toast.LENGTH_LONG).show();
            titleTextView.setText(R.string.dlg_title_edit);
        }

        builder.setView(view);

        // OK ボタンのリスナ
        view.findViewById(R.id.positive_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // Close ボタンのリスナ
        view.findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // Create the AlertDialog object and return it
        return builder.create();

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
    }

}
