package utils;

import exception.CreateFileException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CsvEditor {
    public static void clearFile(String filePath) {
        try (Writer fileWriter = new FileWriter(filePath, StandardCharsets.UTF_8, false);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("");
        } catch (IOException exception) {
            throw new RuntimeException("Что то пошло не так во время очистки файла");
        }
    }

    public static void removeFile(String filePath) {
        Path path = Paths.get(filePath);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new CreateFileException("Невозможно создать новый файл");
        }

    }
}
