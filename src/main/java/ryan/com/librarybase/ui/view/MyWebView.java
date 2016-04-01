package ryan.com.librarybase.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;
import ryan.com.librarybase.utils.Utils;

import java.io.File;

/**
 * Created by wgq on 16/2/3.
 */
@SuppressLint("NewApi")
public class MyWebView extends WebView
{
    public MyWebView(Context context) {
        super(context);
        init();
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

//    public WebViewConfig(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    void init() {
        try{
        getSettings().setMediaPlaybackRequiresUserGesture(false);
        }catch (Error e){} catch(Exception e){}
        getSettings().setJavaScriptEnabled(true);
//        getSettings().setSupportZoom(true);
        getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
//        getSettings().setGeolocationEnabled(true);
        getSettings().setGeolocationEnabled(false);

//        getSettings().setSupportMultipleWindows(true);
        getSettings().setUseWideViewPort(true);
//        getSettings().setBuiltInZoomControls(true);
        getSettings().setDomStorageEnabled(true);
        getSettings().setAppCacheEnabled(true);
        getSettings().setAllowFileAccess(true);
        getSettings().setDomStorageEnabled(true);
        getSettings().setAppCacheEnabled(true);
        getSettings().setAllowFileAccess(true);
        String cacheDirPath = Utils.filePathCache;
        getSettings().setDatabasePath(cacheDirPath); // API 19.getSettings().setAllowFileAccessFromFileURLs(true);
        getSettings().setAllowContentAccess(true);
        getSettings().setAllowUniversalAccessFromFileURLs(true);
    }




}
