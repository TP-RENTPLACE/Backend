package kattsyn.dev.rentplace.services.impl;

import jakarta.transaction.Transactional;
import kattsyn.dev.rentplace.entities.Image;
import kattsyn.dev.rentplace.enums.ImageType;
import kattsyn.dev.rentplace.repositories.ImageRepository;
import kattsyn.dev.rentplace.services.ImageService;
import kattsyn.dev.rentplace.services.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final StorageService storageService;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;
    @Value("${api.path}")
    private String apiPath;

    private void validateImage(MultipartFile file) {
        String contentType = file.getContentType();
        if (file.isEmpty()) {
            throw new RuntimeException("Файл пустой");
        }
        log.info("Content-Type: " + contentType);
        if (!"image/jpeg".equals(contentType) && !"image/png".equals(contentType) && !"image/svg+xml".equals(contentType)) {
            throw new RuntimeException("Поддерживаются только JPEG, PNG и SVG изображения");
        }
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new RuntimeException("Файл слишком большой");
        }
    }

    @Transactional
    @Override
    public Image uploadImage(MultipartFile file) {
        validateImage(file);

        String filename = storageService.store(file);
        Image image = new Image();
        image.setFileName(filename);
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setAdditionalPath("");
        image.setUrl(generateImageUrl("", filename));

        return imageRepository.save(image);
    }

    private String generateImageUrl(String relativePath, String fileName) {
        return String.format("%s/%s/images%s%s", baseUrl, apiPath, relativePath, fileName);
    }

    @Override
    public Image uploadImage(MultipartFile file, ImageType type) {
        try {
            validateImage(file);

            String filename = storageService.store(file, type.additionalPath);

            Image image = new Image();
            image.setFileName(filename);
            image.setOriginalFileName(file.getOriginalFilename());
            image.setContentType(file.getContentType());
            image.setAdditionalPath(type.additionalPath);
            image.setSize(file.getSize());
            image.setUrl(generateImageUrl(type.additionalPath, filename));

            return imageRepository.save(image);
        } catch (Exception e) {
            storageService.rollbackUpload(type.additionalPath, file.getOriginalFilename());
            throw e;
        }
    }

    @Override
    public Image uploadImage(MultipartFile file, String relativePath) {
        try {
            validateImage(file);

            String filename = storageService.store(file, relativePath);

            Image image = new Image();
            image.setFileName(filename);
            image.setOriginalFileName(file.getOriginalFilename());
            image.setContentType(file.getContentType());
            image.setAdditionalPath(relativePath);
            image.setSize(file.getSize());
            image.setUrl(generateImageUrl(relativePath, filename));

            return imageRepository.save(image);
        } catch (Exception e) {
            storageService.rollbackUpload(relativePath, file.getOriginalFilename());
            throw e;
        }
    }

    @Transactional
    @Override
    public Image getImageById(long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));
    }

    @Transactional
    @Override
    public Resource getImageResource(Image image) {
        return storageService.loadAsResource(image.getFileName(), image.getAdditionalPath());
    }

    @Override
    public Image save(Image image) {
        return imageRepository.save(image);
    }

    @Transactional
    @Override
    public void deleteImage(long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        storageService.delete(image.getAdditionalPath(), image.getFileName());
        imageRepository.delete(image);
    }

}
