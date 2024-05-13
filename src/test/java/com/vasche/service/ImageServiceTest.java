package com.vasche.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
        String imageName = "testImage.jpg";
        InputStream stream = getClass().getClassLoader().getResourceAsStream(imageName);
        if (stream != null) {
            imageService.upload(imageName, stream);

            Optional<InputStream> inputStream = imageService.get(imageName);
            assertThat(inputStream).isPresent();
        } else {
            Assertions.fail();
        }

    }
}
