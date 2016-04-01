package ryan.com.librarybase;
/**
 * 类描述
 * 创建人 Ryan
 * 创建时间 2015/9/18 10:31.
 */

public class AppSetting {
    public static String sendTo = "ryan@ryan628.com";//多个接收邮箱用逗号(,)隔开
    public static boolean SENDMSG = false;
    public static boolean SENDERRORMAIL = true;
    public static boolean LOGFILE = true;
    public static int LOG_LEVEL = 1;
    public static String getVersionHost = "http://www.duoduozhaopin.com:8080/apks/spjob.apk";
    public static boolean OPEN_CHECK = false;
    public static String sendMailHost = "http://home.ryan628.com:9904/ErrorMail/errorMail.do?method=deBug";
    public static String yinsi = "http://www.duoduozhaopin.com:8080/server-agreement.html";
    public static String BASE_URL;

    static {
            BASE_URL = "http://172.17.200.130:8442";
    }

}
