package kattsyn.dev.rentplace.services.impl;

import kattsyn.dev.rentplace.properties.StorageProperties;
import kattsyn.dev.rentplace.services.StorageService;
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

@Service
@EnableConfigurationProperties(StorageProperties.class)
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;
    private final Path propertiesImagesLocation;
    //todo: нормально расширить распределение по путям и типам фотографий (недвижимости, удобства, категории, пользователи)

    public FileSystemStorageService(StorageProperties storageProperties) {
        this.rootLocation = Paths.get(storageProperties.getLocation()).toAbsolutePath().normalize();
        this.propertiesImagesLocation = Paths.get(storageProperties.getLocation(), "propertiesImages").toAbsolutePath().normalize();
        init();
    }

    private void init() {
        try {
            Files.createDirectories(rootLocation);
            Files.createDirectories(propertiesImagesLocation);
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
            Files.copy(file.getInputStream(), this.propertiesImagesLocation.resolve(filename));
            return filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + filename, e);
        }
    }



    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
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
    public void delete(String filename) {
        try {
            Files.deleteIfExists(rootLocation.resolve(filename));
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file " + filename, e);
        }
    }
}
