package edu.mirea.remsely.csad.semester7.practice5.controller;

import edu.mirea.remsely.csad.semester7.practice5.dto.FileInfoDto;
import edu.mirea.remsely.csad.semester7.practice5.dto.FileUploadDto;
import edu.mirea.remsely.csad.semester7.practice5.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @Value("${app.path.upload.file}")
    private String uploadPath;

    @GetMapping("/")
    public String index(Model model) {
        List<FileInfoDto> files = fileService.getSentFiles();
        model.addAttribute("files", files);
        model.addAttribute("appName", fileService.getAppName());
        model.addAttribute("totalFiles", files.size());
        return "index";
    }

    @PostMapping("/upload")
    public String uploadFiles(@ModelAttribute FileUploadDto fileUploadDto,
                              RedirectAttributes redirectAttributes) {
        try {
            if (fileUploadDto.files() == null || fileUploadDto.files().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Please select at least one file!");
                return "redirect:/";
            }

            fileService.save(fileUploadDto);
            redirectAttributes.addFlashAttribute(
                    "success",
                    "Successfully uploaded " + fileUploadDto.files().size() + " file(s)!");
            log.info("Successfully uploaded {} files via {}",
                    fileUploadDto.files().size(), fileService.getAppName());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to upload files: " + e.getMessage());
            log.error("Failed to upload files", e);
        }
        return "redirect:/";
    }

    @GetMapping("/upload-files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(uploadPath).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                log.warn("File not found or not readable: {}", filename);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error downloading file: {}", filename, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/health")
    @ResponseBody
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK - " + fileService.getAppName());
    }
}
