package kattsyn.dev.rentplace.services.impl;

import jakarta.transaction.Transactional;
import kattsyn.dev.rentplace.entities.Image;
import kattsyn.dev.rentplace.enums.ImageType;
import kattsyn.dev.rentplace.repositories.ImageRepository;
import kattsyn.dev.rentplace.services.ImageService;
import kattsyn.dev.rentplace.services.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final StorageService storageService;

    private void validateImage(MultipartFile file) {
        String contentType = file.getContentType();
        if (file.isEmpty()) {
            throw new RuntimeException("Файл пустой");
        }
        if (!"image/jpeg".equals(contentType) && !"image/png".equals(contentType)) {
            throw new RuntimeException("Поддерживаются только JPEG и PNG изображения");
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

        return imageRepository.save(image);
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

    @Transactional
    @Override
    public void deleteImage(long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        storageService.delete(image.getAdditionalPath(), image.getFileName());
        imageRepository.delete(image);
    }

}
