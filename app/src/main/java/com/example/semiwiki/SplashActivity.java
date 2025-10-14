// SplashActivity.java
package com.example.semiwiki;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.semiwiki.Login.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    private static final String PREF = "semiwiki_prefs";
    private static final String KEY_AT = "access_token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            SharedPreferences prefs = getSharedPreferences(PREF, MODE_PRIVATE);
            String token = prefs.getString(KEY_AT, null);

            Intent next = (token == null || token.trim().isEmpty())
                    ? new Intent(this, LoginActivity.class)
                    : new Intent(this, BoardActivity.class);

            next.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(next);
            finish();
        }, 1000); // ← 1초
    }
}
