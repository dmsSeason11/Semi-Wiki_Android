package com.example.semiwiki.Board;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.semiwiki.Login.RetrofitInstance;
import com.example.semiwiki.Drawer.MyLikesActivity;
import com.example.semiwiki.Drawer.MyPostsActivity;
import com.example.semiwiki.R;
import com.example.semiwiki.databinding.ActivityBoardBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BoardActivity extends AppCompatActivity {

    private ActivityBoardBinding binding;
    private BoardAdapter adapter;

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

        // RecyclerView + Adapter 연결
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BoardAdapter(new ArrayList<>());
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addItemDecoration(
                new DividerDecoration(this, 0xFF757575, 1f, 0f, 0f)
        );

        loadBoardListFromApi();

        setupTabs();
    }

    private void setupUserDrawerHeader() {
        View header = binding.navView.getHeaderView(0);
        if (header == null) {
            header = binding.navView.inflateHeaderView(R.layout.drawer_header_user);
        }

        TextView tvUserId = header.findViewById(R.id.tv_user_id);
        TextView tvPostCountValue = header.findViewById(R.id.tv_post_count_value);
        View rowMyPosts    = header.findViewById(R.id.row_my_posts);
        View rowLikedPosts = header.findViewById(R.id.row_liked_posts);
        View rowLogout     = header.findViewById(R.id.layout_layout);

        // TODO: 로그인 시점에 저장해둔 값으로 교체
        tvUserId.setText("아이디: admin");
        tvPostCountValue.setText("12");

        // 화면 전환
        rowMyPosts.setOnClickListener(v -> {
            startActivity(new Intent(this, MyPostsActivity.class));
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        });

        rowLikedPosts.setOnClickListener(v -> {
            startActivity(new Intent(this, MyLikesActivity.class));
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        });

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

    /** 정렬 탭 */
    private void setupTabs() {
        View.OnClickListener tabClick = v -> {
            binding.tabNewest.setSelected(false);
            binding.tabLikes.setSelected(false);
            v.setSelected(true);

            if (v.getId() == R.id.tab_newest) {
                // 최신순
                loadBoardListFromApi("recent");
            } else {
                // 추천순
                loadBoardListFromApi("like");
            }
        };

        binding.tabNewest.setOnClickListener(tabClick);
        binding.tabLikes.setOnClickListener(tabClick);
        binding.tabNewest.setSelected(true);
    }

    /** 서버에서 게시글 목록 불러오기 */
    private void loadBoardListFromApi() {
        loadBoardListFromApi("recent");
    }

    private void loadBoardListFromApi(String orderBy) {
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        BoardService service = retrofit.create(BoardService.class);

        // 저장된 access_token 가져오기
        SharedPreferences prefs = getSharedPreferences("semiwiki_prefs", MODE_PRIVATE);
        String token = prefs.getString("access_token", null);
        if (token == null) {
            Log.e("BoardActivity", "토큰 없음");
            return;
        }

        service.getBoardList("Bearer " + token,
                null,   // keyword
                null,   // categories
                orderBy, // orderBy
                0,      // offset
                20      // limit
        ).enqueue(new Callback<List<BoardListItemDTO>>() {
            @Override
            public void onResponse(Call<List<BoardListItemDTO>> call,
                                   Response<List<BoardListItemDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<BoardItem> uiList = BoardMappers.toBoardItems(response.body());
                    adapter.submitList(uiList);
                } else {
                    Log.e("BoardActivity", "목록 불러오기 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<BoardListItemDTO>> call, Throwable t) {
                Log.e("BoardActivity", "네트워크 에러: " + t.getMessage(), t);
            }
        });
    }
}
