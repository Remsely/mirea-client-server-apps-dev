package edu.mirea.remsely.csad.practice7.library.mapper;

import edu.mirea.remsely.csad.practice7.library.dto.BookDto;
import edu.mirea.remsely.csad.practice7.library.entity.BookEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookMapperTest {

    @Test
    void toDto_shouldMapAllFields() {
        BookEntity entity = new BookEntity(1L, "Title", "Author");
        BookDto dto = BookMapper.toDto(entity);
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Title", dto.getTitle());
        assertEquals("Author", dto.getAuthor());
    }

    @Test
    void toEntity_shouldMapAllFields() {
        BookDto dto = new BookDto(2L, "T2", "A2");
        BookEntity entity = BookMapper.toEntity(dto);
        assertNotNull(entity);
        assertEquals(2L, entity.getId());
        assertEquals("T2", entity.getTitle());
        assertEquals("A2", entity.getAuthor());
    }

    @Test
    void nullSafety() {
        assertNull(BookMapper.toDto(null));
        assertNull(BookMapper.toEntity(null));
    }
}
