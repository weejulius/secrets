package jyu.secret.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by jyu on 15-5-16.
 */
public class DBSession {

    private final static DBSession ins = new DBSession();
    private DaoSession daoSession;

    public static DaoSession ins(Context context) {
        return initDB(context).daoSession;
    }


    private static DBSession initDB(Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "secret-db", null);

        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        ins.daoSession = daoMaster.newSession();
        return ins;
    }
}
