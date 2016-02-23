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
    public final static String FIELD_ACCESS_TOKEN = "access_token";

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

    @SyncanoField(name = FIELD_ACCESS_TOKEN, required = true)
    private String authToken;

    private SocialAuthBackend socialAuthBackend;

    public AbstractUser() {
    }

    public AbstractUser(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public AbstractUser(SocialAuthBackend socialAuthBackend, String authToken) {
        this.socialAuthBackend = socialAuthBackend;
        this.authToken = authToken;
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

    public Response<P> fetchProfile() {
        return getProfile().fetch();
    }

    public void fetchProfile(SyncanoCallback<P> syncanoCallback) {
        getProfile().fetch(syncanoCallback);
    }

    public <T extends AbstractUser> Response<T> fetch() {
        return getSyncano().fetchCurrentUser((T) this).send();
    }

    public <T extends AbstractUser> void fetch(SyncanoCallback<T> syncanoCallback) {
        getSyncano().fetchCurrentUser((T) this).sendAsync(syncanoCallback);
    }

    public <T extends AbstractUser> Response<T> register() {
        return getSyncano().registerUser((T) this).send();
    }

    public <T extends AbstractUser> void register(SyncanoCallback<T> callback) {
        getSyncano().registerUser((T) this).sendAsync(callback);
    }

    public <T extends AbstractUser> Response<T> login() {
        return getSyncano().loginUser((T) this).send();
    }

    public <T extends AbstractUser> Response<T> loginSocialUser() {
        return getSyncano().loginSocialUser((T) this).send();
    }

    public <T extends AbstractUser> void loginSocialUser(SyncanoCallback<T> callback) {
        getSyncano().loginSocialUser((T) this).sendAsync(callback);
    }

    public <T extends AbstractUser> void login(SyncanoCallback<T> callback) {
        getSyncano().loginUser((T) this).sendAsync(callback);
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public SocialAuthBackend getSocialAuthBackend() {
        return socialAuthBackend;
    }

    public void setSocialAuthBackend(SocialAuthBackend socialAuthBackend) {
        this.socialAuthBackend = socialAuthBackend;
    }
}
