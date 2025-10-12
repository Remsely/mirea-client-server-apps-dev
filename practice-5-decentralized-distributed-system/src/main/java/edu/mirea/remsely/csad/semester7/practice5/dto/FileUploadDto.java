package edu.mirea.remsely.csad.semester7.practice5.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record FileUploadDto(List<MultipartFile> files) {
}
