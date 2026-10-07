package com.linkhub.app;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.*;
import androidx.webkit.WebViewAssetLoader;

public class MainActivity extends Activity {
  WebView wv; ValueCallback<Uri[]> cb;
  public class Bridge {
    @JavascriptInterface public void share(String t) {
      Intent i = new Intent(Intent.ACTION_SEND); i.setType("text/plain"); i.putExtra(Intent.EXTRA_TEXT, t);
      startActivity(Intent.createChooser(i, "Share"));
    }
  }
  @Override protected void onCreate(Bundle b) {
    super.onCreate(b);
    wv = new WebView(this); setContentView(wv);
    WebSettings s = wv.getSettings(); s.setJavaScriptEnabled(true); s.setDomStorageEnabled(true);
    final WebViewAssetLoader l = new WebViewAssetLoader.Builder()
      .addPathHandler("/assets/", new WebViewAssetLoader.AssetsPathHandler(this)).build();
    wv.setWebViewClient(new WebViewClient() {
      @Override public WebResourceResponse shouldInterceptRequest(WebView v, WebResourceRequest r) { return l.shouldInterceptRequest(r.getUrl()); }
      @Override public boolean shouldOverrideUrlLoading(WebView v, WebResourceRequest r) {
        if ("appassets.androidplatform.net".equals(r.getUrl().getHost())) return false;
        startActivity(new Intent(Intent.ACTION_VIEW, r.getUrl())); return true;
      }
    });
    wv.setWebChromeClient(new WebChromeClient() {
      @Override public boolean onShowFileChooser(WebView v, ValueCallback<Uri[]> c, FileChooserParams p) {
        if (cb != null) cb.onReceiveValue(null);
        cb = c;
        try { startActivityForResult(p.createIntent(), 1); } catch (Exception e) { cb = null; return false; }
        return true;
      }
    });
    wv.addJavascriptInterface(new Bridge(), "Android");
    wv.loadUrl("https://appassets.androidplatform.net/assets/index.html");
  }
  @Override protected void onActivityResult(int rq, int rs, Intent d) {
    if (rq == 1 && cb != null) { cb.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(rs, d)); cb = null; }
  }
  @Override public void onBackPressed() { if (wv.canGoBack()) wv.goBack(); else super.onBackPressed(); }
}
