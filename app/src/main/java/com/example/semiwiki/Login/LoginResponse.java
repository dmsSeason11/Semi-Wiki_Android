package com.example.semiwiki.Login;

import com.google.gson.annotations.SerializedName;

// 서버에서 로그인 성공 시 응답으로 내려주는 JSON을 담는 클래스
public class LoginResponse {

    // 응답 JSON: { "access_token": "...", "refresh_token": "..." }
    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("refresh_token")
    private String refreshToken;

    @SerializedName(value = "accountId", alternate = {"account_id", "userId"})
    private String accountId;

    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public String getAccountId()    { return accountId; }
}
