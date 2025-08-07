package com.example.semiwiki;


 // 서버에 로그인 요청으로 보낼 데이터를 담는 클래스
public class LoginRequest {
    private String accountId; // 사용자 ID
    private String password;  // 사용자 비밀번호

    // 생성자: new LoginRequest("아이디", "비번") 이런 식으로 사용됨
    public LoginRequest(String accountId, String password) {
        this.accountId = accountId;
        this.password = password;
    }

    // getter 메서드 (JSON 변환 시 사용됨)
    public String getAccountId() {
        return accountId;
    }

    public String getPassword() {
        return password;
    }
}
