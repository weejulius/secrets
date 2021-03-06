package jyu.secret.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import jyu.secret.model.User;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table USER.
*/
public class UserDao extends AbstractDao<User, Long> {

    public static final String TABLENAME = "USER";

    /**
     * Properties of entity User.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "ID");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Rnd = new Property(2, String.class, "rnd", false, "RND");
        public final static Property Pwd = new Property(3, String.class, "pwd", false, "PWD");
        public final static Property CreatedDate = new Property(4, java.util.Date.class, "createdDate", false, "CREATED_DATE");
        public final static Property UpdatedDate = new Property(5, java.util.Date.class, "updatedDate", false, "UPDATED_DATE");
    };


    public UserDao(DaoConfig config) {
        super(config);
    }
    
    public UserDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'USER' (" + //
                "'ID' INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE ," + // 0: id
                "'NAME' TEXT UNIQUE ," + // 1: name
                "'RND' TEXT," + // 2: rnd
                "'PWD' TEXT," + // 3: pwd
                "'CREATED_DATE' INTEGER," + // 4: createdDate
                "'UPDATED_DATE' INTEGER);"); // 5: updatedDate
        // Add Indexes
        db.execSQL("CREATE INDEX " + constraint + "IDX_USER_ID ON USER" +
                " (ID);");
        db.execSQL("CREATE INDEX " + constraint + "IDX_USER_NAME ON USER" +
                " (NAME);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'USER'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, User entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String rnd = entity.getRnd();
        if (rnd != null) {
            stmt.bindString(3, rnd);
        }
 
        String pwd = entity.getPwd();
        if (pwd != null) {
            stmt.bindString(4, pwd);
        }
 
        java.util.Date createdDate = entity.getCreatedDate();
        if (createdDate != null) {
            stmt.bindLong(5, createdDate.getTime());
        }
 
        java.util.Date updatedDate = entity.getUpdatedDate();
        if (updatedDate != null) {
            stmt.bindLong(6, updatedDate.getTime());
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public User readEntity(Cursor cursor, int offset) {
        User entity = new User( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // rnd
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // pwd
            cursor.isNull(offset + 4) ? null : new java.util.Date(cursor.getLong(offset + 4)), // createdDate
            cursor.isNull(offset + 5) ? null : new java.util.Date(cursor.getLong(offset + 5)) // updatedDate
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, User entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setRnd(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setPwd(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setCreatedDate(cursor.isNull(offset + 4) ? null : new java.util.Date(cursor.getLong(offset + 4)));
        entity.setUpdatedDate(cursor.isNull(offset + 5) ? null : new java.util.Date(cursor.getLong(offset + 5)));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(User entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(User entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
