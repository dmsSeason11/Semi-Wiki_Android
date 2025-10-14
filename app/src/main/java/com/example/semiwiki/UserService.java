package com.example.semiwiki;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {

    // 마이페이지(헤더용: 아이디/작성글 수)
    @GET("user/{accountId}")
    Call<MyPageDTO> getMyPage(
            @Header("Authorization") String bearerToken,
            @Path("accountId") String accountId
    );

    // 유저가 쓴 게시글 목록 (최신/추천 정렬 지원)
    @GET("user/{accountId}/list")
    Call<List<BoardListItemDTO>> getUserPosts(
            @Header("Authorization") String bearerToken,
            @Path("accountId") String accountId,
            @Query("keyword") String keyword,
            @Query("categories") List<String> categories,
            @Query("orderBy") String orderBy,   // "recent" | "like"
            @Query("offset") Integer offset,
            @Query("limit") Integer limit
    );
}
