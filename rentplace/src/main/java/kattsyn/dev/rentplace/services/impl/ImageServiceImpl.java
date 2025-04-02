package kattsyn.dev.rentplace.services.impl;

import jakarta.transaction.Transactional;
import kattsyn.dev.rentplace.entities.Image;
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

    @Transactional
    @Override
    public Image uploadImage(MultipartFile file) {
        //todo: добавить проверку на вес файла и contentType
        String filename = storageService.store(file);

        Image image = new Image();
        image.setFileName(filename);
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());

        return imageRepository.save(image);
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
        return storageService.loadAsResource(image.getFileName());
    }

    @Transactional
    @Override
    public void deleteImage(long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Image not found"));

        storageService.delete(image.getFileName());
        imageRepository.delete(image);
    }

}
