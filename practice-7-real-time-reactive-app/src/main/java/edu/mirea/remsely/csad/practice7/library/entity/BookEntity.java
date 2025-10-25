package edu.mirea.remsely.csad.practice7.library.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("book")
public class BookEntity {
    @Id
    private Long id;

    private String title;

    private String author;
}
