package xyz.monkeytong.hongbao.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Zhongyi on 1/22/16.
 */
public class HongbaoLogger {
    private Context context;
    private SQLiteDatabase database;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "WeChatLuckyMoney.db";
    private static final String createDatabaseSQL = "CREATE TABLE IF NOT EXISTS HongbaoLog (id INTEGER PRIMARY KEY AUTOINCREMENT, sender TEXT, content TEXT, time TEXT, amount TEXT);";

    public HongbaoLogger(final Context context) {
        this.context = context;
        this.initSchemaAndDatabase();
    }

    private void initSchemaAndDatabase() {
        this.database = context.openOrCreateDatabase("WeChatLuckyMoney", context.MODE_PRIVATE, null);

        this.database.beginTransaction();
        this.database.execSQL(createDatabaseSQL);
        this.database.endTransaction();
    }

    public void writeHongbaoLog(String sender, String content, String amount) {

    }

    public void getAllLogs() {

    }
}
