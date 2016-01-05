package com.syncano.library.data;

import com.syncano.library.Syncano;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.Response;
import com.syncano.library.callbacks.SyncanoCallback;
import com.syncano.library.choice.SocialAuthBackend;

/**
 * Override this class with ProfileType to create your custom User.
 *
 * @param <P> Type of user profile.
 */
public abstract class AbstractUser<P extends Profile> {

    public static final String FIELD_ID = "id";
    public static final String FIELD_USER_NAME = "username";
    public static final String FIELD_PASSWORD = "password";
    public static final String FIELD_USER_KEY = "user_key";
    public static final String FIELD_PROFILE = "profile";

    private Syncano syncano;

    @SyncanoField(name = FIELD_ID, readOnly = true)
    private Integer id;

    @SyncanoField(name = FIELD_USER_NAME, required = true)
    private String userName;

    @SyncanoField(name = FIELD_PASSWORD, required = true)
    private String password;

    @SyncanoField(name = FIELD_USER_KEY, readOnly = true)
    private String userKey;

    @SyncanoField(name = FIELD_PROFILE, readOnly = true)
    private P profile;

    public AbstractUser() {
    }

    public AbstractUser(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public P getProfile() {
        return profile;
    }

    public void setProfile(P profile) {
        this.profile = profile;
    }

    private Syncano getSyncano() {
        if (syncano == null) {
            return Syncano.getInstance();
        }
        return syncano;
    }

    public <T extends AbstractUser<P>> T on(Syncano syncano) {
        this.syncano = syncano;
        return (T) this;
    }

    public <T extends AbstractUser> Response<T> register() {
        return getSyncano().registerCustomUser((T) this).send();
    }

    public <T extends AbstractUser> void register(SyncanoCallback<T> callback) {
        getSyncano().registerCustomUser((T) this).sendAsync(callback);
    }

    public <T extends AbstractUser> Response<T> login() {
        return getSyncano().loginUser((Class<T>) getClass(), getUserName(), getPassword()).send();
    }

    public <T extends AbstractUser> void login(SyncanoCallback<T> callback) {
        getSyncano().loginUser((Class<T>) getClass(), getUserName(), getPassword()).sendAsync(callback);
    }
}
