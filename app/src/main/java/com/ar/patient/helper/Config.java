package com.ar.patient.helper;

import android.app.AlertDialog;


public class Config {

//    public static final String SERVER_URL = "http://arpatient.betaplanets.com/wp-json";   //beta
//    public static final String MEDIA_URL = "http://arpatient.betaplanets.com";   //beta
    public static final String SERVER_URL = "https://arpatientsapp.com/wp-json";   //live
    public static final String MEDIA_URL = "https://arpatientsapp.com";   //live

    public static final String TIME_FORMATE = "hh:mm a";
    public static final int TIMEOUT_SOCKET = 60;
    public static final int CODE_CANCEL_SUBSCRIPTION = 2367;
    public static final String SERVER_DATE_FORMAT = "yyyy-MM-dd";
    public static final String SERVER_TIME_FORMAT = "HH:mm:ss";
    public static final String SERVER_FULL_FORMAT = SERVER_DATE_FORMAT + " " + SERVER_TIME_FORMAT;

    public static final String SKU_MONTLY_SILVER = "com.ar.patient.silver";
    public static final String SKU_MONTLY_GOLD = "com.ar.patient.gold";

    public static boolean IS_PATIENT_DASHBOARD_REFERESH = false;
    public static boolean IS_LEADERBOARD_DASHBOARD_REFERESH = false;
    public static boolean IS_MYPROFILE_DASHBOARD_REFERESH = false;
    public static boolean IS_MENU_DASHBOARD_REFERESH = false;

    public static final String PLAN_TYPE_FREE = "0";
    public static final String PLAN_TYPE_SILVER = "1";
    public static final String PLAN_TYPE_GOLD = "2";

    public static final String TERMS_AND_CONDITION = "125116";
    public static final String ABOUT_US = "125117";
    public static final String PRIVCY_POLICY = "125118";

    public static final String maleConfig = "en-gb-x-gbb-network";
    public static final String femaleConfig = "en-us-x-iob-network";


    public static final String PREF_LOGIN_TYPE = "PREF_LOGIN_TYPE";
    public static final String PREF_PUSH_ID = "PREF_PUSH_ID";
    public static final String PREF_FIREBASE_TOKEN = "PREF_FIREBASE_TOKEN";
    public static final String PREF_SESSION = "PREF_SESSION";
    public static final String PREF_USER_TIMEZONE = "PREF_USER_TIMEZONE";
    public static final String PREF_USERID = "PREF_USERID";
    public static final String PREF_EMAIL = "PREF_EMAIL";
    public static final String PREF_NAME = "PREF_NAME";
    public static final String PREF_MENTION_NAME = "mantion_name";
    public static final String PREF_MENTOR = "PREF_MENTOR";
    public static final String PREF_PHONE = "PREF_PHONE";
    public static final String PREF_AVATAR = "PREF_AVATAR";
    public static final String PREF_TUTORAL = "PREF_TUTORAL";
    public static final String PREF_SUBSCRIB_ID = "PREF_SUBSCRIB_ID";
    public static final String PREF_COURSE_ID = "PREF_COURSE_ID";
    public static final String PREF_COURSE_NAME = "PREF_COURSE_NAME";
    public static final String PREF_MENTOR_LIVE = "PREF_MENTOR_LIVE";
    public static final String PREF_join_date = "join_date";
    public static final String PREF_sub_start_date = "sub_start_date";
    public static final String PREF_sub_end_date = "sub_end_date";
    public static final String PREF_followers_counts = "followers_counts";
    public static final String PREF_following_counts = "following_counts";
    public static final String PREF_sub_channel = "sub_channel";
    public static final String PREF_live_schedule = "live_schedule";
    public static final String PREF_live_broadcast_date = "live_broadcast_date";
    public static final String PREF_live_broadcast_title = "live_broadcast_title";
    public static final String PREF_notification_pause = "notification_pause";


    public static final String PREF_KEY_IS_SUBSCRIPTION = "PREF_KEY_IS_SUBSCRIPTION";
    public static final String PREF_KEY_SUBSCRIPTION_EX_DATE = "PREF_KEY_SUBSCRIPTION_EX_DATE";
    public static final String PREF_KEY_SUBSCRIPTION_TOKEN = "PREF_KEY_SUBSCRIPTION_TOKEN";
    public static final String PREF_KEY_SUBSCRIPTION_BUNDLE = "PREF_KEY_SUBSCRIPTION_BUNDLE";
    public static final String PREF_KEY_SUBSCRIPTION_PLATFORM = "PREF_KEY_SUBSCRIPTION_PLATFORM";

    public static final String ARGEMAIL = "ARGEMAIL";
    public static final int IMAGE_WIDTH = 345;
    public static final int IMAGE_HEIGHT = 345;

    public static final String ARG_CHAT_ROOMS = "chat";
    public static final String ACTION_CHAT = "CHAT";
    // screen code
    public static final int CODE_REQUEST_PERMISSION_SETTING = 1010;
    public static final int CODE_REQUEST_PERMISSION_CAMERA = 1011;
    public static final int CODE_REQUEST_PERMISSION_STORAGE = 1012;
    public static final int CODE_REQUEST_PERMISSION_CALENDER = 1013;
    public static final int CODE_REQUEST_PERMISSION_LOCATION = 1014;
    public static final int CODE_REQUEST_PERMISSION_RECORD_AUDIO = 1015;
    public static final int CODE_MENU_NAVIGATION = 2001;
    public static final int CODE_INSTALL_APK = 2002;
    public static final String ROLE_CUSTOMER = "user";
    public static final String PREF_ROLE = "PREF_ROLE";
    public static final String PREF_IS_PUBLIC = "PREF_IS_PUBLIC";

    public static final String PREF_RECORD_STATUS = "PREF_RECORD_STATUS";
    public static final String PREF_WAKE_UP_COMMAND = "PREF_WAKE_UP_COMMAND";
    public static final String PREF_COMMAND_LIST = "PREF_COMMAND_LIST";
    public static final boolean isMute = false;
    public static final String PREF_IS_SHOW_AUTO_INTENT = "PREF_IS_SHOW_AUTO_INTENT";
    public static final String PREF_SETTING_ID = "PREF_SETTING_ID";
    public static final String PREF_SILENT_CALL = "PREF_SILENT_CALL";
    public static final String PREF_TEST_ID = "PREF_TEST_ID";
    public static final String PREF_IS_HINT_SHOW = "PREF_IS_HINT_SHOW";

    public static final String MAINFOLDERNAME = "ARPatient";
    public static final String FEATURE_LIST = "FEATURE_LIST";

    static final String API_KEY = "";
    static final String ERROR_500 = "Internal Server Error";
    static final String ERROR_404 = "Page Not Found";
    static final String ERROR_405 = "Method Not Allowed";
    static final String ERROR_403 = "Authentication failed";
    static final String ERROR_401 = "Unauthorized Error";
    static final String ERROR_MESSAGE = "Error";
    static final String PREF_UDID = "PREF_UDID";
    private static final String DATE_SERVER_FORMATE = "yyyy-MM-dd";
    private static final String TIME_SERVER_FORMATE = "HH:mm:ss";
    static final String FULL_SERVER_DATE_FORMATE = DATE_SERVER_FORMATE + " " + TIME_SERVER_FORMATE;
    public static boolean isFirstToolTip = false;
    public static String SELECTED_ROLL;
    static AlertDialog locationDialog = null;
    private static String TAG = "mytag";
    static final String PREF_FILE = TAG + "_PREF";


    public static void crash(Throwable t) {
//        Crashlytics.log(t.getLocalizedMessage());
//        Crashlytics.logException(t);
    }

    public static void freeMemory() {
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }
}