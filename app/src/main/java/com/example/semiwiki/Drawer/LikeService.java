package com.example.semiwiki.Drawer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface LikeService {
    @GET("like/{boardId}")
    Call<Boolean> isLikedByMe(
            @Header("Authorization") String bearerToken,
            @Path("boardId") long boardId
    );

    @GET("like/{boardId}/count")
    Call<Integer> getLikeCount(
            @Header("Authorization") String bearerToken,
            @Path("boardId") long boardId
    );
}
