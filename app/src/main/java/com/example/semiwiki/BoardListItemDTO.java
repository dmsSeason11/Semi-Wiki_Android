package com.example.semiwiki;

import java.util.List;

public class BoardListItemDTO {
    private int id;
    private String title;
    private List<String> categories;
    private UserPreview userPreview;

    public int getId() { return id; }
    public String getTitle() { return title; }
    public List<String> getCategories() { return categories; }
    public UserPreview getUserPreview() { return userPreview; }

    public static class UserPreview {
        private int userId;
        private String accountId;

        public int getUserId() { return userId; }
        public String getAccountId() { return accountId; }
    }
}
