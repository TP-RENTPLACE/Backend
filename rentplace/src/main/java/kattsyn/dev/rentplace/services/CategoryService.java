package kattsyn.dev.rentplace.services;

import kattsyn.dev.rentplace.dtos.CategoryCreateEditDTO;
import kattsyn.dev.rentplace.dtos.CategoryDTO;
import kattsyn.dev.rentplace.dtos.ImageDTO;
import kattsyn.dev.rentplace.entities.Category;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {

    List<CategoryDTO> findAll();
    CategoryDTO findById(Long id);
    CategoryDTO createWithImage(CategoryCreateEditDTO categoryCreateEditDTO);
    CategoryDTO update(Long id, CategoryCreateEditDTO categoryCreateEditDTO);
    CategoryDTO deleteById(Long id);
    Category getCategoryById(Long id);

    ImageDTO uploadImage(MultipartFile file, long id);

}
