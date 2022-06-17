package com.ar.patient.retrofit;


import com.ar.patient.responsemodel.BaseBean;
import com.ar.patient.responsemodel.ChangeAvatar;
import com.ar.patient.responsemodel.ChangeProfileResponse;
import com.ar.patient.responsemodel.CmsPageResponse;
import com.ar.patient.responsemodel.DiagnosisResponse;
import com.ar.patient.responsemodel.FaqsResponse;
import com.ar.patient.responsemodel.ForgotpasswordResponse;
import com.ar.patient.responsemodel.InAppCreateResponse;
import com.ar.patient.responsemodel.LoginResponse;
import com.ar.patient.responsemodel.NewsFeedResponse;
import com.ar.patient.responsemodel.leaderboardresponse.Leaderboardresponse;
import com.ar.patient.responsemodel.myprofile.MyProfileResponse;
import com.ar.patient.responsemodel.patientsresponse.PatientsListResponse;
import com.ar.patient.responsemodel.signupResoponse;
import com.ar.patient.responsemodel.submitexam.SubmitExamResponse;
import com.ar.patient.responsemodel.testresultresponse.TestResultResponse;
import com.ar.patient.responsemodel.voiceexam.VoiceExamQuestionResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ServiceInterface {


    @POST(RestClient.API_LOGIN)
    Call<LoginResponse> Login(@Body RequestBody body);

    @POST(RestClient.API_PATIENTS_LIST)
    Call<PatientsListResponse> getpatientslist(@Body RequestBody body);

    @POST(RestClient.API_VOICE_EXAM_API)
    Call<VoiceExamQuestionResponse> getvoiceExam(@Body RequestBody body);

    @POST(RestClient.API_MY_PROFILE)
    Call<MyProfileResponse> getmyprofile(@Body RequestBody body);

    @POST(RestClient.API_TESTS_API)
    Call<Leaderboardresponse> getLeaderboardlist(@Body RequestBody body);

    @POST(RestClient.API_FORGOT_PASSWORD)
    Call<ForgotpasswordResponse> forgotPassword(@Body RequestBody body);

    @POST(RestClient.API_CHNAGE_PROFILE)
    Call<ChangeProfileResponse> chnageprofile(@Body RequestBody body);

    @POST(RestClient.API_CONTACT_US)
    Call<BaseBean> contactus(@Body RequestBody body);

    @POST(RestClient.API_DIAGNOSIS)
    Call<DiagnosisResponse> diagnosis(@Body RequestBody body);

    @POST(RestClient.API_EXAM_SUBMIT)
    Call<SubmitExamResponse> submitExam(@Body RequestBody body);

    @POST(RestClient.API_TESTS_DETAILS_API)
    Call<TestResultResponse> testdetails(@Body RequestBody body);

    @POST(RestClient.API_TERMS)
    Call<CmsPageResponse> terms(@Body RequestBody body);

    @POST(RestClient.API_LOGOUT)
    Call<BaseBean> logout(@Body RequestBody body);

    @POST(RestClient.API_ACCOUNT_DELETE)
    Call<BaseBean> deleteAccount(@Body RequestBody body);

    @POST(RestClient.API_ChANGE_PASSWORD)
    Call<BaseBean> changePassword(@Body RequestBody body);

    @POST(RestClient.API_NEWS_FEED_LIST)
    Call<NewsFeedResponse> newsfeed();

    @POST(RestClient.API_FAQ_LIST)
    Call<FaqsResponse> faqlist();

    @POST(RestClient.API_RESET_PASSWORD)
    Call<BaseBean> resetPassword(@Body RequestBody body);

    @Multipart
    @POST(RestClient.API_SignUp)
    Call<signupResoponse> Signup(
            @Part("is_public") RequestBody is_public,
            @Part("name") RequestBody name,
            @Part("email") RequestBody email,
            @Part("password") RequestBody password,
            @Part MultipartBody.Part avatar);

    @Multipart
    @POST(RestClient.API_CHNAGE_AVATAR)
    Call<ChangeAvatar> changeavatar(
            @Part("user_id") RequestBody user_id,
            @Part MultipartBody.Part avatar);

    @FormUrlEncoded
    @POST(RestClient.INAPP_CREATE)
    Call<InAppCreateResponse> inappCreate(@Field("enddate") String enddate,
                                          @Field("inapp_purchase_id") String inappPurchaseId,
                                          @Field("startdate") String startdate,
                                          @Field("order_id") String orderId,
                                          @Field("transaction_id") String transaction_id,
                                          @Field("islive") String islive,
                                          @Field("type") String type);

}

