package ryan.com.librarybase.utils;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.*;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import ryan.com.librarybase.AppSetting;
import ryan.com.librarybase.BaseApplication;
import ryan.com.librarybase.modle.JumpIntentParam;
import ryan.com.librarybase.modle.SIMCardInfo;
import ryan.com.librarybase.net.http.RequestParams;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类描述
 * 创建人 Ryan
 * 创建时间 2015/11/23 13:12.
 */

public class Tools {
    /**
     * 图片缩放(非等比例)

     * bitmap  :资源图片
     * mWidth  ：新图片的宽
     * mHeight ：新图片的高
     *
     * @return Bitmap
     */
    public static Bitmap resizeImage(Bitmap bitmap, int mWidth, int mHeight) {
        if (null == bitmap)
            return null;
        // 获取这个图片的宽和高
//        com.songpo.android.base.util.Log.i(new WhosvUtil(), new Exception(), "bitmap(" + bitmap.toString() + ")压缩前大小:" + bitmap.getRowBytes() + "宽:" + bitmap.getWidth() + "高:" + bitmap.getHeight());
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (mWidth == -1 && mHeight == -1) {
            mWidth = width / 5;
            mHeight = height / 5;
        }
        // 计算缩放率，新尺寸除原始尺寸
        float scaleWidth = (Float.parseFloat(mWidth + "")) / width;
        float scaleHeight = (Float.parseFloat(mHeight + "")) / height;
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        // 创建新的图片
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int options = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            int tmp = 0;
            while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
                baos.reset();
                if (options > 0)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
                else
                    break;
                if (baos.toByteArray().length / 1024 == tmp)
                    break;
                tmp = baos.toByteArray().length / 1024;
                options -= 20;
            }
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
            baos = null;
            return bitmap;
        } catch (Exception e) {
            Log.e("resizeImage:" + e.toString());
            return bitmap;
        }

    }
    /**
     * 根据图片的url路径获得Bitmap对象
     * @param url
     * @return
     */
    public static Bitmap returnBitmap(String url) {
        URL fileUrl = null;
        Bitmap bitmap = null;

        try {
            fileUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) fileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        catch (OutOfMemoryError e)
        {
            e.printStackTrace();
        }
        return bitmap;

    }
    /**
     * 转换图片成圆形

     * bitmap 传入Bitmap对象
     *
     * @return
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            float roundPx;
            float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
            if (width <= height) {
                roundPx = width / 2;
                top = 0;
                bottom = width;
                left = 0;
                right = width;
                height = width;
                dst_left = 0;
                dst_top = 0;
                dst_right = width;
                dst_bottom = width;
            } else {
                roundPx = height / 2;
                float clip = (width - height) / 2;
                left = clip;
                right = width - clip;
                top = 0;
                bottom = height;
                width = height;
                dst_left = 0;
                dst_top = 0;
                dst_right = height;
                dst_bottom = height;
            }
            Bitmap output = Bitmap.createBitmap(width,
                    height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
            final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
            final RectF rectF = new RectF(dst);
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, src, dst, paint);
            return output;
        } else
            return null;
    }

    public static String millisecondFormat(Long millisecond, String format) {
        if (millisecond == null || millisecond <= 0) {
            Log.e(String.format("传入的时间毫秒数[%s]不合法", "" + millisecond));
            return "";
        }
        if (format == null || "".equals(format.trim())) {
            format = "yyyy-MM-dd";
        }
        return formatDate(new Date(millisecond), format);
    }

    public static boolean isInteger(String value) {
        for (int i = value.length(); --i >= 0; ) {
            int chr = value.charAt(i);
            if (chr < 48 || chr > 57)
                return false;
        }
        return true;
    }

    public static Timestamp getCurTimestamp() {
        return new Timestamp(new Date().getTime());
    }

    /**
     * <p>将文件转成base64 字符串</p>

     * path 文件路径
     *
     * @return
     * @throws Exception
     */
    public static String encodeBase64File(String path) {
        File file = new File(path);
        byte[] buffer = null;
        try {
            FileInputStream inputFile = new FileInputStream(file);
            buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            inputFile.close();
        } catch (Exception e) {
            Log.e("将文件转成base64 字符串:" + e.toString());
        }
//        return new android.util.Base64;
//        Log.e(new WhosvUtil(),new Exception(),"------------"+file+"====");
        return android.util.Base64.encodeToString(buffer, android.util.Base64.DEFAULT);
    }

    /**
     * <p>将base64字符解码保存文件</p>

     * base64Code
     * targetPath
     *
     * @throws Exception
     */
    public static void decoderBase64File(String base64Code, String targetPath) {
        byte[] baseByte = android.util.Base64.decode(base64Code, android.util.Base64.DEFAULT);
        try {
//        byte[] buffer = new BASE64Decoder().decodeBuffer(base64Code);
            FileOutputStream out = new FileOutputStream(targetPath);
            out.write(baseByte);
            out.close();
        } catch (Exception e) {
            Log.e("将base64字符解码保存文件:" + e.toString());
        }
    }

    public static String getByte(long l) {
        String s = "";
        if (l > 1024 * 1024 * 1024) {
            s = (String.format("%.2f",(((double)l / (double)1024 / (double)1024 / (double)1024 * (double)100)) / (double)100.0)) + "GB";
        } else if (l > 1024 * 1024) {
            s = ((String.format("%.2f",(((double)l / (double)1024 / (double)1024 * (double)100)) / (double)100.0))) + "MB";
        } else if (l > 1024) {
            s = ((String.format("%.2f",(((double)l / (double)1024 * (double)100)) / (double)100.0))) + "KB";
        } else {
            s = l + "B";
        }
        return s;
    }

    /**
     * bitmap转为base64
     * bitmap
     *
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = android.util.Base64.encodeToString(bitmapBytes, android.util.Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * base64转为bitmap
     * base64Data
     *
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = android.util.Base64.decode(base64Data, android.util.Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * StringBase64加密
     **/
    public static String getBase64(String str) {
        byte[] b = null;
        String s = null;
        try {
            b = str.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (b != null) {
            s = new BASE64Encoder().encode(b);
        }
        return s;
    }

    /**
     * StringBase64解密
     **/
    public static String getFromBase64(String s) {
        byte[] b = null;
        String result = null;
        if (s != null) {
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                b = decoder.decodeBuffer(s);
                result = new String(b, "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 获取格式化对象
     * strFormat 格式化的格式 如"yyyy-MM-dd"
     *
     * @return 格式化对象
     */
    public static SimpleDateFormat getSimpleDateFormat(String strFormat) {
        if (strFormat != null && !"".equals(strFormat.trim())) {
            return new SimpleDateFormat(strFormat);
        } else {
            return new SimpleDateFormat();
        }
    }

    /**
     * 将java.util.date型按照指定格式转为字符串
     * date  源对象
     * format 想得到的格式字符串
     *
     * @return 如：2010-05-28
     */
    public static String formatDate(Date date, String format) {
        return getSimpleDateFormat(format).format(date);
    }


    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * TODO根据指定的图像路径和大小来获取缩略图
     * 此方法有两点好处：
     * 1. 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
     * 第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。
     * 2. 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使
     * 用这个工具生成的图像不会被拉伸。
     * imagePath 图像的路径
     * width 指定输出图像的宽度
     * height 指定输出图像的高度
     *
     * @return 生成的缩略图
     */
    public static Bitmap getImageThumbnail(String imagePath) {
        return getImageThumbnail(imagePath, 5);
    }

    public static Bitmap getImageThumbnail(String imagePath, int suo) {
        File f = new File(imagePath);
        if (!f.exists()) {
            return null;
        }
        int width = 0;
        int height = 0;
        Bitmap bitmap = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            // 获取这个图片的宽和高，注意此处的bitmap为null
            bitmap = BitmapFactory.decodeFile(imagePath, options);
            options.inJustDecodeBounds = false; // 设为 false
            // 计算缩放比
            int h = options.outHeight;
            int w = options.outWidth;
            width = w / suo;
            height = h / suo;
            int beWidth = w / width;
            int beHeight = h / height;
            int be = 1;
            if (beWidth < beHeight) {
                be = beWidth;
            } else {
                be = beHeight;
            }
            if (be <= 0) {
                be = 1;
            }
            options.inSampleSize = be;
            // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
            bitmap = BitmapFactory.decodeFile(imagePath, options);
            // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        } catch (OutOfMemoryError e) {
        }
        return bitmap;
    }
    public static String getChinaMonth(String month) {
        int m = 0;
        try {
            m = Integer.parseInt(month);
            switch (m) {
                case 1:
                    month = "一月";
                    break;
                case 2:
                    month = "二月";
                    break;
                case 3:
                    month = "三月";
                    break;
                case 4:
                    month = "四月";
                    break;
                case 5:
                    month = "五月";
                    break;
                case 6:
                    month = "六月";
                    break;
                case 7:
                    month = "七月";
                    break;
                case 8:
                    month = "八月";
                    break;
                case 9:
                    month = "九月";
                    break;
                case 10:
                    month = "十月";
                    break;
                case 11:
                    month = "十一月";
                    break;
                case 12:
                    month = "十二月";
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
        }
        return month;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

//    public static void setListViewHeightBasedOnChildren(ListView listView, int gao) {
//        ListAdapter listAdapter = listView.getAdapter();
//        if (listAdapter == null) {
//            return;
//        }
//        int totalHeight = 0;
//        for (int i = 0; i < listAdapter.getCount(); i++) {
//            View listItem = listAdapter.getView(i, null, listView);
//            //listItem.measure(0, 0);
//            totalHeight += dp2px(gao);//listItem.getMeasuredHeight();
//        }
//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.height = totalHeight
//                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//        listView.setLayoutParams(params);
//    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(int dpValue) {
        final float scale = Utils.density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(int pxValue) {
        final float scale = Utils.density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static String md5(String s) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(s.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString().toUpperCase();
    }

    /**
     * 判断email格式是否正确
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }
    /**
     * 判断网站格式是否正确
     *
     * @return
     */
//    public static boolean isWww(String www) {
//        String str = "^(http|www|ftp|)?(://)?(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*((:\\d+)?)(/(\\w+(-\\w+)*))*(\\.?(\\w)*)(\\?)?(((\\w*%)*(\\w*\\?)*(\\w*:)*(\\w*\\+)*(\\w*\\.)*(\\w*&)*(\\w*-)*(\\w*=)*(\\w*%)*(\\w*\\?)*(\\w*:)*(\\w*\\+)*(\\w*\\.)*(\\w*&)*(\\w*-)*(\\w*=)*)*(\\w*)*)$";
//        Pattern p = Pattern.compile(str);
//        Matcher m = p.matcher(www);
//        return m.matches();
//    }
//    public static boolean checkNetWorkStatus(Context context){
//        boolean result;
//        ConnectivityManager cm=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo netinfo = cm.getActiveNetworkInfo();
//        if ( netinfo !=null && netinfo.isConnected() ) {
//            result=true;
//            Log.i(TAG, "The net was connected" );
//        }else{
//            result=false;
//            Log.i(TAG, "The net was bad!");
//        }
//        return result;
//    }
    public static boolean isWww(String url){
        boolean value=false;
        try {
            HttpURLConnection conn=(HttpURLConnection)new URL(url).openConnection();
            int code=conn.getResponseCode();
            Log.w(">>>>>>>>>>>>>>>> "+code+" <<<<<<<<<<<<<<<<<<");
            if(code!=200){
                value=false;
            }else{
                value=true;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }
    /**
     s判断字符串是否有特殊字符
     */
    public static boolean isConSpeCharacters(String string){
        if(string.replaceAll("[\u4e00-\u9fa5]*[a-z]*[A-Z]*\\d*-*_*\\s*","").length()==0){
            return false;//没有
        }
        return true;
    }
    /**
     * 根据系统语言判断是否为中国
     *
     * @return
     */
    public static boolean isZh() {
        Locale locale = BaseApplication.getInstance().getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.startsWith("zh")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Try to return the absolute file path from the given Uri
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }


    /**
     * 调用震动器
     *
     * @param context      调用该方法的Context
     * @param milliseconds 震动的时长，单位是毫秒
     */
    public static void vibrate(final Context context, long milliseconds) {
        Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }

    /**
     * 调用震动器
     *
     * @param context  调用该方法的Context
     * @param pattern  自定义震动模式 。数组中数字的含义依次是[静止时长，震动时长，静止时长，震动时长。。。]时长的单位是毫秒
     * @param isRepeat 是否反复震动，如果是true，反复震动，如果是false，只震动一次
     */
    public static void vibrate(final Context context, long[] pattern, boolean isRepeat) {
        Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(pattern, isRepeat ? 1 : -1);
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmap(Bitmap bm, int degree) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 用移动CMNET方式
     * getExtraInfo 的值是cmnet
     * 用移动CMWAP方式
     * getExtraInfo 的值是cmwap
     * 用联通3gwap方式
     * getExtraInfo 的值是3gwap
     * 用联通3gnet方式
     * getExtraInfo 的值是3gnet
     * 用联通uniwap方式
     * getExtraInfo 的值是uniwap
     * 用联通uninet方式
     * getExtraInfo 的值是uninet
     *
     * @param ex
     * @param mContext
     * @return
     */
    public static String sendErrorMail(final Throwable ex, final Context mContext) {
//        Toast.makeText(mContext, "程序崩溃了...休息,休息一会", Toast.LENGTH_SHORT).show();
        StackTraceElement[] trace = ex.getCause() == null ? ex.getStackTrace() : ex.getCause().getStackTrace();
        String t = "";
        for (int i = 0; i < trace.length; i++) {
            t += "at:" + trace[i].getClassName() + "." + trace[i].getMethodName() + "(" + trace[i].getLineNumber() + ")" + "\n ";
        }
        if (AppSetting.SENDERRORMAIL) {
            String account = Utils.sendUserName;
            SIMCardInfo siminfo = new SIMCardInfo(mContext);
            String mobile_desc = android.os.Build.MODEL;
            String mobile_sdk = android.os.Build.VERSION.SDK;
            String mobile_f = android.os.Build.BRAND;
            String mobile_sys = android.os.Build.VERSION.RELEASE;
            String version_number = Utils.getVersionString();

            ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manager.getActiveNetworkInfo();

            String pm = "("+Utils.sendAppName+")程序崩溃了..." + "(" + formatDate(new Date(), "yyyy-MM-dd HH:mm:ss") + ")" +
                    "\n 手机型号:(" + mobile_f + ")" + mobile_desc + "\n SDK版本:" + mobile_sdk +
                    "\n 系统版本:" + mobile_sys + "\n 软件版本:" + version_number +
                    "\n 电话号码:" + siminfo.getNativePhoneNumber() + "\n 运营商:" + siminfo.getProvidersName() +
                    "\n 联网方式:" + info.getTypeName() + "(" + info.getExtraInfo() + ")" +
                    "\n" + ex.toString();
            String log = "";
//            File f = new File(Utils.getInstance().getFilePath() + "/logs/songpo.log");
//            try {
//                BufferedReader br = new BufferedReader(new FileReader(f));//构造一个BufferedReader类来读取文件
//                String s = null;
//                while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
//                    log = log + "\n" + s;
//                }
//                br.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            errorMsg = pm + "\n" + t;
            Log.e(pm);
            ex.printStackTrace();
            RequestParams params1 = new RequestParams();
            params1.put("bug", pm + "\n " + t + "\n\n\n\n\n\n\n" + log);
            params1.put("account", "(" + mobile_f + ")" + mobile_desc + " " + (account.equals("") ? "无用户信息" : "用户:" + account));
            params1.put("sendto", AppSetting.sendTo);
            Utils.asyncHttp.post(AppSetting.sendMailHost, params1);
            //sendBugEmail(account.equals("") ? "无用户信息" : "用户:" + account, pm + "\n " + t + "\n\n\n\n\n\n\n" + log, "");
        } else {
            ex.printStackTrace();
        }
        return t;
    }
    public static String getSysVersion() {
        String mobile_desc = android.os.Build.MODEL;
        String mobile_sdk = android.os.Build.VERSION.SDK;
        String mobile_f = android.os.Build.BRAND;
        String mobile_sys = android.os.Build.VERSION.RELEASE;
        String version_number = Utils.getVersionString();
        return "手机型号:(" + mobile_f + ")" + mobile_desc + " SDK版本:" + mobile_sdk + " 系统版本:" + mobile_sys + " 软件版本:" + version_number;
    }
    /**
     * 判断手机号码是否合理
     *
     * @param phoneNums
     */
    public static boolean checkPhoneNums(String phoneNums) {
        if (isMatchLength(phoneNums, 11)
                && isMobileNO(phoneNums)) {
            return true;
        }
        return false;
    }

    /**
     * 判断一个字符串的位数
     *
     * @param str
     * @param length
     * @return
     */
    public static boolean isMatchLength(String str, int length) {
        if (str.isEmpty()) {
            return false;
        } else {
            return str.length() == length ? true : false;
        }
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobileNums) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
        String telRegex = "[1][34578]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }
    public static long getMillionSeconds(String year) {
        String str = year + "01010000";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmm");
        long millionSeconds = 0;
        try {
            millionSeconds = sdf.parse(str).getTime();
        } catch (Exception e) {
        }
        return millionSeconds;
    }
    public static long getMillionSecondsByString(String year) {
        String str = year;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmm");
        long millionSeconds = 0;
        try {
            millionSeconds = sdf.parse(str).getTime();
        } catch (Exception e) {
        }
        return millionSeconds;
    }

    public static String getPhoneStar(String s) {
        String p = "";
        if (s!=null) {
            if (s.length() > 3) {
                if (checkPhoneNums(s)) {
                    p = s.substring(0, 3) + "****" + s.substring(6, s.length() - 1);
                } else {
                    p = s.substring(0, 3);
                }
            }
        }
        return p;
    }

    public static int random(int min, int max) {
        Random random = new Random();
        return random.nextInt(max) % (max - min + 1) + min;
    }

    public static String inputStream2String(InputStream in) {
        StringBuffer out = new StringBuffer();
        try {

            byte[] b = new byte[4096];
            for (int n; (n = in.read(b)) != -1; ) {
                out.append(new String(b, 0, n));
            }
        } catch (Exception e) {
        }
        return out.toString();
    }
    public static Intent getVarIntent(JumpIntentParam... jp)
    {
        Intent intent = new Intent();
        for (JumpIntentParam param : jp) {
            if (param!=null) {
                Object param1 = param.getPvalue();
                if (param1 instanceof Integer) {
                    int value = ((Integer) param1).intValue();
                    intent.putExtra(param.getPname(), value);
                } else if (param1 instanceof int[]) {
                    if (param1.getClass().getComponentType().getName().equals("int"))
                        intent.putExtra(param.getPname(), (int[]) param1);
                } else if (param1 instanceof Object[]) {
//                int[] value = ((int[]) param1);
                    if (param1.getClass().getComponentType().getName().equals("int"))
                        intent.putExtra(param.getPname(), (int[]) param1);
                    else if (param1.getClass().getComponentType().getName().equals("java.lang.String"))
                        intent.putExtra(param.getPname(), (String[]) param1);
                } else if (param1 instanceof String) {
                    String s = (String) param1;
                    intent.putExtra(param.getPname(), s);
                } else if (param1 instanceof Double) {
                    double d = ((Double) param1).doubleValue();
                    intent.putExtra(param.getPname(), d);
                } else if (param1 instanceof Float) {
                    float f = ((Float) param1).floatValue();
                    intent.putExtra(param.getPname(), f);
                } else if (param1 instanceof Long) {
                    long l = ((Long) param1).longValue();
                    intent.putExtra(param.getPname(), l);
                } else if (param1 instanceof Boolean) {
                    boolean b = ((Boolean) param1).booleanValue();
                    intent.putExtra(param.getPname(), b);
                }
            }
        }
        return intent;
    }

    public static void applyFont(final Context context, final View root, final String fontName) {
        try {
            if (root instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) root;
                for (int i = 0; i < viewGroup.getChildCount(); i++)
                    applyFont(context, viewGroup.getChildAt(i), fontName);
            } else if (root instanceof TextView)
                ((TextView) root).setTypeface(Typeface.createFromAsset(context.getAssets(), fontName));
        } catch (Exception e) {
            Log.e(String.format("Error occured when trying to apply %s font for %s view", fontName, root));
            e.printStackTrace();
        }
    }
    public static void hideKeyBoard(View view,Context context)
    {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    /** * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases) * * @param context */
    public static void cleanDatabases(Context context) {
        deleteFilesByDirectory(new File("/data/data/"+context.getPackageName()+"/databases"));
        deleteFilesByDirectory(new File("/data/data/"+context.getPackageName()+"/shared_prefs"));
    }
    /** * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * * @param directory */
    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }
}
