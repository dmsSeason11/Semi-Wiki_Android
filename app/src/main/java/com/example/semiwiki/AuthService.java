package com.example.semiwiki;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

// 로그인 관련 API들을 정의해두는 인터페이스
public interface AuthService {

    // POST 요청을 /auth/sign-in 경로로 보냄
    @POST("/auth/sign-in")
    Call<LoginResponse> signIn(@Body LoginRequest request);
    // @Body는 요청 본문에 LoginRequest 객체를 JSON으로 변환해서 보냄
    // 반환값은 LoginResponse를 담은 Call 객체
}
