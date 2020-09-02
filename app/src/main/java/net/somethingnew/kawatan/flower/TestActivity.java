package net.somethingnew.kawatan.flower;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import net.somethingnew.kawatan.flower.db.dao.CardDao;
import net.somethingnew.kawatan.flower.db.dao.FolderDao;

public class TestActivity extends AppCompatActivity {

    GlobalManager globalMgr = GlobalManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test);

        Button btn1 = findViewById(R.id.btn_pref_reset);
        btn1.setOnClickListener(view -> {
            SharedPreferences sharedPref = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
            sharedPref.edit().clear().commit();
            Toast.makeText(getApplicationContext(), "Shared prefs are deleted successfully.", Toast.LENGTH_LONG).show();
        });

        Button btn2 = findViewById(R.id.btn_delete_db);
        btn2.setOnClickListener(view -> {
            FolderDao folderDao = new FolderDao(getApplicationContext());
            folderDao.truncate();
            CardDao cardDao = new CardDao(getApplicationContext());
            cardDao.truncate();
            Toast.makeText(getApplicationContext(), "DB data are truncated successfully.", Toast.LENGTH_LONG).show();
        });
    }

}