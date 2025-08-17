package com.example.semiwiki;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.semiwiki.databinding.ActivityBoardBinding;

public class BoardActivity extends AppCompatActivity {

    private ActivityBoardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ViewBinding으로 레이아웃 연결
        binding = ActivityBoardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 탭 토글 세팅 (여기에 넣는 거!)
        setupTabs();

        // (선택) 리사이클러 뷰 세팅 예시
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // binding.recyclerView.setAdapter(...);
    }

    private void setupTabs() {
        View.OnClickListener tabClick = v -> {
            // 모두 해제
            binding.tabNewest.setSelected(false);
            binding.tabViews.setSelected(false);
            binding.tabLikes.setSelected(false);

            // 클릭된 것만 선택
            v.setSelected(true);

            // TODO: 정렬 기준 바꿔서 어댑터 갱신
            // if (v.getId() == R.id.tabNewest) { ... }
        };

        binding.tabNewest.setOnClickListener(tabClick);
        binding.tabViews.setOnClickListener(tabClick);
        binding.tabLikes.setOnClickListener(tabClick);

        // 초기 선택(시작 시 최신순 선택)
        binding.tabNewest.setSelected(true);
    }
}
