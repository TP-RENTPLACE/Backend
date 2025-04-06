package kattsyn.dev.rentplace.services.impl;

import jakarta.transaction.Transactional;
import kattsyn.dev.rentplace.dtos.CategoryDTO;
import kattsyn.dev.rentplace.entities.Category;
import kattsyn.dev.rentplace.entities.Image;
import kattsyn.dev.rentplace.enums.ImageType;
import kattsyn.dev.rentplace.mappers.CategoryMapper;
import kattsyn.dev.rentplace.repositories.CategoryRepository;
import kattsyn.dev.rentplace.services.CategoryService;
import kattsyn.dev.rentplace.services.ImageService;
import kattsyn.dev.rentplace.utils.PathResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ImageService imageService;

    @Transactional
    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Transactional
    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Category id: %d not found", id)));
    }

    @Transactional
    @Override
    public Category save(CategoryDTO categoryDTO) {
        return categoryRepository.save(categoryMapper.fromCategoryDTO(categoryDTO));
    }

    @Transactional
    @Override
    public Category update(Long id, CategoryDTO categoryDTO) {
        Category category = categoryMapper.fromCategoryDTO(categoryDTO);
        category.setCategoryId(id);
        return categoryRepository.save(category);
    }

    @Transactional
    @Override
    public Category deleteById(Long id) {
        Category categoryForDeletion = findById(id);
        categoryRepository.delete(categoryForDeletion);
        return categoryForDeletion;
    }

    @Transactional
    @Override
    public Image uploadImage(MultipartFile file, long id) {
        Category category = findById(id);
        String path = PathResolver.resolvePath(ImageType.CATEGORY, id);

        if (category.getImage() != null) {
            imageService.deleteImage(category.getImage().getImageId());
        }
        Image image = imageService.uploadImage(file, path);
        category.setImage(image);
        categoryRepository.save(category);

        return image;
    }
}
