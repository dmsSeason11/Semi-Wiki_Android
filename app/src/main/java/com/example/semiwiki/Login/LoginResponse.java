package com.example.semiwiki.Login;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName(value = "accessToken",  alternate = {"access_token"})
    private String accessToken;

    @SerializedName(value = "refreshToken", alternate = {"refresh_token"})
    private String refreshToken;

    @SerializedName(value = "accountId", alternate = {"account_id", "userId"})
    private String accountId;

    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public String getAccountId()    { return accountId; }
}
