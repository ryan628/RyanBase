package ryan.com.librarybase;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDexApplication;
import ryan.com.librarybase.exception.CrashHandler;
import ryan.com.librarybase.utils.Utils;

import java.util.Locale;


/**
 * 自定义应用入口
 *
 * @author Ht
 */
public class BaseApplication extends MultiDexApplication {
    public static BaseApplication mInstance;
    private long t = 0;
    private Context fristContext;

    /**
     * @return
     */
    public Context getFristContext() {
        if (fristContext == null)
            fristContext = getApplicationContext();
        return fristContext;
    }

    public void setFristContext(Context fristContext) {
        this.fristContext = fristContext;
    }

    @Override
    public void onCreate() {
//        Tools.setMinHeapSize(256 * 1024 * 1024);
        t = System.currentTimeMillis();
        super.onCreate();
        mInstance = this;
        getVersion();
        getApp_version();
        Utils.init();
        CrashHandler crashHandler = CrashHandler.getInstance(); //注册crashHandler
        crashHandler.init(getApplicationContext()); //发送以前没发送的报告(可选)

    }



    public static BaseApplication getInstance() {
        return mInstance;
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersion() {
        try {
            PackageManager manager = mInstance.getPackageManager();
            PackageInfo info = manager.getPackageInfo(mInstance.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取当前系统语言
     *
     * @return 当前系统语言
     */
    public static String getLanguage() {
        Locale locale = mInstance.getResources().getConfiguration().locale;
        String language = locale.getDefault().toString();
        return language;
    }

    public void getApp_version() {
        PackageInfo info = null;
        PackageManager manager;
        manager = BaseApplication.getInstance().getFristContext().getPackageManager();
        String v ="";
        try {
            info = manager.getPackageInfo(BaseApplication.getInstance().getFristContext().getPackageName(), 0);
            v = (info.versionName);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Utils.vs = v;
    }
}
