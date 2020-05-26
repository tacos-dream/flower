package net.somethingnew.kawatan.flower;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * 画面Activity.<br>
 */
public class GeneralWebViewActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String targetUri    = getIntent().getStringExtra("target_uri");

        setContentView(R.layout.activity_general_webview);

        //レイアウトで指定したWebViewのIDを指定する。
        WebView  myWebView = (WebView)findViewById(R.id.webView);

        //リンクをタップしたときに標準ブラウザを起動させない
        myWebView.setWebViewClient(new WebViewClient());

        // JavaScriptを有効にする
        myWebView.getSettings().setJavaScriptEnabled(true);

        //最初にYahoo! Japanのページを表示する。
        myWebView.loadUrl(targetUri);
        //myWebView.loadUrl("http://www.yahoo.co.jp");

    }

    public void onStart() {
        super.onStart();

    }


    @Override
    public void onClick(View v) {

        switch(v.getId()){

        }

    }

}
