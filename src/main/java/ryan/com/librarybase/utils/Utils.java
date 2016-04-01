package ryan.com.librarybase.utils;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import ryan.com.librarybase.AppSetting;
import ryan.com.librarybase.BaseApplication;
import ryan.com.librarybase.R;
import ryan.com.librarybase.db.SQLiteDatabase;
import ryan.com.librarybase.db.SQLiteDatabasePool;
import ryan.com.librarybase.net.NetCenter;
import ryan.com.librarybase.qrcode.core.BarcodeFormat;
import ryan.com.librarybase.qrcode.core.EncodeHintType;
import ryan.com.librarybase.qrcode.core.WriterException;
import ryan.com.librarybase.qrcode.core.common.BitMatrix;
import ryan.com.librarybase.qrcode.core.qrcode.QRCodeWriter;

import java.io.File;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

/**
 * 类描述
 * 创建人 Ryan
 * 创建时间 2015/11/23 11:20.
 */
public class Utils {

    public static String filePath = "";
    public static String filePathCache = filePath+"/cache";
    public static int wWidth = 0;
    public static int wHeight = 0;
    private static Dialog dialog;
    public static SQLiteDatabase db;
    public static NetCenter asyncHttp;
    public static float density;
    public static Gson gson;
    public static String token="";
    public static String sendUserName="";
    public static String sendAppName="";

    public static void init() {

        String status1 = Environment.getExternalStorageState();
        File destDir = null;
        if (status1.equals(Environment.MEDIA_MOUNTED)) {
            filePath = (Environment.getExternalStorageDirectory() + "/ryan");
            destDir = new File(filePath);
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
            destDir = new File(filePath + "/voice");
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
        } else {
            filePath = ("/data/data/" + BaseApplication.getInstance().getFristContext().getPackageName() + "/ryan");
            destDir = new File(filePath);
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
            destDir = new File(filePath + "/voice");
            if (!destDir.exists()) {
                destDir.mkdirs();
            }
        }
        File file3 = new File(filePath + "/cache/");
        if (!file3.exists())
            file3.mkdirs();
        File file4 = new File(filePath + "/img/");
        if (!file4.exists())
            file4.mkdirs();
        File file6 = new File(filePath + "/files/");
        if (!file6.exists())
            file6.mkdirs();
        File file7 = new File(filePath + "/logs/");
        if (!file7.exists())
            file7.mkdirs();
        LogUtils.setLogFilePath(filePath + "/logs");

        gson = new Gson();

        ConfigUtil.file = filePath + "/appConfig.properties";
        ConfigUtil.getInstance();

        File file1 = new File(filePath + "/cache", "ryan_" + vs + ".apk");
        if (file1.exists()) {
            file1.delete();
        }
        asyncHttp = new NetCenter();
        db = getSQLiteDatabasePool().getSQLiteDatabase();
        LogUtils.setSyns(AppSetting.LOGFILE);
        density = BaseApplication.getInstance().getFristContext().getResources().getDisplayMetrics().density;
        WindowManager wm = (WindowManager) BaseApplication.getInstance().getFristContext().getSystemService(Context.WINDOW_SERVICE);
        wWidth = (wm.getDefaultDisplay().getWidth());
        wHeight = (wm.getDefaultDisplay().getHeight());
        Fresco.initialize(BaseApplication.getInstance().getFristContext());
        filePathCache = filePath+"/cache";
    }


    private static SQLiteDatabasePool mSQLiteDatabasePool;


    private static SQLiteDatabasePool getSQLiteDatabasePool() {
        if (mSQLiteDatabasePool == null) {
            mSQLiteDatabasePool = SQLiteDatabasePool.getInstance(BaseApplication.getInstance().getFristContext());
            mSQLiteDatabasePool.createPool();
        }
        return mSQLiteDatabasePool;
    }


    public static boolean isIntentAvailable(Context context, Intent intent, String modName) {
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.GET_ACTIVITIES);
        if (list.size() > 0) {
            Log.i("找到:" + modName + "开始跳转");
            return true;
        } else {
            Log.e(modName + "不存在,请在AndroidManifest配置");
            return false;
        }
    }

    /**
     * 得到自定义的progressDialog
     * context
     * msg
     *
     * @return
     */
    public static Dialog createLoadingDialog(Context context, String msg, boolean can) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
        RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.dialog_view);// 加载布局
        // main.xml中的ImageView
        ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        // 加载动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context, R.anim.loading_animation);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        tipTextView.setText(msg);// 设置加载信息
//        spaceshipImage = (ImageView) v.findViewById(R.id.img);
//        final AnimationDrawable loadingImageAnim = (AnimationDrawable) spaceshipImage.getDrawable();
//        spaceshipImage.post(new Runnable() {
//            @Override
//            public void run() {
//                loadingImageAnim.start();
//            }
//        });
        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

        loadingDialog.setCancelable(can);// 不可以用“返回键”取消
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT));// 设置布局

        return loadingDialog;
    }

    public boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 获取联系人电话
     *
     * @param cursor
     * @return
     */
    private String getContactPhone(Context context, Cursor cursor) {

        int phoneColumn = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
        int phoneNum = cursor.getInt(phoneColumn);
        String phoneResult = "";
        //System.out.print(phoneNum);
        if (phoneNum > 0) {
            // 获得联系人的ID号
            int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            String contactId = cursor.getString(idColumn);
            // 获得联系人的电话号码的cursor;
            Cursor phones = context.getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                    null, null);
            //int phoneCount = phones.getCount();
            //allPhoneNum = new ArrayList<String>(phoneCount);
            if (phones.moveToFirst()) {
                // 遍历所有的电话号码
                for (; !phones.isAfterLast(); phones.moveToNext()) {
                    int index = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    int typeindex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
                    int phone_type = phones.getInt(typeindex);
                    String phoneNumber = phones.getString(index);
                    switch (phone_type) {
                        case 2:
                            phoneResult = phoneNumber;
                            break;
                    }
                    //allPhoneNum.add(phoneNumber);
                }
                if (!phones.isClosed()) {
                    phones.close();
                }
            }
        }
        return phoneResult;
    }

    public static String vs="";


    public static String getVersionString() {
        String v = vs.substring(0, vs.lastIndexOf("_"));
        String t = vs.substring(vs.lastIndexOf("_") + 1);
        if (Tools.isInteger(t)) {
            v += "_" + Tools.formatDate(new Date(Long.parseLong(t)), "yyyy-MM-dd HH:mm:ss");
        }
        return v;
    }

    public String getVersionShout() {
        String v = vs.substring(0, vs.lastIndexOf("_"));
        return v;
    }

    public Bitmap createQRImage(String url) {
        int QR_WIDTH = Tools.dp2px(200);
        int QR_HEIGHT = Tools.dp2px(200);

        try {
            //判断URL合法性
            if (url == null || "".equals(url) || url.length() < 1) {
                return null;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            //下面这里按照二维码的算法，逐个生成二维码的图片，
            //两个for循环是图片横列扫描的结果
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }
            //生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = null;
            try {
                bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
                bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            } catch (OutOfMemoryError e) {
            }
            //显示到一个ImageView上面
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void showLoading() {
        showLoading(BaseApplication.getInstance().getFristContext(), "", false);
    }
    public static void showLoading(Context c) {
        showLoading(c, "", false);
    }

    public static void showLoading(Context c, boolean b) {
        showLoading(c, "", b);
    }

    public static void showLoading(Context c, String s, boolean b) {
        if (dialog== null) {
            dialog=(createLoadingDialog(c, s, b));
            try {
                dialog.show();
            } catch (Exception e) {
            }
        }
    }

    public static void showLoading(Context c, String s) {
        showLoading(c, s, false);
    }

    public static void hideLoading() {
        try {
            if (dialog != null && dialog.isShowing())
                dialog.dismiss();
        } catch (IllegalArgumentException e) {
        }
        dialog = (null);
    }
}
