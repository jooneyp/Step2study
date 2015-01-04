package com.goodwebple.step2study;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

/**
 * Created by j8n3y on 15. 1. 3..
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DBHelper";

    private static final int DATABASE_VERSION = 2;
    private static final String DB_NAME = "dictionary.db";
    private static String DB_PATH = "";
    private static final String DICTIONARY_TABLE_NAME = "Wordlist";
    private static final String DICTIONARY_TABLE_CREATE =
            "CREATE TABLE Wordlist (" +
                    "idx INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "word TEXT, " +
                    "mean TEXT, " +
                    "is_correct_memorize_mode BOOLEAN, " +
                    "is_appeared_memorize_mode BOOLEAN, " +
                    "got_wrong_memorize_mode_cnt INTEGER, " +
                    "is_correct_challenge_mode BOOLEAN, " +
                    "is_appeared_challenge_mode BOOLEAN, " +
                    "got_wrong_challenge_mode_cnt INTEGER" +
                    ");";

    private SQLiteDatabase mDataBase;
    private final Context mContext;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 2);
//        if (Build.VERSION.SDK_INT >= 17) {
//            DB_PATH = context.getApplicationInfo() + "/databases/";
//        } else {
//            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
//        }
        DB_PATH = android.os.Environment.getExternalStorageDirectory() + "/databases/";
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if(isExternalStorageWritable()) {
            db.execSQL(DICTIONARY_TABLE_CREATE);
            Log.i(TAG, "Create Table Finished");
        } else {
            Log.i(TAG, "External Storage Write Denied");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public void createDataBase() throws IOException {
        boolean mDataBaseExist = checkDataBase();
        if(!mDataBaseExist) {
            this.getReadableDatabase();
            this.close();
            try {
                copyDataBase();
                Log.e(TAG, "createDataBase() : database created");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        Log.v("dbFile", dbFile + "   " + dbFile.exists());
        return dbFile.exists();
    }

    private void copyDataBase() throws IOException {
        InputStream mInput = mContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0) {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }


    public boolean openDataBase() throws SQLException {
        String mPath = DB_PATH + DB_NAME;
        Log.v("mPath", mPath);
        mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return mDataBase != null;
    }

    @Override
    public synchronized void close() {
        if(mDataBase != null)
            mDataBase.close();
        super.close();
    }

    /* USAGE
    DatabaseHelper mDbHelper = new DatabaseHelper(urContext);
    mDbHelper.createDatabase();
    mDbHelper.open();

    Cursor testdata = mDbHelper.getTestData();

    mDbHelper.close();
     */
}
