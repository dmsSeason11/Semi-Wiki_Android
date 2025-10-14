package com.example.semiwiki;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface LikeService {
    // 현재 유저가 이 게시글 좋아요 눌렀는지
    @GET("like/{boardId}")
    Call<Boolean> isLikedByMe(
            @Header("Authorization") String bearerToken,
            @Path("boardId") long boardId
    );
}
