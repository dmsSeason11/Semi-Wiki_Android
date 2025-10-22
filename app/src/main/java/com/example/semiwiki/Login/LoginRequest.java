package com.example.semiwiki.Login;

import com.google.gson.annotations.SerializedName;

// 서버에 로그인 요청으로 보낼 데이터를 담는 클래스
public class LoginRequest {

    // 서버 명세에 맞춰 JSON 키 매핑
    @SerializedName("accountId")
    private String accountId; // 사용자 아이디

    @SerializedName("password")
    private String password;  // 사용자 비밀번호

    // 생성자: new LoginRequest("아이디", "비번") 형태로 사용
    public LoginRequest(String accountId, String password) {
        this.accountId = accountId;
        this.password = password;
    }

    // Getter (Gson 변환 및 코드 활용용)
    public String getAccountId() { return accountId; }
    public String getPassword() { return password; }
}
