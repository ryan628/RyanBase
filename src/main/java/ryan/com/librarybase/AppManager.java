package ryan.com.librarybase;

import android.app.Activity;
import android.content.Context;

import java.util.ConcurrentModificationException;
import java.util.Stack;

/**
 * 自定义Activity堆栈管理
 *
 * @author Ht
 *
 */
public class AppManager {
    private static Stack<Activity> mActivityStack;
    private static AppManager mInstance;

    private AppManager() {

    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (mInstance == null) {
            mInstance = new AppManager();
        }
        return mInstance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (mActivityStack == null) {
            mActivityStack = new Stack<Activity>();
        }
        mActivityStack.add(activity);
    }

	/**
	 * 获取当前Context（堆栈中最后一个压入的）
	 */
	public Context currentActivity() {
        try {
            Activity activity = mActivityStack.lastElement();
            return activity.getApplicationContext();
        }catch (Exception e)
        {
            return BaseApplication.mInstance.getFristContext();
        }
	}
	/**
	 * 获取所有Activity
	 */
	public Stack<Activity> allActivity() {
		return mActivityStack;
	}

    /**
     * 移除当前Activity（堆栈中最后一个压入的）
     */
    public void removeActivity() {
        Activity activity = mActivityStack.lastElement();
        removeActivity(activity);
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = mActivityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            mActivityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 移除指定的Activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null) {
            mActivityStack.remove(activity);
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        try {
            for (Activity activity : mActivityStack) {
                if (activity.getClass().equals(cls)) {
                    finishActivity(activity);
                }
            }
        }catch (ConcurrentModificationException e){e.printStackTrace();}
        catch (Exception e){e.printStackTrace();}
    }
    /**
     * 结束指定类名的Activity
     */
    public void finishActivityNoSelf(Class<?> cls) {
        for (Activity activity : mActivityStack) {
            if (!activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = mActivityStack.size(); i < size; i++) {
            if (null != mActivityStack.get(i)) {
                mActivityStack.get(i).finish();
            }
        }
        mActivityStack.clear();
    }

    /**
     *
     * 检查是否启动摸个Activity
     */
    public boolean isStartActivity(Class<?> cls)
    {
        boolean b = false;
        for (Activity activity : mActivityStack) {
            if (activity.getClass().equals(cls)) {
                b=true;
                break;
            }
        }
        return b;
    }
    /**
     * 退出应用程序
     *
     * @param context
     *            上下文
     * @param isBackground
     *            是否开开启后台运行
     */
    public void AppExit(Context context, Boolean isBackground) {
        finishAllActivity();
        // 注意，如果您有后台程序运行，请不要支持此句子
        if (!isBackground) {
            System.exit(0);
        }
    }
}
