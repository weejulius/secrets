package jyu.secret.model;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import jyu.secret.model.Secret;
import jyu.secret.model.User;

import jyu.secret.model.SecretDao;
import jyu.secret.model.UserDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig secretDaoConfig;
    private final DaoConfig userDaoConfig;

    private final SecretDao secretDao;
    private final UserDao userDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        secretDaoConfig = daoConfigMap.get(SecretDao.class).clone();
        secretDaoConfig.initIdentityScope(type);

        userDaoConfig = daoConfigMap.get(UserDao.class).clone();
        userDaoConfig.initIdentityScope(type);

        secretDao = new SecretDao(secretDaoConfig, this);
        userDao = new UserDao(userDaoConfig, this);

        registerDao(Secret.class, secretDao);
        registerDao(User.class, userDao);
    }
    
    public void clear() {
        secretDaoConfig.getIdentityScope().clear();
        userDaoConfig.getIdentityScope().clear();
    }

    public SecretDao getSecretDao() {
        return secretDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

}