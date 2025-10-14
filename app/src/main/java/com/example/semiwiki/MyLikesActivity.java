package com.example.semiwiki;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.semiwiki.databinding.ActivityMyLikesBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyLikesActivity extends AppCompatActivity {

    private static final String TAG = "MyLikes";

    private ActivityMyLikesBinding binding;
    private BoardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyLikesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 햄버거 → 드로어
        binding.ivMenu.setOnClickListener(v ->
                binding.drawerLayout.openDrawer(GravityCompat.START)
        );

        setupUserDrawerHeader();

        View tabGroup = findViewById(R.id.tab_group);
        if (tabGroup != null) tabGroup.setVisibility(View.GONE);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BoardAdapter(new ArrayList<>());
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addItemDecoration(
                new DividerDecoration(this, 0xFF757575, 1f, 0f, 0f)
        );

        // api 아직 안 나와서 임시 방식으로 내가 좋아요한 글 화면 구성
        loadMyLikedPosts();
    }

    private void setupUserDrawerHeader() {
        View header = binding.navView.getHeaderView(0);
        if (header == null) header = binding.navView.inflateHeaderView(R.layout.drawer_header_user);

        TextView tvUserId = header.findViewById(R.id.tv_user_id);
        TextView tvPostCountValue = header.findViewById(R.id.tv_post_count_value);
        View rowMyPosts    = header.findViewById(R.id.row_my_posts);
        View rowLikedPosts = header.findViewById(R.id.row_liked_posts);
        View rowLogout     = header.findViewById(R.id.layout_layout);

        fillHeaderFromApi(tvUserId, tvPostCountValue);

        rowMyPosts.setOnClickListener(v -> {
            startActivity(new Intent(this, MyPostsActivity.class));
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        });
        rowLikedPosts.setOnClickListener(v ->
                binding.drawerLayout.closeDrawer(GravityCompat.START)
        );
        rowLogout.setOnClickListener(v ->
                binding.drawerLayout.closeDrawer(GravityCompat.START)
        );
    }

    private void fillHeaderFromApi(TextView tvUserId, TextView tvPostCountValue) {
        SharedPreferences prefs = getSharedPreferences("semiwiki_prefs", MODE_PRIVATE);
        String token     = prefs.getString("access_token", null);
        String accountId = prefs.getString("account_id", null);
        if (token == null || accountId == null) return;

        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        UserService userService = retrofit.create(UserService.class);

        userService.getMyPage("Bearer " + token, accountId)
                .enqueue(new Callback<MyPageDTO>() {
                    @Override public void onResponse(Call<MyPageDTO> call, Response<MyPageDTO> resp) {
                        if (resp.isSuccessful() && resp.body() != null) {
                            tvUserId.setText("아이디: " + resp.body().getAccountId());
                            tvPostCountValue.setText(String.valueOf(resp.body().getNoticeBoardCount()));
                        } else if (resp.code() == 401 || resp.code() == 403) {
                            handleAuthError();
                        } else {
                            Log.w(TAG, "mypage 응답코드: " + resp.code());
                        }
                    }
                    @Override public void onFailure(Call<MyPageDTO> call, Throwable t) {
                        Log.e(TAG, "mypage 실패: " + t.getMessage());
                    }
                });
    }

    /** 임시 구현(라이트): 목록 1페이지 가져와서 per-item 좋아요 여부 검사 → true만 표시 */
    private void loadMyLikedPosts() {
        SharedPreferences prefs = getSharedPreferences("semiwiki_prefs", MODE_PRIVATE);
        String token = prefs.getString("access_token", null);
        if (token == null) {
            Log.e(TAG, "token 누락");
            adapter.submitList(Collections.emptyList());
            return;
        }

        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        BoardService boardService = retrofit.create(BoardService.class);
        LikeService likeService   = retrofit.create(LikeService.class);

        // 1) 공용 목록 한 페이지 가져오기
        boardService.getBoardList(
                "Bearer " + token,
                null, null, null,
                0, 20
        ).enqueue(new Callback<List<BoardListItemDTO>>() {
            @Override public void onResponse(Call<List<BoardListItemDTO>> call,
                                             Response<List<BoardListItemDTO>> resp) {
                if (resp.code() == 204) { // 빈 목록
                    adapter.submitList(Collections.emptyList());
                    return;
                }
                if (!resp.isSuccessful() || resp.body() == null) {
                    if (resp.code() == 401 || resp.code() == 403) {
                        handleAuthError();
                    } else {
                        Log.e(TAG, "list 실패: " + resp.code());
                        Toast.makeText(MyLikesActivity.this,
                                "목록을 불러오지 못했어요 (" + resp.code() + ")", Toast.LENGTH_SHORT).show();
                    }
                    adapter.submitList(Collections.emptyList());
                    return;
                }

                List<BoardListItemDTO> all = resp.body();
                if (all.isEmpty()) { adapter.submitList(Collections.emptyList()); return; }

                // 2) 각 글에 대해 좋아요 여부 확인
                List<BoardListItemDTO> liked = new ArrayList<>();
                AtomicInteger pending = new AtomicInteger(all.size());

                for (BoardListItemDTO dto : all) {
                    likeService.isLikedByMe("Bearer " + token, dto.getId())
                            .enqueue(new Callback<Boolean>() {
                                @Override public void onResponse(Call<Boolean> c, Response<Boolean> r) {
                                    if (r.code() == 401 || r.code() == 403) {
                                        handleAuthError();
                                    } else if (r.isSuccessful() && Boolean.TRUE.equals(r.body())) {
                                        synchronized (liked) { liked.add(dto); }
                                    }
                                    if (pending.decrementAndGet() == 0) {
                                        adapter.submitList(BoardMappers.toBoardItems(liked));
                                    }
                                }
                                @Override public void onFailure(Call<Boolean> c, Throwable t) {
                                    Log.w(TAG, "like check 실패 id=" + dto.getId() + " " + t.getMessage());
                                    if (pending.decrementAndGet() == 0) {
                                        adapter.submitList(BoardMappers.toBoardItems(liked));
                                    }
                                }
                            });
                }
            }
            @Override public void onFailure(Call<List<BoardListItemDTO>> call, Throwable t) {
                Log.e(TAG, "목록 네트워크 에러: " + t.getMessage(), t);
                adapter.submitList(Collections.emptyList());
                Toast.makeText(MyLikesActivity.this,
                        "네트워크 오류가 발생했어요", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleAuthError() {
        Toast.makeText(this, "로그인이 만료되었어요. 다시 로그인해주세요.", Toast.LENGTH_SHORT).show();
        SharedPreferences prefs = getSharedPreferences("semiwiki_prefs", MODE_PRIVATE);
        prefs.edit().remove("access_token").apply();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        super.onBackPressed();
    }
}
