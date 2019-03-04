package com.appzone.shelcom.services;

import com.appzone.shelcom.models.CompanyDataModel;
import com.appzone.shelcom.models.RuleModel;
import com.appzone.shelcom.models.NotificationCount;
import com.appzone.shelcom.models.NotificationDataModel;
import com.appzone.shelcom.models.OrderDataModel;
import com.appzone.shelcom.models.SliderDataModel;
import com.appzone.shelcom.models.UserModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Service {
    @FormUrlEncoded
    @POST("/Api/login")
    Call<UserModel> SignIn(@Field("user_phone") String phone
    );

    @Multipart
    @POST("api/sign-up")
    Call<UserModel> SignUp(@Part("user_name") RequestBody name,
                           @Part("user_phone") RequestBody phone,
                           @Part("user_email") RequestBody email,
                           @Part MultipartBody.Part avatar

    );

    @GET("/Api/appRule")
    Call<RuleModel> getAppRule(@Query("type") String type);

    @FormUrlEncoded
    @POST("/Api/updateToken")
    Call<ResponseBody> updateToken(@Field("user_id") String user_id,
                                   @Field("user_token_id") String fire_base_token);

    @Multipart
    @POST("/Api/profile")
    Call<UserModel> updateImage(@Part("user_id") RequestBody user_id,
                                @Part("user_name") RequestBody user_name,
                                @Part("user_email") RequestBody user_email,
                                @Part MultipartBody.Part avatar
    );

    @FormUrlEncoded
    @POST("/Api/profile")
    Call<UserModel> updateData(@Field("user_id") String user_id,
                               @Field("user_name") String user_name,
                               @Field("user_email") String user_email
    );

    @GET("/Api/logout")
    Call<ResponseBody> logout(@Query("user_id") String user_id);

    @GET("/Api/myOrders")
    Call<OrderDataModel> getMyOrders(@Query("page") int page_index,
                                     @Query("user_id") String user_id,
                                     @Query("order_type") String order_type
    );
    @GET("/Api/alerts")
    Call<NotificationDataModel> getNotification(@Query("user_id") String user_id,
                                                @Query("page") int page
    );

    @GET("/Api/getAlerts")
    Call<NotificationCount> getNotificationCount(@Query("user_id") String user_id,
                                                 @Query("type") String type
                                                 );

    @FormUrlEncoded
    @POST("/api/contacts")
    Call<ResponseBody> sendContacts(@Field("name") String name,
                                    @Field("email") String email,
                                    @Field("message") String message
    );

    @GET("/Api/slider")
    Call<SliderDataModel> getAds();


    @GET("/Api/company")
    Call<CompanyDataModel> getCompanies(@Query("type") int type, @Query("page") int page);

}
