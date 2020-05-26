package net.somethingnew.kawatan.flower;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import net.somethingnew.kawatan.flower.db.DatabaseHelper;
import net.somethingnew.kawatan.flower.db.dao.CardDao;
import net.somethingnew.kawatan.flower.db.dao.FolderDao;
import net.somethingnew.kawatan.flower.model.CardModel;
import net.somethingnew.kawatan.flower.model.FolderModel;
import net.somethingnew.kawatan.flower.util.LogUtility;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * バックアップ画面Activity.<br>
 *     GoobleDriveへの自動バックアップ機能で対応することになったので、以下の実装は未使用（備忘のために残しておく）
 */
public class BackupActivity extends AppCompatActivity {

    GlobalManager               globalMgr   = GlobalManager.getInstance();
    File                        mFileInternalStorageDir;
    File                        mFileSdCardDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_backup);

        buildEventListener();

        mFileInternalStorageDir         = getApplicationContext().getFilesDir();
        mFileSdCardDir                  = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

        TextView textViewInternalPath   = findViewById(R.id.textViewInternalPath);
        TextView textViewISdPath        = findViewById(R.id.textViewSdPath);

        textViewInternalPath.setText(mFileInternalStorageDir.getPath());
        textViewISdPath.setText(mFileSdCardDir.getPath());

    }

    public void onStart() {
        super.onStart();
    }

    public void buildEventListener() {
        RadioGroup radioGroupBackupTo          = findViewById(R.id.radioGroupBackupTo);
        radioGroupBackupTo.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = findViewById(checkedId);
            switch (checkedId) {
                case R.id.radioButtonInternalDisk:
                    if (radioButton.isChecked()) {
                        LogUtility.d("radioButtonInternalDisk selected");
                    }
                    break;
                case R.id.radioButtonSdCard:
                    if (radioButton.isChecked()) {
                        LogUtility.d("radioButtonSdCard selected");
                    }
                    break;
            }
        });

        Button buttonBackup         = findViewById(R.id.buttonBackup);
        buttonBackup.setOnClickListener(view -> {
            LogUtility.d("buttonBackup: pressed");
            // TODO 保存処理
        });

    }
}
