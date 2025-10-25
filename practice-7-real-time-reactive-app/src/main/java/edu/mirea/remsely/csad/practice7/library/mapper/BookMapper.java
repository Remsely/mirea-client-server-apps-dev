package edu.mirea.remsely.csad.practice7.library.mapper;

import edu.mirea.remsely.csad.practice7.library.dto.BookDto;
import edu.mirea.remsely.csad.practice7.library.entity.BookEntity;

public class BookMapper {
    public static BookDto toDto(BookEntity entity) {
        if (entity == null) return null;
        return new BookDto(entity.getId(), entity.getTitle(), entity.getAuthor());
    }

    public static BookEntity toEntity(BookDto dto) {
        if (dto == null) return null;
        return new BookEntity(dto.getId(), dto.getTitle(), dto.getAuthor());
    }
}
