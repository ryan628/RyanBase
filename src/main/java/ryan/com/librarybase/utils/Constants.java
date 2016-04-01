package ryan.com.librarybase.utils;

import org.apache.http.protocol.HTTP;

/**
 * Created by ryan on 16/3/15.
 */
public class Constants {
    public static final int GET = 1;
    public static final int POST = 2;
    public static final int PUT = 3;
    public static final int DELETE = 4;
    public static final int PATCH = 5;

    // 连接超时时间
    public static final int CONNECT_TIMEOUT = 10 * 1000;
    // 最大连接数
    public static final int MAX_CONNECTIONS = 15;
    // 失败重连次数
    public static final int MAX_RETRIES = 3;
    // 失败重连间隔时间
    public static final int RETRIES_TIMEOUT = 5 * 1000;
    // 响应超时时间
    public static final int RESPONSE_TIMEOUT = 10 * 1000;
    // 默认编码
    public static final String CONTENT_ENCODING = HTTP.UTF_8;
    // json Content-Type
    public static final String JSON_CONTENT_TYPE = "application/json";
    public final static int alertR = 27;
    public final static int alertG = 187;
    public final static int alertB = 187;
    public final static int alertRB = 255;
    public final static int alertGB = 255;
    public final static int alertBB = 255;
    public final static int alertRC = 168;
    public final static int alertGC = 168;
    public final static int alertBC = 168;
    public final static String patternCoder = "(?<!\\d)\\d{4}(?!\\d)";
}
