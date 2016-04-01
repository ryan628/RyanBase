package ryan.com.librarybase.db;



/**
 * 类描述
 * 创建人 Ryan
 * 创建时间 2015/6/16 17:48.
 */


public class MapArrayList<T extends Object> extends java.util.ArrayList<HashMap<T>> {
    private static final long serialVersionUID = 1L;

    @Override
    public boolean add(HashMap<T> taHashMap) {
        if (taHashMap != null) {
            return super.add(taHashMap);
        } else {
            return false;
        }
    }
}
