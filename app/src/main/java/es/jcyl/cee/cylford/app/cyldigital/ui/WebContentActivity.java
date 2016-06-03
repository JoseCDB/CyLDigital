package es.jcyl.cee.cylford.app.cyldigital.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import es.jcyl.cee.cylford.app.cyldigital.MainActivity;
import es.jcyl.cee.cylford.app.cyldigital.R;


/**
 * Actividad para el "navegador web"
 */
public class WebContentActivity extends MainActivity implements OnClickListener {
    //anterior vista
    View back;
    //botón volver ventana
    Button wback;
    //botón adelante ventana
    Button wfoward;
    //objeto WebView
    WebView web;

    View loading;

    String url = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_content);
        getSupportActionBar().hide();

        url = this.getIntent().getStringExtra("url");

        back = this.findViewById(R.id.back);
        back.setOnClickListener(this);

        wback = (Button) this.findViewById(R.id.web_back);
        wback.setOnClickListener(this);

        wfoward = (Button) this.findViewById(R.id.web_foward);
        wfoward.setOnClickListener(this);
        // Así no se muestra el texto del TextView del título
        TextView tv = (TextView) this.findViewById(R.id.title);
        tv.setText("");

        loading = this.findViewById(R.id.loading);

        web = (WebView) findViewById(R.id.web);

        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        web.getSettings().setUserAgentString(web.getSettings().getUserAgentString()+ " " + "cylford");
        web.getSettings().setUseWideViewPort(true);
        web.getSettings().setLoadWithOverviewMode(true);
        web.setWebViewClient(new MyWebViewClient());
        web.loadUrl(url);
    }


    @Override
    public void onClick(View v) {
        if( v == back){
            this.onBackPressed();
        }else if( v == wback){
            if( web.canGoBack()){
                web.goBack();
                wback.setEnabled(web.canGoBack());
            }else{
                wback.setEnabled(false);
            }
        }else if( v == wfoward){
            if( web.canGoForward()){
                web.goForward();
                wfoward.setEnabled(web.canGoForward());
            }else{
                wfoward.setEnabled(false);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && web.canGoBack()) {
            web.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            loading.setVisibility(View.GONE);
            wback.setEnabled(web.canGoBack());
            wfoward.setEnabled(web.canGoForward());
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            loading.setVisibility(View.VISIBLE);
            wback.setEnabled(web.canGoBack());
            wfoward.setEnabled(web.canGoForward());
        }
    }
}
