package com.example.semiwiki.Login;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Path;

// 로그인 관련 API들을 정의해둔 인터페이스
public interface AuthService {

    @POST("auth/signin")
    Call<LoginResponse> signIn(@Body LoginRequest request);

    @GET("logout/{accountId}")
    Call<Void> logout(@Path("accountId") String accountId);
}
