package com.example.workshop.ApiService;

import com.example.workshop.Models.Category;
import com.example.workshop.Models.ResponseMessage;
import com.example.workshop.Models.Service;
import com.example.workshop.Models.ServiceDetail;
import com.example.workshop.Models.ServiceOrder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitService
{
    Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    //URL do testowania na localhoscie
    //String BASE_URL = "http://10.0.2.2:5000/";
    String BASE_URL = "http://workshop-env.ru2u9rf8jq.us-east-2.elasticbeanstalk.com:5000/";

         Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(BASE_URL)
                    .build();
//kategorie
    @GET("category")
    Observable <List<Category>> getCategories();
//użytkownik
    @GET("user/login")
    Observable <ResponseMessage> loginUser(@Header("Authorization") String token);
    @GET("logout")
    Observable <ResponseMessage> logoutUser();
    @FormUrlEncoded
    @POST("register")
    Observable <ResponseMessage> registerUser(@Field("name") String name, @Field("password") String password);

//zlecenie usługi
    @FormUrlEncoded
    @POST("user/service/order")
    Observable <ServiceOrder> addServiceOrderToUser(@Header("Authorization") String token, @Query("service") Long service, @FieldMap Map<String, String> serviceOrder);
    @PUT("user/service/order")
    Observable<ResponseMessage> updateServiceOrder(@Header("Authorization") String token, @Query("serviceOrderId") Long serviceOrderId,@Body ServiceOrder serviceOrder);
    @DELETE("user/service/order/{serviceOrderId}")
    Observable<ResponseMessage> deleteServiceOrder(@Header("Authorization") String token, @Path("serviceOrderId") Long serviceOrderId);
    @GET("provider/order")
    Observable<List<ServiceOrder>> getServiceOrderByServiceDateAndProvidername(@Header("Authorization") String token, @Query("year") int year, @Query("month") int month, @Query("day") Integer day);
    @GET("user/order")
    Observable<List<ServiceOrder>> getServiceOrderByServiceUsernameAndDate(@Header("Authorization") String token, @Query("year") int year, @Query("month") int month, @Query("day") Integer day);
    @GET("order")
    Observable<List<ServiceOrder>> getServiceOrderByServiceDate(@Query("service") Long service, @Query("year") int year, @Query("month") int month, @Query("day") int day);
    @GET("id")
    Observable<Long> getServiceByUsername(@Header("Authorization") String token);
    @GET("provider/order/detail/{serviceDetail}")
    Observable<List<ServiceOrder>> getServiceOrderByServiceDetailId(@Header("Authorization") String token, @Path("serviceDetail") Long serviceDetail);
//usługa
    @PUT("/user/order")
    Observable<ServiceOrder> changeOrderStatus(@Header("Authorization") String token, @Query("serviceOrder") Long serviceOrder, @Query("status") String status);
    @FormUrlEncoded
    @POST("user/service")
    Observable<ResponseMessage> addService(@Header("Authorization") String token, @Query("category") Long category, @FieldMap Map<String, String> service);
    @DELETE("provider/service")
    Observable<ResponseMessage> deleteService(@Header("Authorization") String token);
    @DELETE("provider/service/details")
    Observable<ResponseMessage> deleteServiceDetail(@Header("Authorization") String token, @Query("serviceDetail") Long serviceDetail);
    @GET("/search")
    Observable<List<Service>> searchService(@Query("category") Long categoryId, @Query("city") String city, @Query("parentCategory") Long parentCategoryId, @Query("last") int last);
    @GET("/order/{serviceDetail}")
    Observable<List<ServiceOrder>> getServiceOrders(@Path("serviceDetail") Long serviceDetailId);
//szczegóły usługi
    @FormUrlEncoded
    @POST("provider/service/details")
    Observable<ResponseMessage> addDetailsToService(@Header("Authorization") String token, @Query("category") Long category,@FieldMap Map<String, String> serviceDetail);
    @GET("/{service}")
    Observable<List<ServiceDetail>> getServiceDetail(@Path("service") Long serviceId);
}
