package kattsyn.dev.rentplace.services;

import kattsyn.dev.rentplace.dtos.categories.CategoryCreateEditDTO;
import kattsyn.dev.rentplace.dtos.categories.CategoryDTO;
import kattsyn.dev.rentplace.dtos.images.ImageDTO;
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

    List<Category> getCategoriesByIds(Long[] ids);

    ImageDTO uploadImage(MultipartFile file, long id);

}
