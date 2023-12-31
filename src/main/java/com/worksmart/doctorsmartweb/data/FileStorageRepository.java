package com.worksmart.doctorsmartweb.data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

@Repository
public class FileStorageRepository {
    @Value("${STORAGE_FOLDER}")
    private String storageFolder;
    public void save(String originalFilename, InputStream inputStream){
        try {
            Path filePath = Path.of(storageFolder).resolve(originalFilename).normalize();
            Files.copy(inputStream, filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Resource findByName(String filename){
        try {
            Path filePath = Path.of(storageFolder).resolve(filename).normalize();
            return new UrlResource(filePath.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteAllByName(Iterable<String> filenames) {
        try {
            for (String filename : filenames) {
                Path filePath = Path.of(storageFolder).resolve(filename).normalize();
                Files.deleteIfExists(filePath);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
