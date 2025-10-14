package com.example.semiwiki.Board;

import java.util.List;

public class BoardItem {
    private final long id;
    private final String title;
    private final String editor;
    private final List<String> categories;

    public BoardItem(long id, String title, String editor, List<String> categories) {
        this.id = id;
        this.title = title;
        this.editor = editor;
        this.categories = categories;
    }

    public BoardItem(String title, String editor, List<String> categories) {
        this(-1L, title, editor, categories);
    }

    public long getId() { return id; }
    public String getTitle() { return title; }
    public String getEditor() { return editor; }
    public List<String> getCategories() { return categories; }
}
