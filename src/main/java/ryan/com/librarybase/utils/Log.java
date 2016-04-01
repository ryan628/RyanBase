package ryan.com.librarybase.utils;


import java.util.Date;

/**
 * Created by ryanx on 2015/4/8.
 */
public class Log {
    public static void i(String message) {
        String[] infos = getAutoJumpLogInfos();
            LogUtils.i("I", "(" + infos[0] + ")" + infos[2] + infos[1] + " : " + message);
    }

    public static void v(String message) {
        String[] infos = getAutoJumpLogInfos();
            LogUtils.v("V", "(" + infos[0] + ")" + infos[2] + infos[1] + " : " + message);
    }

    public static void e(String message) {
        String[] infos = getAutoJumpLogInfos();
        LogUtils.e("E", "(" + infos[0] + ")" + infos[2] + infos[1] + " : " + message);
    }

    public static void d(String message) {
        String[] infos = getAutoJumpLogInfos();
//        if (BuildConfig.DEBUG)
        LogUtils.d("D", "(" + infos[0] + ")" + infos[2] + infos[1] + " : " + message);
    }

    public static void w(Object message) {
        String[] infos = getAutoJumpLogInfos();
//        if (BuildConfig.DEBUG)
        LogUtils.w("W", "(" + infos[0] + ")" + infos[2] + infos[1] + " : " + message);
    }

    /**
     * 获取打印信息所在方法名，行号等信息
     *
     * @return
     */
    private static String[] getAutoJumpLogInfos() {
        String[] infos = new String[]{"", "", ""};
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        if (elements.length < 5) {
            LogUtils.e("Log", "Stack is too shallow!!!");
            return infos;
        } else {
            infos[0] = Tools.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
            infos[1] = "[" + elements[4].getMethodName() + "()]";
            infos[2] = " " + elements[4].getClassName().replace("ryan.com.ryanapp.", "") + ":("
                    + elements[4].getLineNumber() + ") ";
            return infos;
        }
    }

}
