package ryan.com.librarybase.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import butterknife.ButterKnife;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import cz.msebera.android.httpclient.Header;
import ryan.com.librarybase.AppManager;
import ryan.com.librarybase.AppSetting;
import ryan.com.librarybase.BaseApplication;
import ryan.com.librarybase.R;
import ryan.com.librarybase.modle.JumpIntentParam;
import ryan.com.librarybase.modle.Version;
import ryan.com.librarybase.net.DownLoadManager;
import ryan.com.librarybase.net.http.JsonHttpResponseHandler;
import ryan.com.librarybase.utils.Alert;
import ryan.com.librarybase.utils.Log;
import ryan.com.librarybase.utils.Tools;
import ryan.com.librarybase.utils.Utils;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;

public abstract class BaseActivity extends Activity implements IBaseView,View.OnClickListener,
        PullToRefreshBase.OnRefreshListener2<ListView> {
    private ProgressDialog mProgressDialog;
//    FragmentManager fragmentManager;
    public Context mContext;
    private String modName;
    private boolean isCreate = true;
    private static String serverVersion = "";
    private boolean canOnKeyDown = true;
    public Bundle savedInstanceState;
    public long startLong = 0;

    /**
     * 初始化布局
     */
    public abstract void initContentView();

    /**
     * 初始化控件
     */
    public abstract void initView();


    /**
     * 初始化监听事件
     */
    public abstract void initListener();

    /**
     * 全部初始化完毕
     */
    public abstract void afterOnCreate();

    @Override
    protected void onCreate(Bundle savedInstanceState1) {
        savedInstanceState = savedInstanceState1;
        super.onCreate(savedInstanceState1);
        startLong = System.currentTimeMillis();
        isCreate = true;
        mContext = this;
        modName = this.getClass().getSimpleName();
//        Log.e("打开:"+modName);
//        getWindow().setBackgroundDrawableResource(R.color.gray);
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initContentView();
        // 将该Activity加入堆栈
        AppManager.getAppManager().addActivity(this);
        // 初始化View注入
        ButterKnife.inject(this);
        initView();
        initListener();
        mHAfterOnCreate.sendEmptyMessageDelayed(0, 100);
    }

    @Override
    public void onClick(View view) {
    }

    private Handler mHAfterOnCreate = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    afterOnCreate();
                    if (AppSetting.OPEN_CHECK)
                        if (modName.equals("MainActivity") || modName.equals("SeekMainActivity") || modName.equals("Appstart"))
                            checkVersion();
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onDestroy() {
        AppManager.getAppManager().removeActivity(this);
        super.onDestroy();
        System.gc();
    }

//    /**
//     * 获取Fragment管理器
//     *
//     * @return
//     */
//    public FragmentManager getBaseFragmentManager() {
//        if (fragmentManager == null) {
//            fragmentManager = getSupportFragmentManager();
//        }
//        return fragmentManager;
//    }
//
//    /**
//     * 获取Fragment事物管理
//     *
//     * @return
//     */
//    public FragmentTransaction getFragmentTransaction() {
//        return getBaseFragmentManager().beginTransaction();
//    }
//
//    /**
//     * 替换一个Fragment
//     *
//     * @param res
//     * @param fragment
//     */
//    public void replaceFragment(int res, BaseFragment fragment) {
//        replaceFragment(res, fragment, false);
//    }
//
//    /**
//     * 替换一个Fragment并设置是否加入回退栈
//     *
//     * @param res
//     * @param fragment
//     * @param isAddToBackStack
//     */
//    public void replaceFragment(int res, BaseFragment fragment, boolean isAddToBackStack) {
//        FragmentTransaction fragmentTransaction = getFragmentTransaction();
//        fragmentTransaction.replace(res, fragment);
//        if (isAddToBackStack) {
//            fragmentTransaction.addToBackStack(null);
//        }
//        fragmentTransaction.commit();
//
//    }
//
//    /**
//     * 添加一个Fragment
//     *
//     * @param res
//     * @param fragment
//     */
//    public void addFragment(int res, Fragment fragment) {
//        FragmentTransaction fragmentTransaction = getFragmentTransaction();
//        fragmentTransaction.add(res, fragment);
//        fragmentTransaction.commit();
//    }
//
//    /**
//     * 移除一个Fragment
//     *
//     * @param fragment
//     */
//    public void removeFragment(Fragment fragment) {
//        FragmentTransaction fragmentTransaction = getFragmentTransaction();
//        fragmentTransaction.remove(fragment);
//        fragmentTransaction.commit();
//    }
//
//    /**
//     * 显示一个Fragment
//     *
//     * @param fragment
//     */
//    public void showFragment(Fragment fragment) {
//        if (fragment.isHidden()) {
//            FragmentTransaction fragmentTransaction = getFragmentTransaction();
//            fragmentTransaction.show(fragment);
//            fragmentTransaction.commit();
//        }
//    }
//
//    /**
//     * 隐藏一个Fragment
//     *
//     * @param fragment
//     */
//    public void hideFragment(Fragment fragment) {
//        if (!fragment.isHidden()) {
//            FragmentTransaction fragmentTransaction = getFragmentTransaction();
//            fragmentTransaction.hide(fragment);
//            fragmentTransaction.commit();
//        }
//    }

    @Override
    public void showProgress(boolean flag, String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(flag);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        } else {
            mProgressDialog.show();
        }
    }

    @Override
    public void showProgress(String message) {
        showProgress(true, message);
    }

    @Override
    public void showProgress() {
        showProgress(true);
    }

    @Override
    public void showProgress(boolean flag) {
        showProgress(flag, "");
    }

    @Override
    public void hideProgress() {
        if (mProgressDialog == null)
            return;

        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

    }

    @Override
    public void showToast(int resId) {
        showToast(getString(resId));
    }

    @Override
    public void showToast(String msg) {
        if (!isFinishing()) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void close() {
        finish();
    }

    public void jumpForResult(Class c, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(mContext, c);
        if (Utils.isIntentAvailable(mContext, intent, c.getName()))
            startActivityForResult(intent, requestCode);
        else
            Alert.show(mContext, c.getName() + ",请在AndroidManifest配置");
    }

    public void jumpForResult(Class c, int requestCode, JumpIntentParam... jp) {
        Intent intent = Tools.getVarIntent(jp);
        intent.setClass(mContext, c);

        if (Utils.isIntentAvailable(mContext, intent, c.getName()))
            startActivityForResult(intent, requestCode);
        else
            Alert.show(mContext, c.getName() + ",请在AndroidManifest配置");
    }

    public void jump(Class c) {
        jump(mContext, c);
    }

    public void jump(Class c, JumpIntentParam... jp) {
        jump(mContext, c, jp);
    }

    public void jump(Context c, Class cl) {
//        Log.i(this,new Exception(),this.getModuleName());
//        Utils.getInstance().showLoading(mContext);
        try {
            Intent intent = new Intent();
            intent.setClass(c, cl);
            if (Utils.isIntentAvailable(c, intent, cl.getName()))
                startActivity(intent);
            else
                Alert.show(c, cl.getName() + ",请在AndroidManifest配置");
        } catch (Exception e) {
            Log.e("跳转activity失败:" + e.toString());
        }
    }

    public void jump(Context c, Class cl, JumpIntentParam... jp) {
//        Log.i(this,new Exception(),this.getModuleName());
//        Utils.getInstance().showLoading(mContext);
        Intent intent = Tools.getVarIntent(jp);
        intent.setClass(c, cl);
        if (Utils.isIntentAvailable(c, intent, cl.getName()))
            startActivity(intent);
        else
            Alert.show(c, cl.getName() + ",请在AndroidManifest配置");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        Log.w("onKeyDown:" + keyCode);
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (canOnKeyDown) {
//                if (modName.equals("MainActivity")) {
//                    moveTaskToBack(true);
//                } else if (modName.equals("MyAlert")) {
//                } else {
                    close();
//                }
            }
        }
        return false;
    }

    public void backBase(View v) {
        close();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Alert.res:
                int type = data.getExtras()==null?0:data.getExtras().getInt("type",0);
                switch (resultCode) {
                    case 1:
                        yesBtn(type);
                        break;
                    case 2:
                        noBtn(type);
                        break;
                    case 3:
                        downLoadApk();
                        break;
                    case 4:
                        okBtn(type);
                        break;
                }
                break;
            default:
                break;
        }
    }

    protected void downLoadApk() {
        final ProgressDialog pd;    //进度条对话框
        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载更新");
        pd.show();
        new Thread() {
            @Override
            public void run() {
                try {
                    File file1 = new File(Utils.filePathCache, "/ryan_" + serverVersion + ".apk");
                    if (file1.exists()) {
                        installApk(file1);
                        pd.dismiss();
                    } else {
                        File file = DownLoadManager.getFileFromServer(AppSetting.getVersionHost, pd, serverVersion);
                        sleep(1500);
                        installApk(file);
                        pd.dismiss(); //结束掉进度条对话框
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(e.getMessage());
                    Alert.show(mContext, "下载失败");
                    pd.dismiss();
                }
            }
        }.start();
    }

    //安装apk
    protected void installApk(File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
//        moveTaskToBack(true);
        Log.e("程序退出啦:" + "安装更新");
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private void checkVersion() {
        if (AppSetting.OPEN_CHECK) {
            Utils.asyncHttp.get(AppSetting.BASE_URL + "/getversion", new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    String con = response.optString("body");
                    Log.w(con);
                    if (!con.equals("null")) {
                        Version v = Utils.gson.fromJson(con, Version.class);
                        String content = v.getApkVsersion();
                        String n = Utils.vs;
                        Log.e("服务器版本:" + content + " 本地版本:" + n);
                        if (!n.trim().equals(content.trim())) {
                            String v1 = content.trim().substring(0, content.trim().lastIndexOf("_"));
                            String t1 = content.trim().substring(content.trim().lastIndexOf("_") + 1);
                            if (Tools.isInteger(t1)) {
                                v1 += "_" + Tools.formatDate(new Date(Long.parseLong(t1)), "yyyy-MM-dd HH:mm:ss");
                            }
                            String v2 = n.substring(0, n.lastIndexOf("_"));
                            String t2 = n.substring(n.lastIndexOf("_") + 1);
                            if (Tools.isInteger(t2)) {
                                v2 += "_" + Tools.formatDate(new Date(Long.parseLong(t2)), "yyyy-MM-dd HH:mm:ss");
                            }

                            String bigVersion1 = content.trim().split("_")[0].replace(".", "");
                            String smallVersion1 = content.trim().split("_")[1];
                            String bigVersion2 = n.split("_")[0].replace(".", "");
                            String smallVersion2 = n.split("_")[1];
                            if (Tools.isInteger(bigVersion1) && Tools.isInteger(smallVersion1)
                                    && Tools.isInteger(bigVersion2) && Tools.isInteger(smallVersion2)) {
                                int bv1 = Integer.parseInt(bigVersion1);
                                int bv2 = Integer.parseInt(bigVersion2);
                                long sv1 = Long.parseLong(smallVersion1);
                                long sv2 = Long.parseLong(smallVersion2);
                                boolean isForcibly = v.getNeedUpdate().equals("Y");
                                if (bv1 > bv2) {
                                    Log.w("服务器版本号:" + v1 + "(" + t1 + ")" + " 客户端版本号:" + v2 + "(" + t2 + ")");
                                    serverVersion = content.trim();
                                    if (isForcibly)
                                        Alert.show(BaseActivity.this, new JumpIntentParam("content", getResources().getString(R.string.NewVersion)), new JumpIntentParam("update", 1), new JumpIntentParam("okName", "更新"));
                                    else
                                        Alert.show(BaseActivity.this,new JumpIntentParam("content", getResources().getString(R.string.NewVersion)),
                                                new JumpIntentParam("type", "2"), new JumpIntentParam("customName", "更新"));
                                } else if (bv1 == bv2 && sv1 > sv2) {
                                    Log.w("服务器版本号:" + v1 + "(" + t1 + ")" + " 客户端版本号:" + v2 + "(" + t2 + ")");
                                    serverVersion = content.trim();
                                    if (isForcibly)
                                        Alert.show(BaseActivity.this, new JumpIntentParam("content", getResources().getString(R.string.NewVersion)), new JumpIntentParam("update", 1));
                                    else
                                        Alert.show(BaseActivity.this,  new JumpIntentParam("content", getResources().getString(R.string.NewVersion)),
                                                new JumpIntentParam("type", "2"), new JumpIntentParam("customName", "更新"));
                                }
                            } else {
//                                Log.w("服务器版本号:" + v1 + "(" + t1 + ")" + " 客户端版本号:" + v2 + "(" + t2 + ")");
//                                serverVersion = content.trim();
//                                Alert.showMyAlert(BaseActivity.this, new JumpIntentParam("content", getResources().getString(R.string.NewVersion)), new JumpIntentParam("update", 1));
                            }
                        }
                    }
                }
            });
        } else {
        }
    }


    @Override
    public void onRestart() {
        super.onRestart();
    }

    @Override
    public void onResume() {
        super.onResume();
        BaseApplication.getInstance().setFristContext(mContext);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        listDown();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        listUp();
    }

    //点击弹框单个确定按钮发生的事件
    public void okBtn(int type) {}
    public void yesBtn(int type) {}
    public void noBtn(int type) {}
    public void listUp(){}
    public void listDown(){}

    public String getIntentString(String key)
    {
        return getIntent().getExtras()==null?"":getIntent().getExtras().getString(key,"");
    }
    public int getIntentInt(String key)
    {
        return getIntent().getExtras()==null?0:getIntent().getExtras().getInt(key,0);
    }
    public boolean getIntentBoolean(String key)
    {
        return getIntent().getExtras()==null?false:getIntent().getExtras().getBoolean(key,false);
    }
    public Serializable getIntentSerializable(String key)
    {
        return getIntent().getExtras()==null?null:getIntent().getExtras().getSerializable(key);
    }
}
