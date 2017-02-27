package com.headrun.evidyaloka.config;

/**
 * Created by HP-HP on 27-03-2016.
 */

public class ApiEndpoints {
    public static final String BASE_URL = "http://www.evidyaloka.org/";
    public static final String BASE_URL1 = "http://dev.evidyaloka.org:9090/";
    public static final String BASE_URL2 = "http://dev.evidyaloka.org/";

    public static final String GET_DEMANDS = BASE_URL + "get_demand/";
    public static final String GET_FILTERS = BASE_URL + "get_filters_api/";
    public static final String GET_DEMAND_DEATILS = BASE_URL + "demand_details_api/";
    public static final String BLOCAK_DEMANDS = BASE_URL + "block_demand/";
    public static final String RELEASE_DEMANDS = BASE_URL + "release_demand/";
    public static final String CSRFTOKEN = BASE_URL + "rest_authentication/csrf";
    public static final String UPDATE_FCMTOKEN = BASE_URL + "api/fcm_key/";
    public static final String REST_AUTH = BASE_URL + "rest_authentication/login/";
    public static final String SIGNUP = BASE_URL + "rest_authentication/signup/";
    public static final String SIGNOUT = BASE_URL + "rest_authentication/logout/";
    public static final String UPCOMING_SESSIONS1 = BASE_URL + "api/get_upcom_sess/";
    public static final String UPCOMING_SESSIONS = BASE_URL + "api/get_user_sessions/";
    public static final String CHANGE_SESSIONS = BASE_URL + "api/save_session_status/";
}
