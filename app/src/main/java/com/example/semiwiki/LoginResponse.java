package com.example.semiwiki;

// 서버에서 로그인 성공 시 응답으로 주는 데이터를 담는 클래스
public class LoginResponse {

    // 서버에서 내려주는 access_token
    private String access_token;

    // getter: Retrofit이 이 메서드를 이용해서 응답 JSON을 매핑함
    public String getAccess_token() {
        return access_token;
    }
}
