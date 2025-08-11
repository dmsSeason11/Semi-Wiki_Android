package com.example.semiwiki;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1) 토큰 확인 (null / 빈문자 모두 체크)
        String token = getAccessToken();
        if (token == null || token.isEmpty()) {
            // 토큰 없으면 로그인 화면으로 이동
            startActivity(new Intent(this, LoginActivity.class));
            finish(); // 뒤로가기 시 메인 안 보이게 종료
            return;   // 아래 코드 실행 막기
        }

        // 2) 토큰 있으면 메인 화면 유지
        setContentView(R.layout.activity_main);

        // TODO: 메인 화면 초기화 작업 여기서 진행
    }

    @Nullable
    private String getAccessToken() {
        // 네가 쓰던 prefs 이름 그대로 사용
        String token = getSharedPreferences("semiwiki_prefs", MODE_PRIVATE)
                .getString("access_token", null);

        // 혹시 공백 문자열 저장된 경우 대비
        if (token != null && token.trim().isEmpty()) {
            return null;
        }
        return token;
    }
}
