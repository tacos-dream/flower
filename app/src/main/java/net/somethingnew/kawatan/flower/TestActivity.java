package net.somethingnew.kawatan.flower;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

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
        });
    }

}