package ryan.com.librarybase.net;

import ryan.com.librarybase.net.http.TextHttpResponseHandler;
import ryan.com.librarybase.utils.Constants;

public class TransactionHttpResponseHandler extends TextHttpResponseHandler {
    private TransactionListener listener;

    /**
     * 自定义http响应处理器
     *
     * @param listener
     */
    public TransactionHttpResponseHandler(TransactionListener listener) {
        this.listener = listener;
        setCharset(Constants.CONTENT_ENCODING);
    }

    @Override
    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
        listener.onFailure(ResponseCode.ERROR_NETWORK);
    }

    @Override
    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
        Response response = Response.getResponse(responseString);
        if (response.getCode() == ResponseCode.SUCCESS) {
            listener.onSuccess();
        } else {
            listener.onFailure(response.getCode());
        }
    }
}
