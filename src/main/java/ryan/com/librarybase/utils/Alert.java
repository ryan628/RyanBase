package ryan.com.librarybase.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import ryan.com.librarybase.BaseApplication;
import ryan.com.librarybase.modle.JumpIntentParam;
import ryan.com.librarybase.ui.BaseActivity;
import ryan.com.librarybase.ui.BaseAlert;


/**
 * Created by ryanx on 2015/4/9.
 */
public class Alert {
    public final static int res = 65535;

    /**
     * 弹窗提示
     *
     * @param msg(内容)
     */
    public static void show(String msg) {
        show(BaseApplication.getInstance().getFristContext(), new JumpIntentParam("type", "1"), new JumpIntentParam("content", msg));
    }
    /**
     * 弹窗提示
     *
     * @param ob(Context)
     * @param msg(内容)
     */
    public static void show(Context ob, String msg) {
        show(ob, new JumpIntentParam("type", "1"), new JumpIntentParam("content", msg));
    }


    /**
     * 弹窗提示(可定义标题)
     *
     * @param ob(Context)
     * @param title(标题)
     * @param msg(内容)
     */
    public static void show(Context ob, String title, String msg) {
        show(ob, new JumpIntentParam("type", "1"), new JumpIntentParam("title", title), new JumpIntentParam("content", msg));
    }

    /**
     * 浮层提示
     *
     * @param ob(Context)
     * @param content(内容)
     */
    public static void showToast(Context ob, String content) {
        Toast.makeText(ob, content, Toast.LENGTH_SHORT).show();
    }

    public static void show(Context ob, JumpIntentParam... jp) {
        Intent intent= Tools.getVarIntent(jp);
        intent.setClass(ob, BaseAlert.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ob.startActivity(intent);
    }

    public static void show(BaseActivity ob, String msg,int type) {
        Intent intent = Tools.getVarIntent(new JumpIntentParam("type", "1"), new JumpIntentParam("content", msg), new JumpIntentParam("finish_type", type));
        intent.setClass(ob.mContext, BaseAlert.class);
        ob.startActivityForResult(intent, res);
    }
    public static void show(BaseActivity ob, String title, String msg,int type) {
        Intent intent = Tools.getVarIntent(new JumpIntentParam("type", "1"), new JumpIntentParam("title", title), new JumpIntentParam("content", msg), new JumpIntentParam("finish_type", type));
        intent.setClass(ob.mContext, BaseAlert.class);
        ob.startActivityForResult(intent, res);
    }
    public static void show(BaseActivity ob,JumpIntentParam... jp) {
        Intent intent = Tools.getVarIntent(jp);
        intent.setClass(ob.mContext, BaseAlert.class);
        ob.startActivityForResult(intent, res);
    }
}
