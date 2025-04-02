package kattsyn.dev.rentplace.services;

import kattsyn.dev.rentplace.entities.Image;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    Image uploadImage(MultipartFile file);
    void deleteImage(long id);
    Image getImageById(long id);
    Resource getImageResource(Image image);
}
