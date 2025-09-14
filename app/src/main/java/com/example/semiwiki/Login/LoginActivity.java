package com.example.semiwiki.Login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.semiwiki.Board.BoardActivity;
import com.example.semiwiki.databinding.ActivityLoginBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding; // ViewBinding 변수 선언

    // 토큰 저장용 키(간단 버전). 프로젝트 크면 TokenStore 유틸로 분리 권장
    private static final String PREF = "semiwiki_prefs";
    private static final String KEY_AT = "access_token";
    private static final String KEY_RT = "refresh_token";
    private static final String KEY_ACC_ID = "account_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ViewBinding 초기화
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // [자동 로그인] 이미 토큰 있으면 바로 메인으로 이동
        String existingToken = getSharedPreferences(PREF, MODE_PRIVATE).getString(KEY_AT, null);
        if (existingToken != null && !existingToken.isEmpty()) {
            moveToBoard();
            return;
        }

        // 로그인 버튼 클릭 시 실행
        binding.loginButton.setOnClickListener(v -> {
            String id = binding.loginInputId.getText().toString().trim(); // 아이디 입력값
            String pw = binding.loginInputPw.getText().toString().trim(); // 비번 입력값

            // [입력 검증] 비어있으면 바로 안내 후 리턴
            if (id.isEmpty()) {
                binding.loginInputId.setError("아이디를 입력해주세요");
                return;
            }
            if (pw.isEmpty()) {
                binding.loginInputPw.setError("비밀번호를 입력해주세요");
                return;
            }

            login(id, pw); // 로그인 함수 호출
        });
    }

    private void moveToBoard() {
        Intent intent = new Intent(LoginActivity.this, BoardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    // 로그인 요청 함수
    private void login(String id, String pw) {
        // 버튼 연타 방지(간단). 실제로는 ProgressBar 사용 추천
        binding.loginButton.setEnabled(false);

        LoginRequest request = new LoginRequest(id, pw); // 요청 객체 생성
        AuthService service = RetrofitInstance.getAuthService(); // 서비스 인스턴스 얻기
        Call<LoginResponse> call = service.signIn(request);      // API 호출

        // 비동기로 요청 실행
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                binding.loginButton.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    // 로그인 성공: access_token / refresh_token 꺼내기
                    // ※ LoginResponse는 @SerializedName("access_token") 매핑 후 getAccessToken() 으로 받는 형태 권장
                    String accessToken  = response.body().getAccessToken();
                    String refreshToken = response.body().getRefreshToken();

                    String accountIdFromServer = response.body().getAccountId();
                    String accountIdToSave = (accountIdFromServer != null && !accountIdFromServer.isEmpty())
                            ? accountIdFromServer : id;

                    // 토큰 저장
                    getSharedPreferences(PREF, MODE_PRIVATE)
                            .edit()
                            .putString(KEY_AT, accessToken)
                            .putString(KEY_RT, refreshToken)
                            .putString(KEY_ACC_ID, accountIdToSave)
                            .putString("user_id", id)
                            .apply();

                    Toast.makeText(LoginActivity.this, "로그인 성공!", Toast.LENGTH_SHORT).show();
                    moveToBoard();
                } else {
                    //  로그인 실패: 상태코드에 따라 안내
                    int code = response.code();
                    String msg;
                    if (code == 403) {
                        msg = "비밀번호가 올바르지 않습니다. (403)";
                    } else if (code == 404) {
                        msg = "존재하지 않는 아이디입니다. (404)";
                    } else if (code == 400) {
                        msg = "요청 형식 오류입니다. (400)";
                    } else {
                        msg = "로그인 실패 (" + code + ")";
                    }
                    Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                binding.loginButton.setEnabled(true);
                // 통신 자체가 실패 (서버 꺼짐, 인터넷 끊김 등)
                Toast.makeText(LoginActivity.this, "네트워크 오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
