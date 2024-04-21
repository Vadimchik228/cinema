package com.vasche.service;

import com.vasche.util.PropertiesUtil;
import com.vasche.util.constants.ApplicationProperties;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

public class ImageService {

    private static final String BASE_PATH = PropertiesUtil.get(ApplicationProperties.IMAGE_PATH_KEY);

    public ImageService() {
    }

    public Optional<InputStream> get(final String imagePath) throws IOException {
        final Path fullImagePath = Path.of(BASE_PATH, imagePath);
        return Files.exists(fullImagePath) ?
                Optional.of(Files.newInputStream(fullImagePath)) :
                Optional.empty();
    }

    public void upload(final String imagePath, final InputStream inputStream) throws IOException {
        final Path imageFullPath = Path.of(BASE_PATH, imagePath);
        try (inputStream) {
            Files.createDirectories(imageFullPath.getParent());
            Files.write(imageFullPath, inputStream.readAllBytes(), StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        }
    }
}
