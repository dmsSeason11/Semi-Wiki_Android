package com.example.semiwiki.Login;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    @SerializedName("accountId")
    private String accountId; // 사용자 아이디

    @SerializedName("password")
    private String password;  // 사용자 비밀번호

    public LoginRequest(String accountId, String password) {
        this.accountId = accountId;
        this.password = password;
    }

    public String getAccountId() { return accountId; }
    public String getPassword() { return password; }
}
