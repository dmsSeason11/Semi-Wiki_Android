package com.example.semiwiki;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.semiwiki.databinding.ActivityBoardBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BoardActivity extends AppCompatActivity {

    private ActivityBoardBinding binding;

    private BoardAdapter adapter;
    private final List<BoardItem> boardData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityBoardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 1) 햄버거 아이콘 -> 드로어 열기
        binding.ivMenu.setOnClickListener(v ->
                binding.drawerLayout.openDrawer(GravityCompat.START));

        // 2) 드로어 헤더의 "로그인" 글씨 클릭 -> 로그인 화면
        View header = binding.navView.getHeaderView(0);
        TextView tvLoginHeader = header.findViewById(R.id.tv_guest_login);
        tvLoginHeader.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        });

        // 3) RecyclerView + Adapter 연결
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BoardAdapter(new ArrayList<>());
        binding.recyclerView.setAdapter(adapter);

        binding.recyclerView.addItemDecoration(
                new DividerDecoration(this, 0xFF757575, 1f, 0f, 0f)
        );

        loadDummy();
        adapter.submitList(new ArrayList<>(boardData));

        setupTabs();
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

    /**  정렬 탭 */
    private void setupTabs() {
        View.OnClickListener tabClick = v -> {
            binding.tabNewest.setSelected(false);
            binding.tabLikes.setSelected(false);
            v.setSelected(true);

            if (v.getId() == R.id.tab_newest) {
                sortByNewest();
            } else {
                sortByLikes();
            }

            // 정렬 후 어댑터에 반영
            adapter.submitList(new ArrayList<>(boardData));
        };

        binding.tabNewest.setOnClickListener(tabClick);
        binding.tabLikes.setOnClickListener(tabClick);
        binding.tabNewest.setSelected(true);
    }

    /** 최신순 */
    private void sortByNewest() {
        Collections.reverse(boardData);
    }

    /** 추천순 */
    private void sortByLikes() {
        Collections.sort(boardData, (a, b) -> {
            int ca = a.getCategories() == null ? 0 : a.getCategories().size();
            int cb = b.getCategories() == null ? 0 : b.getCategories().size();
            return Integer.compare(cb, ca);
        });
    }

    /** 임시 데이터*/
    private void loadDummy() {
        boardData.clear();
        boardData.add(new BoardItem("롤 현역 유럽파 축구 선수 +5", "wjddlfnd",
                Arrays.asList("전공", "기숙사", "논란")));
        boardData.add(new BoardItem("롤 현역 유럽파 축구 선수 +5", "wjddlfnd",
                Arrays.asList("전공", "기숙사", "논란")));
        boardData.add(new BoardItem("롤 현역 유럽파 축구 선수 +5", "wjddlfnd",
                Arrays.asList("전공", "기숙사", "논란")));
        boardData.add(new BoardItem("롤 현역 유럽파 축구 선수 +5", "wjddlfnd",
                Arrays.asList("전공", "기숙사", "논란")));
        boardData.add(new BoardItem("롤 현역 유럽파 축구 선수 +5", "wjddlfnd",
                Arrays.asList("전공", "기숙사", "논란")));
        boardData.add(new BoardItem("롤 현역 유럽파 축구 선수 +5", "wjddlfnd",
                Arrays.asList("전공", "기숙사", "논란")));
        boardData.add(new BoardItem("롤 현역 유럽파 축구 선수 +5", "wjddlfnd",
                Arrays.asList("전공", "기숙사", "논란")));
        boardData.add(new BoardItem("롤 현역 유럽파 축구 선수 +5", "wjddlfnd",
                Arrays.asList("전공", "기숙사", "논란")));
        boardData.add(new BoardItem("롤 현역 유럽파 축구 선수 +5", "wjddlfnd",
                Arrays.asList("전공", "기숙사", "논란")));
        boardData.add(new BoardItem("롤 현역 유럽파 축구 선수 +5", "wjddlfnd",
                Arrays.asList("전공", "기숙사", "논란")));
        boardData.add(new BoardItem("롤 현역 유럽파 축구 선수 +5", "wjddlfnd",
                Arrays.asList("전공", "기숙사", "논란")));
        boardData.add(new BoardItem("롤 현역 유럽파 축구 선수 +5", "wjddlfnd",
                Arrays.asList("전공", "기숙사", "논란")));
        boardData.add(new BoardItem("롤 현역 유럽파 축구 선수 +5", "wjddlfnd",
                Arrays.asList("전공", "기숙사", "논란")));
        boardData.add(new BoardItem("세미위키 안드로이드 퍼블리싱", "anseha",
                Arrays.asList("전공")));
        boardData.add(new BoardItem("백엔드", "wjddlfnd",
                Arrays.asList("전공")));
    }
}
