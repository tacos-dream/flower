package net.somethingnew.kawatan.flower;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import net.somethingnew.kawatan.flower.db.DatabaseHelper;
import net.somethingnew.kawatan.flower.db.dao.CardDao;
import net.somethingnew.kawatan.flower.db.dao.FolderDao;

public class TestActivity extends AppCompatActivity {

    GlobalManager globalMgr = GlobalManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test);

        findViewById(R.id.btn_pref_reset).setOnClickListener(view -> {
            SharedPreferences sharedPref = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
            sharedPref.edit().clear().commit();
            Toast.makeText(getApplicationContext(), "Shared prefs are deleted successfully.", Toast.LENGTH_LONG).show();
        });

        findViewById(R.id.btn_delete_tables).setOnClickListener(view -> {
            FolderDao folderDao = new FolderDao(getApplicationContext());
            folderDao.truncate();
            CardDao cardDao = new CardDao(getApplicationContext());
            cardDao.truncate();
            Toast.makeText(getApplicationContext(), "DB data are truncated successfully.", Toast.LENGTH_LONG).show();
        });

        findViewById(R.id.btn_drop_tables).setOnClickListener(view -> {
            final DatabaseHelper helper = new DatabaseHelper(this);
            SQLiteDatabase db = helper.getDBInstance();
            db.execSQL("DROP TABLE IF EXISTS FOLDER_TBL");
            db.execSQL("DROP TABLE IF EXISTS CARD_TBL");
            Toast.makeText(getApplicationContext(), "DB table are droped successfully.", Toast.LENGTH_LONG).show();
        });

        /**
         * /data/data/packageName/database/KawatanV1.db を削除
         */
        findViewById(R.id.btn_delete_db).setOnClickListener(view -> {
            deleteDatabase(Constants.DB_NAME);
            Toast.makeText(getApplicationContext(), "DB was deleted successfully.", Toast.LENGTH_LONG).show();
        });

        /**
         * Restart Application
         */
        findViewById(R.id.btn_restart).setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setClass(TestActivity.this, RestartActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        /**
         * 画面サイズ表示
         */
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        TextView textView = findViewById(R.id.textViewWindowSize);
        textView.setText("px: H" + metrics.heightPixels + " x  W" + metrics.widthPixels + "  dp: H" + (int) (metrics.heightPixels / metrics.density) + " x W" + (int) (metrics.widthPixels / metrics.density));


    }

}