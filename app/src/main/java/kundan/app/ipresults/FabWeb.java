package kundan.app.ipresults;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by kkr on 27-07-2017.
 */

public class FabWeb extends AppCompatActivity {
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fabweb);
        WebView webView = (WebView)findViewById(R.id.webView);
        webView.loadUrl("http://outcome-ipu.herokuapp.com");
        webView.setWebViewClient(new MyBrowser());
        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.getBuiltInZoomControls();
        webSettings.getTextZoom();
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //  view.loadUrl(url);
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {

        }

    }

}
