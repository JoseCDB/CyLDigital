package es.jcyl.cee.cylford.app.cyldigital.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.navdrawer.SimpleSideDrawer;

import es.jcyl.cee.cylford.app.cyldigital.MainActivity;
import es.jcyl.cee.cylford.app.cyldigital.R;


/**
 * Actividad para el "navegador web"
 */
public class WebContentActivity extends MainActivity implements OnClickListener {

    //botón atrás ventana
    private Button wback;
    //botón adelante ventana
    private Button wfoward;
    //objeto WebView
    private WebView web;
    private View loading;
    private String url = null;
    private SimpleSideDrawer slide_me;

    //anterior vista
    private ImageView back, btnMenuRight;

    private View itemPresentacion, itemFormativas, itemAcerca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_content);

        url = this.getIntent().getStringExtra("url");

        back = (ImageView) this.findViewById(R.id.back);
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
        web.getSettings().setUserAgentString(web.getSettings().getUserAgentString() + " " + "cylford");
        web.getSettings().setUseWideViewPort(true);
        web.getSettings().setLoadWithOverviewMode(true);
        web.setWebViewClient(new MyWebViewClient());
        web.loadUrl(url);

        //Botón de menú lateral
        btnMenuRight = (ImageView) this.findViewById(R.id.btnrightmenu);
        btnMenuRight.setOnClickListener(this);

        //Objeto que realiza la acción de mostrar el menú lateral
        slide_me = new SimpleSideDrawer(this);
        slide_me.setRightBehindContentView(R.layout.menu_right);

        itemPresentacion = this.findViewById(R.id.presentacion);
        itemPresentacion.setOnClickListener(this);

        itemAcerca = this.findViewById(R.id.acerca);
        itemAcerca.setOnClickListener(this);

        itemFormativas = this.findViewById(R.id.formativas);
        itemFormativas.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if( v == back) {
            this.onBackPressed();
        } else if( v == wback){
            if( web.canGoBack()) {
                web.goBack();
                wback.setEnabled(web.canGoBack());
            }else{
                wback.setEnabled(false);
            }
        } else if( v == wfoward) {
            if( web.canGoForward()){
                web.goForward();
                wfoward.setEnabled(web.canGoForward());
            } else {
                wfoward.setEnabled(false);
            }
        } else if (v == btnMenuRight) {
            slide_me.toggleRightDrawer();
        } else if (v == itemAcerca) {
            slide_me.closeRightSide();
            Intent i = new Intent(this, AcercaActivity.class);
            startActivity(i);
            //finish();
        } else if (v == itemPresentacion) {
            slide_me.closeRightSide();
            Intent i = new Intent(this, PresentacionActivity.class);
            startActivity(i);
            //finish();
        } else if (v == itemFormativas) {
            slide_me.closeRightSide();
            Intent i = new Intent(this, MenuFormacionActivity.class);
            startActivity(i);
            //finish();
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
