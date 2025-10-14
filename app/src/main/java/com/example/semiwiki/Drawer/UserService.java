package com.example.semiwiki.Drawer;

import com.example.semiwiki.Board.BoardListItemDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {

    // 마이페이지(헤더)
    @GET("user/{accountId}")
    Call<MyPageDTO> getMyPage(
            @Header("Authorization") String bearerToken,
            @Path("accountId") String accountId
    );

    // 내가 쓴 글 (서버 기본 정렬: 최신순)
    @GET("user/{accountId}/list")
    Call<List<BoardListItemDTO>> getUserPosts(
            @Header("Authorization") String bearerToken,
            @Path("accountId") String accountId,
            @Query("orderBy") String orderBy,   // "recent" | "like"
            @Query("offset") Integer offset,
            @Query("limit") Integer limit
    );

    // 내가 좋아요한 글 (최근 좋아요 순)
    @GET("user/{accountId}/list/like")
    Call<List<BoardListItemDTO>> getUserLikedPosts(
            @Header("Authorization") String bearerToken,
            @Path("accountId") String accountId,
            @Query("offset") Integer offset,
            @Query("limit") Integer limit
    );
}
