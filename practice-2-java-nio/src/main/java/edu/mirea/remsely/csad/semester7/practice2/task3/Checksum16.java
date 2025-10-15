package edu.mirea.remsely.csad.semester7.practice2.task3;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Checksum16 {
    private static final int BUFFER_SIZE = 64 * 1024;

    public static void main(String[] args) throws IOException {
        Path p = Path.of(
                "practice-2-java-nio/src/main/java/edu/mirea/remsely/csad/semester7/practice2/task3/file_to_checksum.txt"
        );
        int s1 = simpleByteSum16(p);
        System.out.printf("Simple byte sum : 0x%04X (%d)%n", s1, s1);
    }

    public static int simpleByteSum16(Path path) throws IOException {
        try (FileChannel ch = FileChannel.open(path, StandardOpenOption.READ)) {
            ByteBuffer buf = ByteBuffer.allocateDirect(BUFFER_SIZE);
            int checksum = 0;
            while (ch.read(buf) > 0) {
                buf.flip();
                while (buf.hasRemaining()) {
                    checksum += (buf.get() & 0xFF);
                    checksum &= 0xFFFF;
                }
                buf.clear();
            }
            return checksum;
        }
    }
}
