package jyu.secret;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import jyu.secret.model.DBSession;
import jyu.secret.model.DaoMaster;
import jyu.secret.model.DaoSession;

/**
 * Created by jyu on 15-5-16.
 */
public class SecretApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        DBSession.ins(this);
    }


}
