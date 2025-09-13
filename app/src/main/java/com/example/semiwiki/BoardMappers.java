package com.example.semiwiki;

import java.util.ArrayList;
import java.util.List;

public class BoardMappers {

    public static BoardItem toBoardItem(BoardListItemDTO dto) {
        String title = dto.getTitle();
        String editor = "";
        if (dto.getUserPreview() != null) {
            editor = dto.getUserPreview().getAccountId();
        }
        List<String> categories = dto.getCategories();

        return new BoardItem(title, editor, categories);
    }

    public static List<BoardItem> toBoardItems(List<BoardListItemDTO> dtoList) {
        List<BoardItem> items = new ArrayList<>();
        if (dtoList == null) return items;

        for (BoardListItemDTO dto : dtoList) {
            items.add(toBoardItem(dto));
        }
        return items;
    }
}
