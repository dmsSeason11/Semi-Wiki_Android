package com.example.semiwiki;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface BoardService {
    @GET("notice-board/list")
    Call<List<BoardListItemDTO>> getBoardList(
            @Header("Authorization") String bearerToken,
            @Query("keyword") String keyword,
            @Query("categories") List<String> categories,
            @Query("orderBy") String orderBy,
            @Query("offset") Integer offset,
            @Query("limit") Integer limit
    );
}
