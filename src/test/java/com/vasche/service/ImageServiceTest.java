package com.vasche.service;

import org.junit.jupiter.api.Test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class ImageServiceTest {
    ImageService imageService = new ImageService();

    @Test
    void getIfImageExists() throws IOException {
        Optional<InputStream> inputStream = imageService.get("Безымянный.png");
        assertThat(inputStream).isPresent();
    }

    @Test
    void getIfImageDoesNotExist() throws IOException {
        Optional<InputStream> inputStream = imageService.get("Безымянный");
        assertThat(inputStream).isEmpty();
    }

    @Test
    void upload() throws IOException {
        String imagePath = "C:/Users/Vadim/Desktop/HarryPotter.jpg";
        String imageName = "HarryPotter.jpg";
        InputStream stream = new BufferedInputStream(new FileInputStream(imagePath));
        imageService.upload(imageName, stream);

        Optional<InputStream> inputStream = imageService.get(imageName);
        assertThat(inputStream).isPresent();
    }
}
