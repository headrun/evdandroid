package com.headrun.evidyaloka.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.headrun.evidyaloka.model.LoginResponse;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class UserSession {

    public String TAG = UserSession.this.getClass().getSimpleName();
    public SharedPreferences pref;
    public Context _context;
    public SharedPreferences.Editor editor;
    int PRIVATE_MODE = 0;
    private static final String PREFER_NAME = "EVD";

    public static final String userlogin_name = "loginname";
    public static final String ALL_LANG = "lang";
    public static final String SEL_LANG_FILER = "sel_lang";
    public static final String ALL_STATE = "states";
    public static final String SEL_STATE_FILTER = "sel_states";
    public static final String SEL_COOKIE = "sel_cookie";
    public static final String OLD_FCM_TOKEN = "old_fcm_token";
    public static final String New_FCM_TOKEN = "new_fcm_token";
    public static final String FB_ID = "fb_id";
    public static final String GOOGLE_ID = "google_id";
    public static final String LOGIN_FIRST = "login_first";
    public static final String SEL_LOGIN_TYPE = "login_type";
    public static final String USER_DATA = "user_data";
    public static final String CSRF = "csrf";
    public static final String CHANGE_SESSION_STATU = "change_session_status";
    public static final String SESSION_TITLES = "session_title";
    public static final String IS_LOIGN = "is_login";

    public UserSession(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.commit();
    }

    public UserSession() {
    }


    public void setUserData(LoginResponse user_data) {
        editor.putString(USER_DATA, new Gson().toJson(user_data));
        setIsLoign(true);
        editor.commit();

    }

    public LoginResponse getUserData() {
        String data = pref.getString(USER_DATA, "");
        if (data.isEmpty())
            return null;
        else
            return new Gson().fromJson(data, LoginResponse.class);
    }

    public void setSessionTitles(String session_titles) {
        editor.putString(SESSION_TITLES, session_titles);
        editor.commit();
    }

    public List<String> getSessionTitles() {
        String data = pref.getString(SESSION_TITLES, "");
        if (data.isEmpty())
            return Collections.emptyList();
        else
            return Arrays.asList(data.substring(1, data.length() - 1).replaceAll("\\[|\\]|\\s", "").split(","));
    }


    public void setIsLoign(boolean is_login) {
        editor.putBoolean(IS_LOIGN, is_login);
        editor.commit();
    }

    public boolean getIsLogin() {
        return pref.getBoolean(IS_LOIGN, false);
    }

    public void setSelLoginType(String login_type) {
        editor.putString(SEL_LOGIN_TYPE, login_type);
        editor.commit();
    }

    public String getSelLoginType() {
        return pref.getString(SEL_LOGIN_TYPE, "");
    }

    public void setChangeSessionStatu(String session_id, String status) {

        HashMap<String, String> status_data1 = getChangeSessionStatus();
        status_data1.put(session_id, status);
        editor.putString(CHANGE_SESSION_STATU, status_data1.toString());
        editor.commit();
    }

    public void setChangeSessionStatu(HashMap<String, String> statusChange) {
        editor.putString(CHANGE_SESSION_STATU, statusChange.toString());
        editor.commit();
    }

    public HashMap<String, String> getChangeSessionStatus() {

        String status_data = pref.getString(CHANGE_SESSION_STATU, "");
        return new Utils().HashMapToString(status_data);
    }

    public void removeChangeSessionStatus(String session_id) {

        HashMap<String, String> status_data1 = getChangeSessionStatus();

        if (status_data1.containsKey(session_id)) {
            status_data1.remove(session_id);
        }
        editor.putString(CHANGE_SESSION_STATU, status_data1.toString());
        editor.commit();
    }

    public void SetCsrf(String csrf) {
        editor.putString(CSRF, csrf);
        editor.commit();
    }

    public String getCsrf() {
        return pref.getString(CSRF, "");
    }


    public void setFb_Id(String fb_id) {
        editor.putString(FB_ID, fb_id);
        editor.commit();

    }

    public String getFb_id() {
        return pref.getString(FB_ID, "");
    }

    public void setLogin_first(String login_first) {
        editor.putString(LOGIN_FIRST, login_first);
        editor.commit();

    }

    public String getLoginFirst() {
        return pref.getString(LOGIN_FIRST, "0");
    }

    public void setGoogle_Id(String google_id) {
        editor.putString(GOOGLE_ID, google_id);
        editor.commit();

    }

    public String getGoogle_id() {
        return pref.getString(GOOGLE_ID, "");
    }

    public void setCookie(String sel_cookie) {
        editor.putString(SEL_COOKIE, sel_cookie);
        editor.commit();
    }

    public String getCookie() {
        return pref.getString(SEL_COOKIE, "");
    }

    public void setLangFilter(String lang_filter) {
        editor.putString(ALL_LANG, lang_filter);
        editor.commit();
    }

    public String getLangFilter() {
        return pref.getString(ALL_LANG, "");
    }

    public void setSelLangFilter(String sel_lang) {
        editor.putString(SEL_LANG_FILER, sel_lang);
        editor.commit();
    }

    public String getSelLangFilter() {
        return pref.getString(SEL_LANG_FILER, "[]");
    }

    public void setStateFilter(String state_filter) {
        editor.putString(ALL_STATE, state_filter);
        editor.commit();
    }

    public String getStateFilter() {
        return pref.getString(ALL_STATE, "");
    }

    public void setSelStateFilter(String sel_state) {
        editor.putString(SEL_STATE_FILTER, sel_state);
        editor.commit();
    }

    public String getSelStateFilter() {
        return pref.getString(SEL_STATE_FILTER, "[]");
    }

    public void setOLDFCMToken(String sel_state) {
        editor.putString(OLD_FCM_TOKEN, sel_state);
        editor.commit();
    }

    public String getOLDFCMToken() {
        return pref.getString(OLD_FCM_TOKEN, "");
    }

    public void setNEWFCMToken(String sel_state) {
        editor.putString(New_FCM_TOKEN, sel_state);
        editor.commit();
    }

    public String getNEWFCMToken() {
        return pref.getString(New_FCM_TOKEN, "");
    }


    public void clearCookie() {
        editor.remove(SEL_COOKIE);
        editor.remove(LOGIN_FIRST);
        editor.remove(SEL_LOGIN_TYPE);
        editor.remove(USER_DATA);
        editor.remove(IS_LOIGN);
        editor.remove(CHANGE_SESSION_STATU);
        editor.commit();
    }
}