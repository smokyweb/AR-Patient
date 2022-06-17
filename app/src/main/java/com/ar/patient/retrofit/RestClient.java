package com.ar.patient.retrofit;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ar.patient.helper.Config;
import com.ar.patient.helper.Pref;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RestClient {

    public static final String INIT_API = "init/";
    public static final String USERS_API = "users/";
    public static final String PATIENTS_API = "patients/";
    public static final String TESTS_API = "tests/";
    public static final String NEWSFEED_API = "newsfeeds/";
    public static final String FAQS_API = "faqs/";
    public static final String INAPP_PURCHASE_API = "inapp_purchase/";


    // Init API
    public static final String API_TERMS = INIT_API + "cms";

    // users API
    public static final String API_LOGIN = USERS_API + "login";
    public static final String API_SignUp = USERS_API + "signup";
    public static final String API_FORGOT_PASSWORD = USERS_API + "forgot_password";
    public static final String API_RESET_PASSWORD = USERS_API + "reset_password";
    public static final String API_MY_PROFILE = USERS_API + "profile";
    public static final String API_CHNAGE_PROFILE = USERS_API + "change_profile";
    public static final String API_CHNAGE_AVATAR = USERS_API + "change_avatar";
    public static final String API_CONTACT_US = USERS_API + "contact_us";
    public static final String API_LOGOUT = USERS_API + "logout";
    public static final String API_ACCOUNT_DELETE = USERS_API + "delete";
    public static final String API_ChANGE_PASSWORD = USERS_API + "change_password";

    //patients
    public static final String API_PATIENTS_LIST = PATIENTS_API + "types";

    //Test Api
    public static final String API_TESTS_API = TESTS_API + "leaderboard";
    public static final String API_TESTS_DETAILS_API = TESTS_API + "result";
    public static final String API_VOICE_EXAM_API = TESTS_API + "ve_definition";
    public static final String API_DIAGNOSIS = TESTS_API + "diagnosis";
    public static final String API_EXAM_SUBMIT = TESTS_API + "ve_submit";


    //News Feed APi
    public static final String API_NEWS_FEED_LIST = NEWSFEED_API + "list";

    //faqs api
    public static final String API_FAQ_LIST = FAQS_API + "list";

    //In-app purchase
    public static final String INAPP_CREATE = INAPP_PURCHASE_API + "create";


    private static final String BASE_URL = Config.SERVER_URL + "/v17/";
    public Context context;
    private ServiceInterface apiService;


    public RestClient(final Context context) {
        this.context = context;
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(Config.TIMEOUT_SOCKET, TimeUnit.SECONDS)
                .readTimeout(Config.TIMEOUT_SOCKET, TimeUnit.SECONDS)
                .addInterceptor(new LoggingInterceptor()).build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL).client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ServiceInterface.class);
    }


    public ServiceInterface getApiService() {
        return apiService;
    }

    class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {

            Request original = chain.request();
            Log.e("request", original.toString());
            HttpUrl originalHttpUrl = original.url();


            HttpUrl url = originalHttpUrl.newBuilder()
                    .addQueryParameter("user_lang", "en")
                    .addQueryParameter("role",
                            Pref.getValue(context, Config.PREF_ROLE, "").equalsIgnoreCase("") ? "N/A" : Pref.getValue(context, Config.PREF_ROLE, ""))
                    .addQueryParameter("version", "1.0")
                    .addQueryParameter("platform", "ANDROID").build();

            Log.d("mytag", "User Id : ===> " + String.valueOf(Pref.getValue(context, Config.PREF_USERID, "")));
            Log.d("mytag", "session Id : ===> " + String.valueOf(Pref.getValue(context, Config.PREF_SESSION, "")));

            Request.Builder requestBuilder = original.newBuilder().url(url)
                    .addHeader("user-id", String.valueOf(Pref.getValue(context, Config.PREF_USERID, "")))
                    .addHeader("session-id", String.valueOf(Pref.getValue(context, Config.PREF_SESSION, "")));


            //Request.Builder requestBuilder = original.newBuilder().url(url);
            Request request = requestBuilder.build();
            long t1 = System.nanoTime();
//            Log.d("mytag", String.format("Requested Api Url ::", String.format("Sending request %s on %s%n%s\nBody : %s", request.url(), chain.connection(), request.headers(), body));
//            Log.d("mytag", String.format("Requested Api Url ::", String.format(String.valueOf(request.url()))));
            Response response = chain.proceed(request);
            long t2 = System.nanoTime();
            String responseString = new String(response.body().bytes());
            Log.d("mytag", "Response : " + responseString);
            if (response.code() == 200) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    if (jsonObject.getString("code").equalsIgnoreCase("1001") ||
                            jsonObject.getString("code").equalsIgnoreCase("1002") ||
                            jsonObject.getString("code").equalsIgnoreCase("1004")) {
                        if (ErrorActivity.activity == null)
                            context.startActivity(new Intent(context, ErrorActivity.class).putExtra(ErrorActivity.ARG_MESSAGE, jsonObject.getString("message")).putExtra(ErrorActivity.ARG_IS_LOGIN, true).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                } catch (Exception e) {
                    if (url.toString().contains("users/login?")) {
                        responseString = "{\n" +
                                "  \"code\": 1,\n" +
                                "  \"message\": \"Email or password wrong.\"\n" +
                                "}";
                    } else {
                        responseString = "{\n" +
                                "  \"code\": 1,\n" +
                                "  \"message\": \"server response wrong.\"\n" +
                                "}";
                    }

                }
            }
            return response.newBuilder().body(ResponseBody.create(responseString, response.body().contentType())).build();
            //return response.newBuilder().body(ResponseBody.create(response.body().contentType(), responseString)).build();
        }

    }
}
