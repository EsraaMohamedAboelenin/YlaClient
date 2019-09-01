package solversteam.familycab.Util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by kholoud on 4/18/2017.
 */
public class PrefsManger {

    private static final String FILE_NAME = "WethaqPrefsManger";

    private Context mContext;
    private SharedPreferences mSharedPreferences;

    private static final String KEY_USER_LANG_CHOOSED = "UserAppLanguage";
    private static final String KEY_LANG = "AppLanguage";
    private static final String KEY_GCM_TOKEN = "GCMToken";
    private static final String KEY_LOGGED = "Logged";
    private static final String KEY_COOKIE = "Cookie";
    private static final String KEY_USER_NAME = "UserName";
    private static final String KEY_USER_PICURL = "UserPicURL";
    private static final String KEY_USER_TYPE = "UserType";
    private static final String KEY_USER_BALANCE = "UserBalance";

    private static final Boolean DEFAULT_USERCHOOSE = false;
    private static final String DEFAULT_LANG = "en";
    private static final String DEFAULT_GCM_TOKEN = "GCMToken";
    private static final Boolean DEFAULT_LOGGED = false;
    private static final String DEFAULT_COOKIE = null;
    private static final String DEFAULT_USER_NAME = "";
    private static final String DEFAULT_USER_PICURL = "";
    private static final int DEFAULT_USER_TYPE = 0;
    private static final String DEFAULT_USER_BALANCE = "";

    public PrefsManger(Context context) {
        // Prepare Shared Preferences
        this.mContext = context;
        mSharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    // false >> user didn't choose any lang
    public void setUserChooser(boolean key) {
        mSharedPreferences.edit().putBoolean(KEY_USER_LANG_CHOOSED, key).commit();
    }

    public boolean getUserChooser() {
        boolean key = mSharedPreferences.getBoolean(KEY_USER_LANG_CHOOSED, DEFAULT_USERCHOOSE);
        return key;
    }

    //en_us -> English , ar ->Arabic
    public void setLang(String lang) {
        mSharedPreferences.edit().putString(KEY_LANG, lang).commit();
    }

    public String getLang() {
        String lang = mSharedPreferences.getString(KEY_LANG, DEFAULT_LANG);
        return lang;
    }

    // false >> !logged
    public void setLogin(boolean login) {
        mSharedPreferences.edit().putBoolean(KEY_LOGGED, login).commit();
    }

    public boolean getLogin() {
        boolean login = mSharedPreferences.getBoolean(KEY_LOGGED, DEFAULT_LOGGED);
        return login;
    }

    public void setGCMToken(String token) {
        mSharedPreferences.edit().putString(KEY_GCM_TOKEN, token).commit();
    }

    public String getGCMToken() {
        String token = mSharedPreferences.getString(KEY_GCM_TOKEN, DEFAULT_GCM_TOKEN);
        return token;
    }

    public void setCookie(String cookie) {
        mSharedPreferences.edit().putString(KEY_COOKIE, cookie).commit();
    }

    public String getCookie() {
        String cookie = mSharedPreferences.getString(KEY_COOKIE, DEFAULT_COOKIE);
        return cookie;
    }

    public void setUserName(String name) {
        mSharedPreferences.edit().putString(KEY_USER_NAME, name).commit();
    }

    public String getUserName() {
        String userName = mSharedPreferences.getString(KEY_USER_NAME, DEFAULT_USER_NAME);
        return userName;
    }

    public void setUserPicurl(String url) {
        mSharedPreferences.edit().putString(KEY_USER_PICURL, url).commit();
    }

    public String getUserPicurl() {
        String url = mSharedPreferences.getString(KEY_USER_PICURL, DEFAULT_USER_PICURL);
        return url;
    }

    public void setUserType(int type) {
        mSharedPreferences.edit().putInt(KEY_USER_TYPE, type).commit();
    }

    public int getUserType() {
        int type = mSharedPreferences.getInt(KEY_USER_TYPE, DEFAULT_USER_TYPE);
        return type;
    }

    public void setUserBalance(String balance) {
        mSharedPreferences.edit().putString(KEY_USER_BALANCE, balance).commit();
    }

    public String getUserBalance() {
        String balance = mSharedPreferences.getString(KEY_USER_BALANCE, DEFAULT_USER_BALANCE);
        return balance;
    }

    public void clearUserInfo() {
        mSharedPreferences.edit().putString(KEY_GCM_TOKEN, DEFAULT_GCM_TOKEN).commit();
        mSharedPreferences.edit().putBoolean(KEY_LOGGED, DEFAULT_LOGGED).commit();
        mSharedPreferences.edit().putString(KEY_COOKIE, DEFAULT_COOKIE).commit();
        mSharedPreferences.edit().putString(KEY_USER_NAME, DEFAULT_USER_NAME).commit();
        mSharedPreferences.edit().putString(KEY_USER_PICURL, DEFAULT_USER_PICURL).commit();
        mSharedPreferences.edit().putInt(KEY_USER_TYPE, DEFAULT_USER_TYPE).commit();
        mSharedPreferences.edit().putString(KEY_USER_BALANCE, DEFAULT_USER_BALANCE).commit();
    }
}
