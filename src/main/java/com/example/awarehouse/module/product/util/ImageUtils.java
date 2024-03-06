package com.example.awarehouse.module.product.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public class ImageUtils {

    public static String encodeFileToBase64Binary(Path path) throws IOException {
        byte[] bytes = Files.readAllBytes(path);
        return Base64.getEncoder().encodeToString(bytes);
    }
}