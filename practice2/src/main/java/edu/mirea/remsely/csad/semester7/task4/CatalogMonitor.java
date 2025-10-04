package edu.mirea.remsely.csad.semester7.task4;

import edu.mirea.remsely.csad.semester7.task3.Checksum16;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.file.StandardWatchEventKinds.*;

public class CatalogMonitor {
    private static class FileInfo {
        long size;
        int checksum;
        List<String> lines;

        FileInfo(long size, int checksum, List<String> lines) {
            this.size = size;
            this.checksum = checksum;
            this.lines = lines;
        }
    }

    private static final Map<Path, FileInfo> fileCache = new HashMap<>();

    public static void main(String[] args) throws IOException, InterruptedException {
        WatchService watchService = FileSystems.getDefault().newWatchService();

        Path dirToWatch = Paths.get("src/main/java/edu/mirea/remsely/csad/semester7/task4");

        dirToWatch.register(watchService, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);
        System.out.println("Monitoring catalog: " + dirToWatch.toAbsolutePath());

        System.out.println("Scanning catalog...");
        scanAndCacheDirectory(dirToWatch);
        System.out.println("Scanning completed. Waiting for events...\n");

        WatchKey key;
        while ((key = watchService.take()) != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();
                Path fileName = (Path) event.context();
                Path fullPath = dirToWatch.resolve(fileName);

                if (kind == ENTRY_CREATE) {
                    handleCreate(fullPath);
                } else if (kind == ENTRY_MODIFY) {
                    handleModify(fullPath);
                } else if (kind == ENTRY_DELETE) {
                    handleDelete(fullPath);
                }
            }
            key.reset();
        }
    }

    private static void scanAndCacheDirectory(Path dir) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path entry : stream) {
                if (!Files.isDirectory(entry)) {
                    updateCache(entry);
                }
            }
        }
    }

    private static void updateCache(Path filePath) {
        try {
            if (!Files.exists(filePath) || Files.isDirectory(filePath)) return;
            long size = Files.size(filePath);
            int checksum = Checksum16.simpleByteSum16(filePath);
            List<String> lines = Files.readAllLines(filePath);
            fileCache.put(filePath, new FileInfo(size, checksum, lines));
        } catch (IOException e) {
            System.err.printf("Error on file caching: %s %s ", e.getClass().getName(), e.getMessage());
        }
    }

    private static void handleCreate(Path filePath) {
        System.out.println("[CREATED] File: " + filePath.getFileName());
        updateCache(filePath);
    }

    private static void handleModify(Path filePath) {
        System.out.println("[UPDATED] File: " + filePath.getFileName());

        FileInfo oldInfo = fileCache.get(filePath);
        List<String> oldLines = oldInfo != null ? oldInfo.lines : List.of();

        try {
            List<String> newLines = Files.readAllLines(filePath);

            List<String> deletedLines = oldLines.stream()
                    .filter(line -> !newLines.contains(line))
                    .toList();

            List<String> addedLines = newLines.stream()
                    .filter(line -> !oldLines.contains(line))
                    .toList();

            if (!deletedLines.isEmpty()) {
                System.out.println("  Removed lines:");
                deletedLines.forEach(line -> System.out.println("    - " + line));
            }
            if (!addedLines.isEmpty()) {
                System.out.println("  Added lines:");
                addedLines.forEach(line -> System.out.println("    + " + line));
            }
        } catch (IOException e) {
            System.err.println("  Error on file reading to analyse changes.");
        }

        updateCache(filePath);
    }

    private static void handleDelete(Path filePath) {
        FileInfo cachedInfo = fileCache.get(filePath);
        if (cachedInfo != null) {
            System.out.println("[REMOVED] File: " + filePath.getFileName());
            System.out.printf("  Size: %d bytes%n", cachedInfo.size);
            System.out.printf("  Checksum: 0x%04X%n", cachedInfo.checksum);
            fileCache.remove(filePath);
        } else {
            System.out.println("[REMOVED] File (not in cache): " + filePath.getFileName());
        }
    }
}
