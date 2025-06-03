package kattsyn.dev.rentplace.services;

import kattsyn.dev.rentplace.entities.Image;
import kattsyn.dev.rentplace.enums.ImageType;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    Image uploadImage(MultipartFile file);
    Image uploadImage(MultipartFile file, ImageType imageType);
    Image uploadImage(MultipartFile file, String relativePath);

    void deleteImage(long id);
    Image getImageById(long id);
    Resource getImageResource(Image image);
    Image save(Image image);

}
