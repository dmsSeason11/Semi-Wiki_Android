package com.example.semiwiki.Board;

import java.util.ArrayList;
import java.util.List;

public class BoardMappers {

    public static BoardItem toBoardItem(BoardListItemDTO dto) {
        String editor = (dto.getUserPreview() != null)
                ? dto.getUserPreview().getAccountId()
                : "";

        return new BoardItem(
                dto.getId(),
                dto.getTitle(),
                editor,
                dto.getCategories()
        );
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
