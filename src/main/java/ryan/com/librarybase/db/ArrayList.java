package ryan.com.librarybase.db;
/**
 * 类描述
 * 创建人 Ryan
 * 创建时间 2015/6/16 17:46.
 */

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class ArrayList extends java.util.ArrayList<NameValuePair> {

    private static final long serialVersionUID = 1L;

    @Override
    public boolean add(NameValuePair nameValuePair) {
        if (!StringUtils.isEmpty(nameValuePair.getValue())) {
            return super.add(nameValuePair);
        } else {
            return false;
        }
    }

    /**
     * 添加数据
     *
     * @param key
     * @param value
     * @return
     */
    public boolean add(String key, String value) {
        return add(new BasicNameValuePair(key, value));
    }

}