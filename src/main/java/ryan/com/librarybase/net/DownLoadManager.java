package ryan.com.librarybase.net;

import android.app.ProgressDialog;
import ryan.com.librarybase.utils.Utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 类描述
 * 创建人 Ryan
 * 创建时间 15/7/18 下午8:16.
 */

public class DownLoadManager {
    public static File getFileFromServer(String path, ProgressDialog pd, String sv) throws Exception {
        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            //获取到文件的大小 
            pd.setMax(conn.getContentLength());
            InputStream is = conn.getInputStream();
            File file = new File(Utils.filePathCache, "/songpo_" + sv + ".apk");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                //获取当前下载量
                pd.setProgress(total);
            }
            fos.close();
            bis.close();
            is.close();
            return file;
//        } else {
//            return null;
//        }
    }
}
