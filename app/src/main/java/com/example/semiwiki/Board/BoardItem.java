package com.example.semiwiki.Board;

import java.util.List;

public class BoardItem {
    private final String title;
    private final String editor;
    private final List<String> categories;

    public BoardItem(String title, String editor, List<String> categories) {
        this.title = title;
        this.editor = editor;
        this.categories = categories;
    }

    public String getTitle() { return title; }
    public String getEditor() { return editor; }
    public List <String> getCategories() { return categories; }

}
