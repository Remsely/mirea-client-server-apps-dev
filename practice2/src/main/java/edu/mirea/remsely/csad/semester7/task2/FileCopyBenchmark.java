package edu.mirea.remsely.csad.semester7.task2;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Locale;

public class FileCopyBenchmark {
    public static void main(String[] args) throws Exception {
        Path source = Path.of(Constants.FILE_NAME);

        long size = Files.size(source);
        System.out.printf("File to copy size: %.2f MB%n", size / 1024.0 / 1024.0);

        copyAndMeasure("Streams (FileInputStream/FileOutputStream)",
                source, Path.of(Constants.PATH + "copy_streams.bin"), FileCopyBenchmark::copyStreams);

        copyAndMeasure("FileChannel transferTo",
                source, Path.of(Constants.PATH + "copy_channel.bin"), FileCopyBenchmark::copyChannel);

        copyAndMeasure("Apache Commons IO (FileUtils.copyFile)",
                source, Path.of(Constants.PATH + "copy_commons.bin"), FileCopyBenchmark::copyCommons);

        copyAndMeasure("Files.copy",
                source, Path.of(Constants.PATH + "copy_files.bin"), FileCopyBenchmark::copyFiles);
    }

    private static void copyStreams(Path source, Path target) throws IOException {
        byte[] buffer = new byte[64 * 1024];
        try (InputStream in = new FileInputStream(source.toFile());
             OutputStream out = new FileOutputStream(target.toFile())) {
            int read;
            while ((read = in.read(buffer)) >= 0) {
                if (read > 0) out.write(buffer, 0, read);
            }
        }
    }

    private static void copyChannel(Path source, Path target) throws IOException {
        try (FileChannel in = FileChannel.open(source, StandardOpenOption.READ);
             FileChannel out = FileChannel.open(target,
                     StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
            long size = in.size();
            long pos = 0;
            while (pos < size) {
                long transferred = in.transferTo(pos, size - pos, out);
                if (transferred <= 0) break;
                pos += transferred;
            }
        }
    }

    private static void copyCommons(Path source, Path target) throws IOException {
        FileUtils.copyFile(source.toFile(), target.toFile());
    }

    private static void copyFiles(Path source, Path target) throws IOException {
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
    }

    @FunctionalInterface
    interface Copier {
        void copy(Path src, Path dst) throws IOException;
    }

    private static void copyAndMeasure(String title, Path source, Path target, Copier copier) throws IOException {
        Files.deleteIfExists(target);
        System.gc();
        long memBefore = usedMem();
        long t0 = System.nanoTime();
        copier.copy(source, target);
        long t1 = System.nanoTime();
        long memAfter = usedMem();

        long bytes = Files.size(target);
        double ms = (t1 - t0) / 1_000_000.0;
        double mb = bytes / 1024.0 / 1024.0;
        double speed = mb / ((t1 - t0) / 1_000_000_000.0);

        System.out.printf(Locale.US,
                "%-42s : %8.2f ms | %7.2f MB/s | mem = %6.1f KB%n",
                title, ms, speed, (memAfter - memBefore) / 1024.0);
    }

    private static long usedMem() {
        Runtime r = Runtime.getRuntime();
        return r.totalMemory() - r.freeMemory();
    }
}
