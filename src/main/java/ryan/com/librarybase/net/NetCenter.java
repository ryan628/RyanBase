package ryan.com.librarybase.net;

import android.content.Context;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;
import ryan.com.librarybase.AppManager;
import ryan.com.librarybase.BaseApplication;
import ryan.com.librarybase.net.http.AsyncHttpClient;
import ryan.com.librarybase.net.http.AsyncHttpResponseHandler;
import ryan.com.librarybase.net.http.JsonHttpResponseHandler;
import ryan.com.librarybase.net.http.RequestParams;
import ryan.com.librarybase.utils.Alert;
import ryan.com.librarybase.utils.Constants;
import ryan.com.librarybase.utils.Log;
import ryan.com.librarybase.utils.Tools;

import java.util.Collections;
import java.util.Map;

/**
 * 网络访问控制中心 用于统一管理网络访问接口及相关配置
 *
 * @author Ht
 */
public class NetCenter {



    /**
     */
    private static AsyncHttpClient client ;

    public NetCenter()
    {
        client = new AsyncHttpClient();
        // 设置连接超时时间
        client.setConnectTimeout(Constants.CONNECT_TIMEOUT);
        // 设置最大连接数
        client.setMaxConnections(Constants.MAX_CONNECTIONS);
        // 设置重连次数以及间隔时间
        client.setMaxRetriesAndTimeout(Constants.MAX_RETRIES, Constants.RETRIES_TIMEOUT);
        // 设置响应超时时间
        client.setResponseTimeout(Constants.RESPONSE_TIMEOUT);

        initBaseHeader();
    }

    /**
     * 初始化公共请求头
     */
    public void initBaseHeader() {
        client.addHeader("Source", BaseRequestHeader.getSource());
        client.addHeader("App-Version", BaseRequestHeader.getAppVersion());
        client.addHeader("Device-Model", BaseRequestHeader.getDeviceModel());
        client.addHeader("Sys-Version", BaseRequestHeader.getSysVersion());
        client.addHeader("Language", BaseRequestHeader.getLanguage());
        client.addHeader("Longitude", String.valueOf(BaseRequestHeader.getLongitude()));
        client.addHeader("Latitude", String.valueOf(BaseRequestHeader.getLatitude()));
    }

    /**
     * 添加一个请求头
     *
     * @param key
     * @param value
     */
    public void setHeader(String key, String value) {
        client.addHeader(key, value);
    }

    /**
     * 清除所有请求头
     */
    public void removeAllHeaders() {
        client.removeAllHeaders();
    }

    /**
     * 发起带参数的get请求
     *
     * @param url             请求路径
     * @param t               继承自BaseRequest的请求参数实体类
     * @param responseHandler 响应回调
     */
    public <T extends BaseRequest> void get(int i, String url, T t,
                                                   AsyncHttpResponseHandler responseHandler) {

        sendRequest(i, url, t, responseHandler);
    }

    /**
     * 发起带参数的get请求
     *
     * @param url
     * @param entity
     * @param contentType
     * @param responseHandler
     */
    public void get(String url, String entity, String contentType,
                           AsyncHttpResponseHandler responseHandler) {
        StringEntity stringEntity = null;

        try {
            stringEntity = new StringEntity(entity, Constants.CONTENT_ENCODING);
        } catch (Exception e) {
            e.printStackTrace();
        }

        sendRequest(Constants.GET, url, stringEntity, contentType, responseHandler);
    }

    /**
     * 发起带参数的get请求
     *
     * @param url
     * @param entity
     * @param responseHandler
     */
    public void get(String url, String entity, AsyncHttpResponseHandler responseHandler) {
        StringEntity stringEntity = null;

        try {
            stringEntity = new StringEntity(entity, Constants.CONTENT_ENCODING);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sendRequest(Constants.GET, url, stringEntity, "application/json; charset=UTF-8", responseHandler);
    }

    /**
     * 发起带参数get请求
     *
     * @param url             请求路径
     * @param params          以map形式存储的参数
     * @param responseHandler 响应回调
     */
    public void get(String url, RequestParams params,
                           AsyncHttpResponseHandler responseHandler) {

        sendRequest(Constants.GET, url, true, params, responseHandler);
    }

    /**
     * 发起带参数get请求
     *
     * @param url             请求路径
     * @param params          以map形式存储的参数
     * @param responseHandler 响应回调
     */
    public void get(String url, boolean b, RequestParams params,
                           AsyncHttpResponseHandler responseHandler) {

        sendRequest(Constants.GET, url, b, params, responseHandler);
    }

    /**
     * 发起不带参数get请求
     *
     * @param url             请求路径
     * @param responseHandler 响应回调
     */
    public void get(String url, AsyncHttpResponseHandler responseHandler) {
        get(Constants.GET, url, null, responseHandler);
    }

    /**
     * 发起带参数post请求
     *
     * @param url             请求路径
     * @param t               继承自BaseRequest的请求参数实体类
     * @param responseHandler 响应回调
     */
    public <T extends BaseRequest> void post(String url, T t,
                                                    AsyncHttpResponseHandler responseHandler) {

        sendRequest(Constants.POST, url, t, responseHandler);
    }

    /**
     * 发起带参数post请求
     *
     * @param url             请求路径
     * @param params          以map形式存储的参数
     * @param responseHandler 响应回调
     */
    public void post(String url, Map<String, String> params,
                            AsyncHttpResponseHandler responseHandler) {

        sendRequest(Constants.POST, url, params, responseHandler);
    }

    /**
     * 发起带参数无回调post请求
     *
     * @param url    请求路径
     * @param params 以map形式存储的参数
     */
    public void post(String url, RequestParams params) {

        sendRequest(Constants.POST, url, true, params, new JsonHttpResponseHandler() {
        });
    }

    /**
     * 发起带参数无回调post请求
     *
     * @param url    请求路径
     * @param params 以map形式存储的参数
     */
    public void post(String url, boolean b, RequestParams params) {

        sendRequest(Constants.POST, url, b, params, new JsonHttpResponseHandler() {
        });
    }

    /**
     * 发起带参数post请求
     *
     * @param url             请求路径
     * @param params          以map形式存储的参数
     * @param responseHandler 响应回调
     */
    public void post(String url, RequestParams params,
                            AsyncHttpResponseHandler responseHandler) {

        sendRequest(Constants.POST, url, true, params, responseHandler);
    }

    /**
     * 发起带参数post请求
     *
     * @param url             请求路径
     * @param params          以map形式存储的参数
     * @param responseHandler 响应回调
     */
    public void post(String url, boolean b, RequestParams params,
                            AsyncHttpResponseHandler responseHandler) {

        sendRequest(Constants.POST, url, b, params, responseHandler);
    }

    /**
     * 发起带请求体的post请求
     *
     * @param url
     * @param entity
     * @param responseHandler
     */
    public void post(String url, String entity, AsyncHttpResponseHandler responseHandler) {
        try {
            StringEntity se = new StringEntity(entity, "UTF-8");
            post(url, se, "application/json; charset=UTF-8", responseHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 发起带请求体的post请求
     *
     * @param url
     * @param entity
     */
    public void post(String url, String entity) {
        try {
            StringEntity se = new StringEntity(entity, "UTF-8");
            post(url, se, "application/json; charset=UTF-8", new JsonHttpResponseHandler() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 发起带请求体的post请求
     *
     * @param url
     * @param entity
     * @param contentType
     * @param responseHandler
     */
    public void post(String url, StringEntity entity, String contentType,
                            AsyncHttpResponseHandler responseHandler) {
//        StringEntity stringEntity = null;
//
//        try {
//            stringEntity = new StringEntity(entity, CONTENT_ENCODING);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

        sendRequest(Constants.POST, url, entity, contentType, responseHandler);
    }/**
     * 发起带请求体的post请求
     *
     * @param url
     * @param entity
     * @param contentType
     * @param responseHandler
     */
    public void post(String url, RequestParams entity, String contentType,
                            AsyncHttpResponseHandler responseHandler) {
//        StringEntity stringEntity = null;
//
//        try {
//            stringEntity = new StringEntity(entity, CONTENT_ENCODING);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

        sendRequest(Constants.POST, url, entity, contentType, responseHandler);
    }

    /**
     * 发起不带参数post请求
     *
     * @param url             请求路径
     * @param responseHandler 响应回调
     */
    public void post(String url, AsyncHttpResponseHandler responseHandler) {

        post(url, Collections.<String, String>emptyMap(), responseHandler);
    }

    /////////////////////////////////////////////////////

    /**
     * 发起带参数put请求
     *
     * @param url             请求路径
     * @param t               继承自BaseRequest的请求参数实体类
     * @param responseHandler 响应回调
     */
    public <T extends BaseRequest> void put(String url, T t,
                                                   AsyncHttpResponseHandler responseHandler) {

        sendRequest(Constants.PUT, url, t, responseHandler);
    }

    /**
     * 发起带参数put请求
     *
     * @param url             请求路径
     * @param params          以map形式存储的参数
     * @param responseHandler 响应回调
     */
    public void put(String url, Map<String, String> params,
                           AsyncHttpResponseHandler responseHandler) {

        sendRequest(Constants.PUT, url, params, responseHandler);
    }

    /**
     * 发起带参数无回调put请求
     *
     * @param url    请求路径
     * @param params 以map形式存储的参数
     */
    public void put(String url, RequestParams params) {

        sendRequest(Constants.PUT, url, true, params, new JsonHttpResponseHandler() {
        });
    }

    /**
     * 发起带参数put请求
     *
     * @param url             请求路径
     * @param params          以map形式存储的参数
     * @param responseHandler 响应回调
     */
    public void put(String url, RequestParams params,
                           AsyncHttpResponseHandler responseHandler) {

        sendRequest(Constants.PUT, url, true, params, responseHandler);
    }

    /**
     * 发起带请求体的put请求
     *
     * @param url
     * @param entity
     * @param responseHandler
     */
    public void put(String url, String entity, AsyncHttpResponseHandler responseHandler) {
        try {
            StringEntity se = new StringEntity(entity, "UTF-8");
            put(url, se, "application/json; charset=UTF-8", responseHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 发起带请求体的put请求
     *
     * @param url
     * @param entity
     * @param contentType
     * @param responseHandler
     */
    public void put(String url, StringEntity entity, String contentType,
                           AsyncHttpResponseHandler responseHandler) {

        sendRequest(Constants.PUT, url, entity, contentType, responseHandler);
    }

    /**
     * 发起不带参数put请求
     *
     * @param url             请求路径
     * @param responseHandler 响应回调
     */
    public void put(String url, AsyncHttpResponseHandler responseHandler) {

        put(url, Collections.<String, String>emptyMap(), responseHandler);
    }

    /////////////////////////////////////////////////////

    /**
     * 发起带参数delete请求
     *
     * @param url             请求路径
     * @param params          以map形式存储的参数
     * @param responseHandler 响应回调
     */
    public void delete(String url, Map<String, String> params,
                              AsyncHttpResponseHandler responseHandler) {

        sendRequest(Constants.DELETE, url, params, responseHandler);
    }

    /**
     * 发起带参数delete请求
     *
     * @param url             请求路径
     * @param params          以map形式存储的参数
     * @param responseHandler 响应回调
     */
    public void delete(String url, RequestParams params,
                              AsyncHttpResponseHandler responseHandler) {

        sendRequest(Constants.DELETE, url, true, params, responseHandler);
    }

    /**
     * 发起带请求体的delete请求
     *
     * @param url
     * @param entity
     * @param contentType
     * @param responseHandler
     */
    public void delete(String url, String entity, String contentType,
                              AsyncHttpResponseHandler responseHandler) {
        StringEntity stringEntity = null;

        try {
            stringEntity = new StringEntity(entity, Constants.CONTENT_ENCODING);
        } catch (Exception e) {
            e.printStackTrace();
        }

        sendRequest(Constants.DELETE, url, stringEntity, contentType, responseHandler);
    }

    /////////////////////////////////////////////////////

    /**
     * 发起带参数patch请求
     *
     * @param url             请求路径
     * @param t               继承自BaseRequest的请求参数实体类
     * @param responseHandler 响应回调
     */
    public <T extends BaseRequest> void patch(String url, T t,
                                                     AsyncHttpResponseHandler responseHandler) {

        sendRequest(Constants.PATCH, url, t, responseHandler);
    }

    /**
     * 发起带参数patch请求
     *
     * @param url             请求路径
     * @param params          以map形式存储的参数
     * @param responseHandler 响应回调
     */
    public void patch(String url, Map<String, String> params,
                             AsyncHttpResponseHandler responseHandler) {

        sendRequest(Constants.PATCH, url, params, responseHandler);
    }

    /**
     * 发起带参数无回调patch请求
     *
     * @param url    请求路径
     * @param params 以map形式存储的参数
     */
    public void patch(String url, RequestParams params) {

        sendRequest(Constants.PATCH, url, true, params, new JsonHttpResponseHandler() {
        });
    }

    /**
     * 发起带参数patch请求
     *
     * @param url             请求路径
     * @param params          以map形式存储的参数
     * @param responseHandler 响应回调
     */
    public void patch(String url, RequestParams params,
                             AsyncHttpResponseHandler responseHandler) {

        sendRequest(Constants.PATCH, url, true, params, responseHandler);
    }

    /**
     * 发起带请求体的patch请求
     *
     * @param url
     * @param entity
     * @param responseHandler
     */
    public void patch(String url, String entity, AsyncHttpResponseHandler responseHandler) {
        try {
            StringEntity se = new StringEntity(entity, "UTF-8");
            patch(url, se, "application/json; charset=UTF-8", responseHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 发起带请求体的patch请求
     *
     * @param url
     * @param entity
     * @param contentType
     * @param responseHandler
     */
    public void patch(String url, StringEntity entity, String contentType,
                             AsyncHttpResponseHandler responseHandler) {

        sendRequest(Constants.PATCH, url, entity, contentType, responseHandler);
    }

    /**
     * 发起不带参数patch请求
     *
     * @param url             请求路径
     * @param responseHandler 响应回调
     */
    public void patch(String url, AsyncHttpResponseHandler responseHandler) {

        patch(url, Collections.<String, String>emptyMap(), responseHandler);
    }


    /**
     * 发起网络请求
     *
     * @param type            请求类型
     * @param url             请求路径
     * @param t               请求参数
     * @param responseHandler 响应回调
     */
    private <T extends BaseRequest> void sendRequest(int type,
                                                            String url, T t, AsyncHttpResponseHandler responseHandler) {
        Map<String, String> params = null;

        // 将传入的请求实体类映射成Map
        if (t != null) {
            params = NetUtils.getParams(t);
        }

        sendRequest(type, url, params, responseHandler);
    }

    /**
     * 发起可设置参数的网络请求
     *
     * @param type            请求类型
     * @param url             请求路径
     * @param params          请求参数
     * @param responseHandler 响应回调
     */
    private void sendRequest(int type,
                                    String url, Map<String, String> params, AsyncHttpResponseHandler responseHandler) {
        // 将Map转换成请求参数
        RequestParams requestParams = new RequestParams(params);
        requestParams.setContentEncoding(Constants.CONTENT_ENCODING);

        // 获取当前页面的Context
        Context context = AppManager.getAppManager().currentActivity();

        // 判断网络是否可用
        if (!NetUtils.isNetworkConnected(context)) {
            er();
            return;
        }
        Log.w("url(" + getType(type) + "):" + AsyncHttpClient.getUrlWithQueryString(true, url, requestParams));
        // 根据传入类型调用不同请求方法,可自行扩展
        // 传入Context以便与生命周期联动
        switch (type) {
            case Constants.GET:
                // 发起get请求,获取请求处理器
                client.get(context, url, requestParams, responseHandler);
                break;
            case Constants.POST:
                // 发起post请求,获取请求处理器
                client.post(context, url, requestParams, responseHandler);
                break;
            case Constants.PUT:
                // 发起put请求,获取请求处理器
                // .....
                client.put(context, url, requestParams, responseHandler);
                break;
            case Constants.DELETE:
                // 发起delete请求,获取请求处理器
                client.delete(context, url, new Header[]{}, requestParams, responseHandler);
                break;
            case Constants.PATCH:
                // 发起patch请求,获取请求处理器
                client.patch(context, url, requestParams, responseHandler);
                break;
            default:
                // 默认发起get请求
                client.get(context, url, requestParams, responseHandler);
                break;
        }
    }

    /**
     * 发起可设置参数的网络请求
     *
     * @param type            请求类型
     * @param url             请求路径
     * @param requestParams   请求参数
     * @param responseHandler 响应回调
     */
    private void sendRequest(int type,
                                    String url, boolean b, RequestParams requestParams, AsyncHttpResponseHandler responseHandler) {
        // 将Map转换成请求参数
        requestParams.setContentEncoding(Constants.CONTENT_ENCODING);

        // 获取当前页面的Context
        Context context = AppManager.getAppManager().currentActivity();

        // 判断网络是否可用
        if (!NetUtils.isNetworkConnected(context)) {
            er();
            //responseHandler.onFailure(0, null, null, null);
            return;
        }
        try {
            String fullUrl = Tools.inputStream2String(requestParams.getEntity(responseHandler).getContent());
            // 根据传入类型调用不同请求方法,可自行扩展
            // 传入Context以便与生命周期联动
            Log.w("url(" + getType(type) + "):" + url+"?"+fullUrl);
        }catch (Exception e){}
        switch (type) {
            case Constants.GET:
                // 发起get请求,获取请求处理器
                client.get(context, url, requestParams, responseHandler);
                break;
            case Constants.POST:
                // 发起post请求,获取请求处理器
                client.post(context, url, requestParams, responseHandler);
                break;
            case Constants.PUT:
                // 发起put请求,获取请求处理器
                client.put(context, url, requestParams, responseHandler);
                break;
            case Constants.DELETE:
                // 发起delete请求,获取请求处理器
                client.delete(context, url, new Header[]{}, requestParams, responseHandler);
                break;
            case Constants.PATCH:
                // 发起patch请求,获取请求处理器
                client.patch(context, url, requestParams, responseHandler);
                break;
            default:
                // 默认发起get请求
                client.get(context, url, requestParams, responseHandler);
                break;
        }
    }
    /**
     * 发起可设置参数的网络请求
     *
     * @param type            请求类型
     * @param url             请求路径
     * @param requestParams   请求参数
     * @param responseHandler 响应回调
     */
    private void sendRequest(int type,
                                    String url, RequestParams requestParams, String contentType, AsyncHttpResponseHandler responseHandler) {
        // 将Map转换成请求参数
        requestParams.setContentEncoding(Constants.CONTENT_ENCODING);

        // 获取当前页面的Context
        Context context = AppManager.getAppManager().currentActivity();

        // 判断网络是否可用
        if (!NetUtils.isNetworkConnected(context)) {
            er();
            //responseHandler.onFailure(0, null, null, null);
            return;
        }
        try {
            String fullUrl = Tools.inputStream2String(requestParams.getEntity(responseHandler).getContent());
            // 根据传入类型调用不同请求方法,可自行扩展
            // 传入Context以便与生命周期联动
            Log.w("url(" + getType(type) + "):" + url+"?"+fullUrl);
        }catch (Exception e){}
        switch (type) {
            case Constants.POST:
                // 发起post请求,获取请求处理器
                client.post(context, url, requestParams,contentType, responseHandler);
                break;
        }
    }

    /**
     * 发起可设置请求体的网络请求
     *
     * @param type            请求类型
     * @param url             请求路径
     * @param entity          请求体
     * @param contentType     Content-Type
     * @param responseHandler
     */
    private void sendRequest(int type,
                             String url, HttpEntity entity, String contentType, AsyncHttpResponseHandler responseHandler) {
        // 获取当前页面的Context
        Context context = AppManager.getAppManager().currentActivity();

        // 判断网络是否可用
        if (!NetUtils.isNetworkConnected(context)) {
            er();
            return;
        }
        try {
            Log.w("url(" + getType(type) + "):" + url + Tools.inputStream2String(entity.getContent()));
        } catch (Exception e) {
        }
        // 根据传入类型调用不同请求方法,可自行扩展
        // 传入Context以便与生命周期联动
        switch (type) {
            case Constants.GET:
                // 发起get请求,获取请求处理器
                client.get(context, url, entity, contentType, responseHandler);
                break;
            case Constants.POST:
                // 发起post请求,获取请求处理器
                client.post(context, url, entity, contentType, responseHandler);
                break;
            case Constants.PUT:
                // 发起put请求,获取请求处理器
                // .....
                client.put(context, url, entity, contentType, responseHandler);
                break;
            case Constants.DELETE:
                // 发起delete请求,获取请求处理器
                client.delete(context, url, entity, contentType, responseHandler);
                break;
            case Constants.PATCH:
                // 发起patch请求,获取请求处理器
                client.patch(context, url, entity, contentType, responseHandler);
                break;
            default:
                // 默认发起get请求
                client.get(context, url, entity, contentType, responseHandler);
                break;
        }
    }

    /**
     * 取消当前Context的请求队列
     */
    public void clearRequestQueue() {
        Context context = AppManager.getAppManager().currentActivity();
        // 销毁指定Context的请求, 第二个参数true代表强制结束
        client.cancelRequests(context, true);
    }

    private void er() {
        Alert.show(BaseApplication.mInstance.getFristContext(), "请检查网络");
    }

    private String getType(int type)
    {
        String s="GET";
        switch (type) {
            case Constants.GET:
                s = "GET";
                break;
            case Constants.POST:
                s = "POST";
                break;
            case Constants.PUT:
                s = "PUT";
                break;
            case Constants.DELETE:
                s = "DELETE";
                break;
            case Constants.PATCH:
                s = "PATCH";
                break;
            default:
                s = "GET";
                break;
        }
        return s;
    }
}
