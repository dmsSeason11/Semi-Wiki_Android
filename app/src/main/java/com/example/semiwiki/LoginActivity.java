package com.example.semiwiki;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.semiwiki.databinding.ActivityLoginBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding; // ViewBinding 변수 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ViewBinding 초기화
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 로그인 버튼 클릭 시 실행
        binding.buttonLogin.setOnClickListener(v -> {
            String id = binding.editTextId.getText().toString(); // 아이디 입력값
            String pw = binding.editTextPw.getText().toString(); // 비번 입력값

            login(id, pw); // 로그인 함수 호출
        });
    }

    // 로그인 요청 함수
    private void login(String id, String pw) {
        LoginRequest request = new LoginRequest(id, pw); // 요청 객체 생성
        AuthService service = RetrofitInstance.getAuthService(); // 서비스 인스턴스 얻기

        Call<LoginResponse> call = service.signIn(request); // API 호출

        // 비동기로 요청 실행
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    // 로그인 성공 access_token 꺼내기
                    String token = response.body().getAccess_token();
                    Toast.makeText(LoginActivity.this, "로그인 성공\n토큰: " + token, Toast.LENGTH_SHORT).show();
                } else {
                    // 로그인 실패
                    Toast.makeText(LoginActivity.this, "실패: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // 통신 자체가 실패 (서버 꺼짐, 인터넷 끊김 등)
                Toast.makeText(LoginActivity.this, "서버 연결 실패: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

