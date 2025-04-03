package kattsyn.dev.rentplace.services;

import kattsyn.dev.rentplace.dtos.CategoryDTO;
import kattsyn.dev.rentplace.entities.Category;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {

    List<Category> findAll();
    Category findById(Long id);
    Category save(CategoryDTO categoryDTO);
    Category update(Long id, CategoryDTO categoryDTO);
    Category deleteById(Long id);

    void uploadImage(MultipartFile file, long id);

}
