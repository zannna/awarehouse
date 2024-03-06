package com.example.awarehouse.module.storage;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    void store(MultipartFile file, String newFileName);
}
