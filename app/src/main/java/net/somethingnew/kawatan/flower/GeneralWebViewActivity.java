package net.somethingnew.kawatan.flower;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

/**
 * 画面Activity.<br>
 */
public class GeneralWebViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String targetUri = getIntent().getStringExtra("target_uri");
        setContentView(R.layout.activity_general_webview);

        findViewById(R.id.buttonGoBack).setOnClickListener(view -> {
            finish();
        });

        //レイアウトで指定したWebViewのIDを指定する。
        WebView myWebView = (WebView) findViewById(R.id.webView);

        //リンクをタップしたときに標準ブラウザを起動させない
        myWebView.setWebViewClient(new WebViewClient());

        // JavaScriptを有効にする
        myWebView.getSettings().setJavaScriptEnabled(true);

        myWebView.loadUrl(targetUri);
    }

    public void onStart() {
        super.onStart();
    }
}
