package net.somethingnew.kawatan.flower;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


/**
 * バックアップ画面Activity.<br>
 *     GoobleDriveへの自動バックアップ機能で対応することになったので、以下の実装は未使用（備忘のために残しておく）
 */
public class AboutUsActivity extends AppCompatActivity {

    GlobalManager globalMgr = GlobalManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_aboutus);

        TextView textView = findViewById(R.id.textViewVersion);
        textView.setText(Constants.VERSION);

        ImageView imageView = findViewById(R.id.imageViewLogo);
        imageView.setImageResource(IconManager.getResIdAtRandom(globalMgr.mCategory));

        findViewById(R.id.buttonGoBack).setOnClickListener(view -> {
            finish();
        });
    }

    public void onStart() {
        super.onStart();
    }

}
