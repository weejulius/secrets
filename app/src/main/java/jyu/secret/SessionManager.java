package jyu.secret;

import android.content.Context;

import java.util.Date;
import java.util.List;

import jyu.secret.model.DBSession;
import jyu.secret.model.Secret;
import jyu.secret.model.User;

/**
 * Created by jyu on 15-5-21.
 */
public class SessionManager {

    private final static SessionManager ins = new SessionManager();

    private Encrypts encrypts = Encrypts.ins();

    private boolean isLogined;
    private String userName = "jyu";
    private Long userId = 0l;

    private SessionManager() {
    }

    public static SessionManager ins() {
        return ins;
    }


    public boolean isLogined() {
        return isLogined;
    }

    public void setIsLogined(boolean isLogined) {
        this.isLogined = isLogined;
    }


    public boolean tryLogin(Context context, final String pwd) {

        if (pwd == null) {
            return false;
        }

        final List<User> users = DBSession.ins(context).getUserDao().queryRaw("where name=? ", userName);

        final boolean r = users != null && users.size() == 1;

        if (r) {

            final User user = users.get(0);

            if (user.getPwd() == null || user.getRnd() == null) {
                return false;
            }

            userId = user.getId();

            encrypts.loadRandomKey(user.getRnd());
            encrypts.genSeedForShortPwd(pwd.substring(pwd.length() - 4));
            if (!confirmPwd(context,pwd)) {
                return false;
            }
        }

        isLogined = r;
        return r;

    }

    public boolean tryCreate(Context context, final String pwd) {

        if (isLogined()) {
            return false;
        }
        if (pwd == null || pwd.isEmpty()) {
            return false;
        }

        final User u = new User();
        u.setCreatedDate(new Date());
        u.setUpdatedDate(new Date());
        u.setName(userName);
        u.setRnd(encrypts.genRandomKey());
        encrypts.loadRandomKey(u.getRnd());

        u.setPwd(encrypts.genSeedForShortPwd(pwd.substring(pwd.length()-4)));


        Long id = DBSession.ins(context).insertOrReplace(u);

        return tryLogin(context, pwd);

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public User getUser(Context ctx) {
        return DBSession.ins(ctx).getUserDao().load(userId);
    }

    public List<Secret> queryRecentSecrets(Context context) {
        if (userId == null) {
            return null;
        }
        return DBSession.ins(context).getSecretDao().queryRaw(" where user_Id =? and level != 2 order by updated_date desc limit 10", userId.toString());
    }

    public List<Secret> querySecretByTitle(Context ctx, String title) {
        return DBSession.ins(ctx).getSecretDao().queryRaw("where user_id=? and title=?", userId.toString(), title);
    }

    public void tryLogout() {
        userId = null;
        setIsLogined(false);
    }

    public boolean isRegistered(Context ctx) {
        final List<User> users = DBSession.ins(ctx).getUserDao().queryRaw("where name=?", userName);

        return users != null && !users.isEmpty();
    }

    public boolean confirmPwd(Context ctx, String userPwd) {
        return encrypts.genSeedForShortPwd(userPwd.substring(userPwd.length()-4)).equals(getUser(ctx).getPwd());
    }
}
