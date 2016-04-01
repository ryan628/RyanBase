package ryan.com.librarybase.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * 类描述
 * 创建人 Ryan
 * 创建时间 2015/10/17 14:21.
 */

public class ConfigUtil {
    public static String file ="";// Utils.getInstance().getFilePath()+"/appConfig.properties";
    private Properties prop;
    private static ConfigUtil configUtil;
    public static ConfigUtil getInstance()
    {
        if (configUtil==null)
        {
            configUtil = new ConfigUtil();
        }
        return configUtil;
    }
    private ConfigUtil()
    {
        initConfig();
    }
    //读取配置文件
    private Properties loadConfig() {
        Properties properties = new Properties();
        File fil = new File(file);
        if (!fil.exists())
        {
            return null;
        }
        try {
            FileInputStream s = new FileInputStream(file);
            properties.load(s);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return properties;
    }

    //保存配置文件
    private boolean saveConfig() {
        try {
            File fil = new File(file);
            if (!fil.exists())
                fil.createNewFile();
            FileOutputStream s = new FileOutputStream(fil);
            prop.store(s, "");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void initConfig() {
        prop = loadConfig();
        if (prop == null) {
            //配置文件不存在的时候创建配置文件 初始化配置信息
            prop = new Properties();
            prop.put("version", "000.000");
            prop.put("isg", "true");
            saveConfig();
        }
    }
    public String getStringProp(String key)
    {
        Object o = prop.getProperty(key,"");
        return (String)o;
    }
    public int getIntProp(String key)
    {
        Object o = prop.getProperty(key,"0");
        return Integer.parseInt((String)o);
    }
    public long getLongProp(String key)
    {
        Object o = prop.getProperty(key,"0");
        return Long.parseLong((String)o);
    }
    public boolean getBooleanProp(String key)
    {
        Object o = prop.getProperty(key,"false");
        return (((String)o).equals("true"))?true:false;
    }
    public void setProp(String key,Object value)
    {
        if (value instanceof Integer) {
            int v = ((Integer) value).intValue();
            prop.put(key, v+"");
        } else if (value instanceof String) {
            prop.put(key, value);
        }else if (value instanceof Long) {
            long l = ((Long) value).longValue();
            prop.put(key, l+"");
        } else if (value instanceof Boolean) {
            boolean b = ((Boolean) value).booleanValue();
            prop.put(key, b+"");
        }else {return;}
        saveConfig();
    }
    public boolean needWelcome(String version)
    {
        boolean b=false;
        try {
            String oldVer = getStringProp("version");
            if (oldVer.length()<13)
            {
                prop.put("version", version);
                saveConfig();
                b = true;
            }else {
                if (!oldVer.substring(0, oldVer.indexOf("_") == -1 ? oldVer.length() - 1 : oldVer.indexOf("_")).equals(version.substring(0, version.indexOf("_") == -1 ? version.length() - 1 : version.indexOf("_")))) {
                    b = true;
                    prop.put("version", version);
                    saveConfig();
                }
            }
        }catch (Exception e)
        {
            prop.put("version", version);
            saveConfig();
            b = true;
            Log.e(e.toString());
//            e.printStackTrace();
        }
        return b;
    }
}
