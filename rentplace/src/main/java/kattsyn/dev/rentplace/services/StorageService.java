package kattsyn.dev.rentplace.services;


import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    String store(MultipartFile file);
    String store(MultipartFile file, String location);
    Resource loadAsResource(String filename, String location);
    void rollbackUpload(String relativePath, String fileName);
    void delete(String relativePath, String filename);
}
