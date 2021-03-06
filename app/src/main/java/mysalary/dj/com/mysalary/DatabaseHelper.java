package mysalary.dj.com.mysalary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, "mySalary", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE table salary(month DATE, amount DECIMAL(6,2))");
        db.execSQL("CREATE table spending(month DATE, amount DECIMAL(6,2), category text, place text, claimable boolean)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists salary");
        db.execSQL("drop table if exists spending");
    }

    public boolean insertSpending(String date, double amount, String category, boolean claimable, String place){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("month",date);
        contentValues.put("amount",amount);
        contentValues.put("category",category);
        contentValues.put("claimable",claimable);
        contentValues.put("place", place);
        long ins = db.insert("spending",null,contentValues);
        if(ins==-1)
            return false;
        else
            return true;

    }

    public Cursor readSpending(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM spending;";
        return db.rawQuery(query,null);
    }

    public Cursor readSpending(String dateYear){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM spending WHERE month LIKE '"+dateYear+"%';";
        return db.rawQuery(query,null);
    }

    public Cursor readMonth(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT (strftime('%Y',month)||'/'||strftime('%m',month)) as month FROM spending GROUP BY strftime('%Y',month), strftime('%m',month);";
        return db.rawQuery(query,null);
    }
}
