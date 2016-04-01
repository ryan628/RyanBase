package ryan.com.librarybase.db;

/**
 * 类描述
 * 创建人 Ryan
 * 创建时间 2015/6/16 17:50.
 */


public class DBException extends Exception {
    private static final long serialVersionUID = 1L;

    public DBException() {
        super();
    }

    public DBException(String detailMessage) {
        super(detailMessage);
    }

}