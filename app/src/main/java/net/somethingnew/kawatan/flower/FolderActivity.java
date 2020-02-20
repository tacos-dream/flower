package net.somethingnew.kawatan.flower;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class FolderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        findViewById(R.id.btnOpenFolderSettings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // DialogFragment を表示します
                FolderSettingsDialogFragment exampleDialogFragment = new FolderSettingsDialogFragment();
                exampleDialogFragment.show(getSupportFragmentManager(),
                        FolderSettingsDialogFragment.class.getSimpleName());
            }
        });
    }
}
