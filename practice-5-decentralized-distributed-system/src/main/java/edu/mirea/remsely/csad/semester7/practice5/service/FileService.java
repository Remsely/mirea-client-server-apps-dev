package edu.mirea.remsely.csad.semester7.practice5.service;

import edu.mirea.remsely.csad.semester7.practice5.dto.FileInfoDto;
import edu.mirea.remsely.csad.semester7.practice5.dto.FileUploadDto;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {
    @Value("${app.path.upload.file}")
    private String uploadPath;

    @Getter
    @Value("${spring.application.name}")
    private String appName;

    private final SimpleDateFormat formatForDateNow = new SimpleDateFormat("HH:mm dd.MM.yyyy");

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(uploadPath));
            log.info("Upload directory created/verified at: {}", uploadPath);
            formatForDateNow.setTimeZone(TimeZone.getTimeZone("UTC"));
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload folder!", e);
        }
    }

    public void save(FileUploadDto fileUploadDTO) {
        for (MultipartFile multipartFile : fileUploadDTO.files()) {
            String newFileName = UUID.randomUUID() + "_" + multipartFile.getOriginalFilename();
            saveFile(multipartFile, newFileName);
        }
    }

    public List<FileInfoDto> getSentFiles() {
        List<FileInfoDto> fileInfoDtoList = new ArrayList<>();
        List<File> uploadFileList = getUploadFilesFromFolder();

        uploadFileList.forEach(file -> fileInfoDtoList.add(
                FileInfoDto.builder()
                        .name(file.getName())
                        .date(formatForDateNow.format(new Date(file.lastModified())))
                        .url("/upload-files/" + file.getName())  // ✅ Исправлено
                        .size(formatFileSize(file.length()))
                        .build()
        ));

        return fileInfoDtoList.stream()
                .sorted(Comparator.comparing(FileInfoDto::date).reversed())  // ✅ Новые файлы сверху
                .toList();
    }

    private void saveFile(MultipartFile file, String fileName) {
        try {
            Path filePath = Paths.get(uploadPath, fileName);
            Files.copy(file.getInputStream(), filePath);
            log.info("File saved successfully by {}: {}", appName, fileName);
        } catch (IOException e) {
            log.error("Failed to save file: {}", fileName, e);
            throw new RuntimeException("Failed to save file: " + fileName, e);
        }
    }

    private List<File> getUploadFilesFromFolder() {
        File folder = new File(uploadPath);
        File[] files = folder.listFiles();

        if (files == null) {
            return new ArrayList<>();
        }

        return Arrays.stream(files).filter(File::isFile).toList();
    }

    private String formatFileSize(long size) {
        if (size < 1024) return size + " B";
        if (size < 1024 * 1024) return String.format("%.2f KB", size / 1024.0);
        if (size < 1024 * 1024 * 1024) return String.format("%.2f MB", size / (1024.0 * 1024));
        return String.format("%.2f GB", size / (1024.0 * 1024 * 1024));
    }
}
