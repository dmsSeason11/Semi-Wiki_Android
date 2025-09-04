package com.example.semiwiki;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

// 로그인 관련 API들을 정의해둔 인터페이스
public interface AuthService {

    @POST("auth/sign-in")
    Call<LoginResponse> signIn(@Body LoginRequest request);
}
