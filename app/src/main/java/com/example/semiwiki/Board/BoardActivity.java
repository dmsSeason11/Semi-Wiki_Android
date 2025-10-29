package com.example.semiwiki.Board;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.semiwiki.Drawer.MyLikesActivity;
import com.example.semiwiki.Drawer.MyPageDTO;
import com.example.semiwiki.Drawer.MyPostsActivity;
import com.example.semiwiki.Drawer.UserService;
import com.example.semiwiki.Login.AuthService;
import com.example.semiwiki.Login.LoginActivity;
import com.example.semiwiki.Login.RetrofitInstance;
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

    private static final String PREF = "semiwiki_prefs";
    private static final String KEY_AT = "access_token";
    private static final String KEY_ID = "account_id";

    private View searchBarContainer;
    private EditText etKeyword;
    private ImageView ivClear;

    private View emptyView;
    private View listCardContainer;
    private View boardTitleView;
    private View tabGroupView;

    private String currentOrderBy = "recent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences(PREF, MODE_PRIVATE);
        String token = prefs.getString(KEY_AT, null);
        if (token == null || token.isEmpty()) {
            goLoginAndFinish();
            return;
        }

        binding = ActivityBoardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ivMenu.setOnClickListener(v ->
                binding.drawerLayout.openDrawer(GravityCompat.START)
        );

        setupUserDrawerHeader();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BoardAdapter(new ArrayList<>());
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.addItemDecoration(
                new DividerDecoration(this, 0xFF757575, 1f, 0f, 0f)
        );

        adapter.setOnItemClickListener((item, position) -> {
            Intent i = new Intent(this, PostDetailActivity.class);
            i.putExtra(PostDetailActivity.EXTRA_BOARD_ID, item.getId());
            startActivity(i);
        });

        setupTabs();

        searchBarContainer = findViewById(R.id.include_search_bar_top);
        emptyView = findViewById(R.id.view_empty);
        listCardContainer = findViewById(R.id.layout_list_card);
        boardTitleView = findViewById(R.id.tv_board_text);
        tabGroupView = findViewById(R.id.tab_group);

        etKeyword = searchBarContainer.findViewById(R.id.et_keyword);
        ivClear = searchBarContainer.findViewById(R.id.iv_clear);

        ImageView ivMenu = binding.ivMenu;
        ImageView ivLogo = binding.ivLogo;
        ImageView ivSearch = binding.ivSearch;

        searchBarContainer.setVisibility(View.GONE);
        if (emptyView != null) emptyView.setVisibility(View.GONE);

        ivSearch.setOnClickListener(v -> {
            ivMenu.setVisibility(View.GONE);
            ivLogo.setVisibility(View.GONE);
            ivSearch.setVisibility(View.GONE);

            searchBarContainer.setVisibility(View.VISIBLE);
            etKeyword.requestFocus();
        });

        ivClear.setOnClickListener(v -> {
            etKeyword.setText("");

            searchBarContainer.setVisibility(View.GONE);
            ivMenu.setVisibility(View.VISIBLE);
            ivLogo.setVisibility(View.VISIBLE);
            ivSearch.setVisibility(View.VISIBLE);

            currentOrderBy = binding.tabNewest.isSelected() ? "recent" : "like";
            loadBoardListFromApi(currentOrderBy);

            if (listCardContainer != null) listCardContainer.setVisibility(View.VISIBLE);
            if (boardTitleView != null) boardTitleView.setVisibility(View.VISIBLE);
            if (tabGroupView != null) tabGroupView.setVisibility(View.VISIBLE);
            if (emptyView != null) emptyView.setVisibility(View.GONE);
            binding.recyclerView.setVisibility(View.VISIBLE);
        });

        etKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyword = s.toString().trim();

                if (keyword.isEmpty()) {
                    currentOrderBy = binding.tabNewest.isSelected() ? "recent" : "like";
                    loadBoardListFromApi(currentOrderBy);

                    if (listCardContainer != null) listCardContainer.setVisibility(View.VISIBLE);
                    if (boardTitleView != null) boardTitleView.setVisibility(View.VISIBLE);
                    if (tabGroupView != null) tabGroupView.setVisibility(View.VISIBLE);
                    if (emptyView != null) emptyView.setVisibility(View.GONE);

                    binding.recyclerView.setVisibility(View.VISIBLE);
                } else {
                    searchBoardFromApi(keyword, currentOrderBy);
                }
                ivClear.setVisibility(keyword.isEmpty() ? View.GONE : View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        loadBoardListFromApi(currentOrderBy);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = getSharedPreferences(PREF, MODE_PRIVATE);
        String token = prefs.getString(KEY_AT, null);
        if (token == null || token.isEmpty()) {
            goLoginAndFinish();
        }
    }

    private void setupUserDrawerHeader() {
        View header = binding.navView.getHeaderView(0);
        if (header == null) {
            header = binding.navView.inflateHeaderView(R.layout.drawer_header_user);
        }

        TextView tvUserId = header.findViewById(R.id.tv_user_id);
        TextView tvPostCountValue = header.findViewById(R.id.tv_post_count_value);
        View rowMyPosts = header.findViewById(R.id.row_my_posts);
        View rowLikedPosts = header.findViewById(R.id.row_liked_posts);
        View rowLogout = header.findViewById(R.id.layout_layout);

        SharedPreferences prefs = getSharedPreferences(PREF, MODE_PRIVATE);
        String accountId = prefs.getString(KEY_ID, "-");
        tvUserId.setText("아이디: " + accountId);
        tvPostCountValue.setText("0");

        fillHeaderFromApi(tvUserId, tvPostCountValue);

        rowMyPosts.setOnClickListener(v -> {
            startActivity(new Intent(this, MyPostsActivity.class));
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        });

        rowLikedPosts.setOnClickListener(v -> {
            startActivity(new Intent(this, MyLikesActivity.class));
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        });

        rowLogout.setOnClickListener(v -> {
            AuthService auth = RetrofitInstance.getAuthService();
            auth.logout(accountId).enqueue(new retrofit2.Callback<Void>() {
                @Override public void onResponse(retrofit2.Call<Void> call, retrofit2.Response<Void> resp) {
                    Log.d("BoardActivity", "logout resp=" + resp.code());
                }
                @Override public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                    Log.w("BoardActivity", "logout call failed: " + t.getMessage());
                }
            });

            prefs.edit()
                    .remove(KEY_AT)
                    .remove("refresh_token")
                    .remove(KEY_ID)
                    .apply();

            goLoginAndFinish();
        });
    }

    private void fillHeaderFromApi(TextView tvUserId, TextView tvPostCountValue) {
        SharedPreferences prefs = getSharedPreferences(PREF, MODE_PRIVATE);
        String token = prefs.getString(KEY_AT, null);
        String accountId = prefs.getString(KEY_ID, null);

        if (token == null || accountId == null) {
            Log.w("BoardActivity", "token/accountId 누락");
            handleAuthError();
            return;
        }

        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        UserService userService = retrofit.create(UserService.class);

        userService.getMyPage("Bearer " + token, accountId)
                .enqueue(new Callback<MyPageDTO>() {
                    @Override public void onResponse(Call<MyPageDTO> call, Response<MyPageDTO> resp) {
                        if (resp.code() == 401 || resp.code() == 403) { handleAuthError(); return; }
                        if (resp.code() == 404) {
                            Toast.makeText(BoardActivity.this, "해당 아이디의 사용자를 찾을 수 없어요.", Toast.LENGTH_SHORT).show();
                            tvUserId.setText("아이디: " + accountId);
                            tvPostCountValue.setText("0");
                            return;
                        }
                        if (resp.isSuccessful() && resp.body() != null) {
                            tvUserId.setText("아이디: " + resp.body().getAccountId());
                            tvPostCountValue.setText(String.valueOf(resp.body().getNoticeBoardCount()));
                        } else {
                            Log.w("BoardActivity", "mypage 응답코드: " + resp.code());
                        }
                    }
                    @Override public void onFailure(Call<MyPageDTO> call, Throwable t) {
                        Log.e("BoardActivity", "mypage 실패: " + t.getMessage());
                    }
                });
    }

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
            binding.tabLikes.setSelected(false);
            v.setSelected(true);

            if (v.getId() == R.id.tab_newest) {
                currentOrderBy = "recent";
            } else {
                currentOrderBy = "like";
            }

            if (etKeyword != null && etKeyword.getText() != null && etKeyword.getText().length() > 0) {
                searchBoardFromApi(etKeyword.getText().toString().trim(), currentOrderBy);
            } else {
                loadBoardListFromApi(currentOrderBy);
            }
        };

        binding.tabNewest.setOnClickListener(tabClick);
        binding.tabLikes.setOnClickListener(tabClick);
        binding.tabNewest.setSelected(true);
    }

    private void loadBoardListFromApi() { loadBoardListFromApi("recent"); }

    private void loadBoardListFromApi(String orderBy) {
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        BoardService service = retrofit.create(BoardService.class);

        SharedPreferences prefs = getSharedPreferences(PREF, MODE_PRIVATE);
        String token = prefs.getString(KEY_AT, null);
        if (token == null || token.isEmpty()) {
            handleAuthError();
            return;
        }

        service.getBoardList("Bearer " + token,
                null,
                null,
                orderBy,
                0,
                20
        ).enqueue(new Callback<List<BoardListItemDTO>>() {
            @Override
            public void onResponse(Call<List<BoardListItemDTO>> call,
                                   Response<List<BoardListItemDTO>> response) {
                if (response.code() == 401 || response.code() == 403) { handleAuthError(); return; }
                if (response.code() == 204) {
                    adapter.submitList(new ArrayList<>());
                    return;
                }

                if (response.isSuccessful() && response.body() != null) {
                    List<BoardItem> uiList = BoardMappers.toBoardItems(response.body());
                    adapter.submitList(uiList);

                    showListState(uiList);
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

    private void searchBoardFromApi(String keyword, String orderBy) {
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        BoardService service = retrofit.create(BoardService.class);

        SharedPreferences pref = getSharedPreferences(PREF, MODE_PRIVATE);
        String token = pref.getString(KEY_AT, null);
        if (token == null || token.isEmpty()) {
            handleAuthError();
            return;
        }

        service.getBoardList(
                "Bearer " + token,
                keyword,
                null,
                orderBy,
                0,
                20
        ).enqueue(new Callback<List<BoardListItemDTO>>() {
            @Override
            public void onResponse(Call<List<BoardListItemDTO>> call, Response<List<BoardListItemDTO>> response) {
                if (response.code() == 401 || response.code() == 403) { handleAuthError(); return; }

                if (response.isSuccessful() && response.body() != null) {
                    List<BoardListItemDTO> body = response.body();
                    if (body.isEmpty()) {
                        showEmptyState();
                    } else {
                        List<BoardItem> uiList = BoardMappers.toBoardItems(body);
                        showListState(uiList);
                    }
                } else {
                    showEmptyState();
                }
            }

            @Override
            public void onFailure(Call<List<BoardListItemDTO>> call, Throwable t) {
                Log.e("BoardActivity", "검색 실패: " + t.getMessage());
                showEmptyState();
            }
        });
    }

    private void showEmptyState() {
        if (boardTitleView != null) boardTitleView.setVisibility(View.GONE);
        if (tabGroupView != null) tabGroupView.setVisibility(View.GONE);
        if (listCardContainer != null) listCardContainer.setVisibility(View.GONE);
        binding.recyclerView.setVisibility(View.GONE);

        if (emptyView != null) emptyView.setVisibility(View.VISIBLE);

        adapter.submitList(new ArrayList<>());
    }

    private void showListState(List<BoardItem> uiList) {
        if (boardTitleView != null) boardTitleView.setVisibility(View.VISIBLE);
        if (tabGroupView != null) tabGroupView.setVisibility(View.VISIBLE);
        if (listCardContainer != null) listCardContainer.setVisibility(View.VISIBLE);

        if (emptyView != null)emptyView.setVisibility(View.GONE);

        binding.recyclerView.setVisibility(View.VISIBLE);
        adapter.submitList(uiList);
    }

    private void handleAuthError() {
        Toast.makeText(this, "로그인이 만료되었어요. 다시 로그인해주세요.", Toast.LENGTH_SHORT).show();
        SharedPreferences prefs = getSharedPreferences(PREF, MODE_PRIVATE);
        prefs.edit()
                .remove(KEY_AT)
                .remove("refresh_token")
                .remove(KEY_ID)
                .apply();
        goLoginAndFinish();
    }

    private void goLoginAndFinish() {
        Intent i = new Intent(this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }
}
