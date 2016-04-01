package ryan.com.librarybase.db;
/**
 * 类描述
 * 创建人 Ryan
 * 创建时间 2015/6/16 18:45.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelperMy extends SQLiteOpenHelper {

    private static final String DBNAME = "songpo.db";
    private final static int DB_VERSION = 1;// 默认数据库版本
    private DbHelperMy myDBHelper;
    private android.database.sqlite.SQLiteDatabase myDB;

    private final Context myContext;

    public DbHelperMy(Context context) {
        super(context, DBNAME, null, DB_VERSION);
        this.myContext = context;
    }

    @Override
    public void onCreate(android.database.sqlite.SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion,
                          int newVersion) {
    }

    public DbHelperMy open() throws SQLException {
        myDBHelper = new DbHelperMy(myContext);
        myDB = myDBHelper.getWritableDatabase();
        return this;
    }

    public Cursor ExecQuery(String query) {
        //myDBHelper.open();
        return myDB.rawQuery(query, null);
        //myDBHelper.close();

    }

    public void ExecSQL(String query) throws SQLException {

        myDB.execSQL(query);
    }

    public long insertSQL(String table, String nullColumnHack,
                          ContentValues values) throws SQLException {

        return myDB.insertOrThrow(table, nullColumnHack, values);

    }

    public int updateSQL(String table, ContentValues values,
                         String whereClause, String[] whereArgs) throws SQLException {

        return myDB.update(table, values, whereClause, whereArgs);

    }

    public Cursor ExecQueryParam(String query, String[] param)
            throws SQLException {
        //myDBHelper.open();
        return myDB.rawQuery(query, param);
        // myDBHelper.close();

    }

}
