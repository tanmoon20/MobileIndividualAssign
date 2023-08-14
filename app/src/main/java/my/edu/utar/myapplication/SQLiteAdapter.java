package my.edu.utar.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class SQLiteAdapter {
    private Context context;

    public SQLiteAdapter(Context c){
        super();
        context = c;
    }

    private static final String DATABASE_NAME = "BillBreakDown";
    private static final String DATABASE_TABLE = "Bill";
    private static final String COLUMN1 = "DateTime";
    private static final String COLUMN2 = "Description";
    private static final String COLUMN3 = "Name";
    private static final String COLUMN4 = "Value";
    private static final String COLUMN5 = "Type";
    private static final String COLUMN6 = "Result";
    private static final String COLUMN7 = "Total";

    public static final int DATABASE_VERSION = 1;
    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase sqLiteDatabase;

    public static final String SCRIPT = "CREATE TABLE " + DATABASE_TABLE + "(id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN1 + " VARCHAR(30)," + COLUMN2 + " VARCHAR(50), " +  COLUMN3 + " VARCHAR(30), " + COLUMN4 + " VARCHAR(10), "
            + COLUMN5 + " VARCHAR(10), " + COLUMN6 + " VARCHAR(10), " + COLUMN7 + " VARCHAR(10));";

    public SQLiteAdapter openToRead() throws android.database.SQLException{
        sqLiteHelper = new SQLiteHelper (context, DATABASE_NAME, null, DATABASE_VERSION);
        sqLiteDatabase = sqLiteHelper.getReadableDatabase();
        return this;
    }

    public SQLiteAdapter openToWrite() throws android.database.SQLException{
        sqLiteHelper = new SQLiteHelper(context,DATABASE_NAME, null, DATABASE_VERSION);
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        sqLiteHelper.close();
    }

    public long insert(String col1, String col2, String col3, String col4, String col5, String col6, String col7){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN1, col1);
        contentValues.put(COLUMN2, col2);
        contentValues.put(COLUMN3, col3);
        contentValues.put(COLUMN4, col4);
        contentValues.put(COLUMN5, col5);
        contentValues.put(COLUMN6, col6);
        contentValues.put(COLUMN7, col7);
        return sqLiteDatabase.insert(DATABASE_TABLE, null, contentValues);
    }

    public int deleteAll(){
        return sqLiteDatabase.delete(DATABASE_TABLE, null, null);
    }

    public String queueAll(){
        String[] columns = new String[]{COLUMN1, COLUMN2, COLUMN3, COLUMN4, COLUMN5, COLUMN6, COLUMN7};
        Cursor cursor = sqLiteDatabase.query(DATABASE_TABLE, columns,null,null,null,null,null);
        String result = "";

        int col1 = cursor.getColumnIndex(COLUMN1);
        int col2 = cursor.getColumnIndex(COLUMN2);
        int col3 = cursor.getColumnIndex(COLUMN3);
        int col4 = cursor.getColumnIndex(COLUMN4);
        int col5 = cursor.getColumnIndex(COLUMN5);
        int col6 = cursor.getColumnIndex(COLUMN6);
        int col7 = cursor.getColumnIndex(COLUMN7);

        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
            result = result + cursor.getString(col1) + ","
                            + cursor.getString(col2) + ","
                            + cursor.getString(col3) + ","
                            + cursor.getString(col4) + ","
                            + cursor.getString(col5) + ","
                            + cursor.getString(col6) + ","
                            + cursor.getString(col7) + ",";
        }
        return result;
    }

    public class SQLiteHelper extends SQLiteOpenHelper {

        //constructor with 4 parameters
        public SQLiteHelper(@Nullable Context context, @Nullable String name,
                            @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        //create database
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(SCRIPT);
        }

        //version control
        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(SCRIPT);
        }
    }

}
