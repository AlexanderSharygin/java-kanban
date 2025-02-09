package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

public class CsvEditor {
    public static void clearFile(String filePath) {
        try (Writer fileWriter = new FileWriter(filePath, StandardCharsets.UTF_8, false);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("");
        } catch (IOException exception) {
            throw new RuntimeException("Что то пошло не так во время очистки файла");
        }

    }
}
