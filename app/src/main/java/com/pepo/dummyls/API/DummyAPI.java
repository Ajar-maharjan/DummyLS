package com.pepo.dummyls.API;

import com.pepo.dummyls.Model.User;
import com.pepo.dummyls.ServerResponse.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface DummyAPI {

    @POST("signup")
    Call<UserResponse> registerUser(@Body User users);

    @FormUrlEncoded
    @POST("login")
    Call<UserResponse> checkUser(@Field("username") String username, @Field("password") String password);

}
