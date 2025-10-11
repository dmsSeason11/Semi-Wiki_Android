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

        // 햄버거 아이콘 -> 드로어 열기
        binding.ivMenu.setOnClickListener(v ->
                binding.drawerLayout.openDrawer(GravityCompat.START)
        );

        setupUserDrawerHeader();

        //  RecyclerView + Adapter 연결
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

    private void setupUserDrawerHeader() {
        View header = binding.navView.getHeaderView(0);
        if (header == null) return;

        TextView tvUserId = header.findViewById(R.id.tv_user_id);
        TextView tvPostCountValue = header.findViewById(R.id.tv_post_count_value);
        View rowMyPosts = header.findViewById(R.id.row_my_posts);
        View rowLikedPosts = header.findViewById(R.id.row_liked_posts);
        View rowLogout = header.findViewById(R.id.layout_layout);

        // TODO: 로그인 시점에 저장해둔 값으로 교체
        tvUserId.setText("아이디: wjdidlfdnd");
        tvPostCountValue.setText("12");


        rowMyPosts.setOnClickListener(v ->
                binding.drawerLayout.closeDrawer(GravityCompat.START)
        );
        rowLikedPosts.setOnClickListener(v ->
                binding.drawerLayout.closeDrawer(GravityCompat.START)
        );
        rowLogout.setOnClickListener(v ->
                binding.drawerLayout.closeDrawer(GravityCompat.START)
        );
    }

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
