package com.example.semiwiki.Board;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class BoardListItemDTO {
    @SerializedName("id") private int id;
    @SerializedName("title") private String title;
    @SerializedName("categories") private List<String> categories;

    @SerializedName(value = "userPreview", alternate = {"user_preview"})
    private UserPreview userPreview;

    public int getId() { return id; }
    public String getTitle() { return title; }
    public List<String> getCategories() { return categories; }
    public UserPreview getUserPreview() { return userPreview; }

    public static class UserPreview {
        @SerializedName(value = "userId", alternate = {"user_id"})
        private int userId;
        @SerializedName(value = "accountId", alternate = {"account_id"})
        private String accountId;

        public int getUserId() { return userId; }
        public String getAccountId() { return accountId; }
    }
}
