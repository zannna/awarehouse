package com.example.awarehouse.module.storage;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;

public class FileSystemStorageService implements StorageService {
    public static final Path rootLocation = Paths.get("C:\\Users\\DELL\\OneDrive\\Pulpit\\photos");

    public void store(MultipartFile file, String newFileName) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }
            Path destinationFile = this.rootLocation.resolve(
                            Paths.get(newFileName))
                    .normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                throw new StorageException(
                        "Cannot store file outside current directory.");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }
    public void delete(String fileName) {
        try {
            Path file = rootLocation.resolve(fileName);
            boolean deleted = Files.deleteIfExists(file);
            if (!deleted) {
                throw new StorageException("Failed to delete non-existent file.");
            }
        } catch (IOException e) {
            throw new StorageException("Failed to delete file.", e);
        }
    }
}
