package kattsyn.dev.rentplace.services.impl;

import kattsyn.dev.rentplace.properties.StorageProperties;
import kattsyn.dev.rentplace.services.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@Slf4j
@EnableConfigurationProperties(StorageProperties.class)
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;
    private final StorageProperties storageProperties;

    public FileSystemStorageService(StorageProperties storageProperties) {
        this.rootLocation = Paths.get(storageProperties.getLocation()).toAbsolutePath().normalize();
        init();
        this.storageProperties = storageProperties;
    }

    private void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not create directory", e);
        }
    }

    private void init(Path path) {
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new RuntimeException("Could not create directory", e);
        }
    }

    @Override
    public String store(MultipartFile file) {
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file " + filename);
            }
            Files.copy(file.getInputStream(), this.rootLocation.resolve(filename));
            return filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + filename, e);
        }
    }

    //при передаче в location "/properties/prop1/" создалась папка properties, в ней prop1 и там уже фотка
    @Override
    public String store(MultipartFile file, String relativePath) {
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path path = Paths.get(storageProperties.getLocation(), relativePath).toAbsolutePath().normalize();
        init(path);
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file " + filename);
            }
            Files.copy(file.getInputStream(), path.resolve(filename));
            return filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + filename, e);
        }
    }

    @Override
    public void rollbackUpload(String relativePath, String fileName) {
        try {
            Path filePath = Paths.get(storageProperties.getLocation(), relativePath).resolve(fileName).toAbsolutePath().normalize();
            Files.deleteIfExists(filePath);

            // Удаляем пустую директорию если она пуста
            Path dirPath = Paths.get(storageProperties.getLocation(), relativePath).toAbsolutePath().normalize();
            if (Files.isDirectory(dirPath)) {
                try (Stream<Path> files = Files.list(dirPath)) {
                    if (files.findAny().isEmpty()) {
                        Files.delete(dirPath);
                    }
                }
            }
        } catch (IOException e) {
            log.error("Failed to rollback file upload", e);
        }
    }


    public Resource loadAsResource(String filename, String location) {
        try {
            Path file = Paths.get(storageProperties.getLocation(), location).toAbsolutePath().normalize().resolve(filename);

            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not read file: " + filename, e);
        }
    }


    @Override
    public void delete(String relativePath, String filename) {
        try {
            Files.deleteIfExists(Paths.get(storageProperties.getLocation(), relativePath).toAbsolutePath().normalize().resolve(filename));
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file " + filename, e);
        }
    }
}
