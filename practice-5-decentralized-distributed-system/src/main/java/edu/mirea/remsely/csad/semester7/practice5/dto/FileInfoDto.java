package edu.mirea.remsely.csad.semester7.practice5.dto;

import lombok.Builder;

@Builder
public record FileInfoDto(String name, String date, String url, String size) {
}
