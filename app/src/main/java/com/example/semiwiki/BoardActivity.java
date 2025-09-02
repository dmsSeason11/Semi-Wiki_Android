package com.example.semiwiki;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.semiwiki.databinding.ActivityBoardBinding;

public class BoardActivity extends AppCompatActivity {

    private ActivityBoardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityBoardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 1) 햄버거 아이콘 -> 드로어 열기
        binding.ivMenu.setOnClickListener(v ->
                binding.drawerLayout.openDrawer(GravityCompat.START));

        // 2) 드로어 헤더의 "로그인" 글씨 클릭 -> 로그인 화면
        //    (activity_board.xml 의 NavigationView에 app:headerLayout="@layout/drawer_header" 가 있어야 함)
        View header = binding.navView.getHeaderView(0);
        TextView tvLoginHeader = header.findViewById(R.id.tv_guest_login);
        tvLoginHeader.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        });

        // 탭 토글
        setupTabs();

        // RecyclerView (임시)
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // binding.recyclerView.setAdapter(...);  // 나중에 연결
    }

    // 뒤로가기: 드로어가 열려 있으면 닫기
    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        super.onBackPressed();
    }

    private void setupTabs() {
        View.OnClickListener tabClick = v -> {
            binding.tabNewest.setSelected(false);
            binding.tabViews.setSelected(false);
            binding.tabLikes.setSelected(false);
            v.setSelected(true);
            // TODO: 정렬 기준 바꾸고 어댑터 갱신
        };
        binding.tabNewest.setOnClickListener(tabClick);
        binding.tabViews.setOnClickListener(tabClick);
        binding.tabLikes.setOnClickListener(tabClick);
        binding.tabNewest.setSelected(true);
    }
}
