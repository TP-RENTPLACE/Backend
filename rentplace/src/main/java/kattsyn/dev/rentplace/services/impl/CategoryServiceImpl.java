package kattsyn.dev.rentplace.services.impl;

import jakarta.transaction.Transactional;
import kattsyn.dev.rentplace.dtos.CategoryCreateEditDTO;
import kattsyn.dev.rentplace.dtos.CategoryDTO;
import kattsyn.dev.rentplace.dtos.ImageDTO;
import kattsyn.dev.rentplace.entities.Category;
import kattsyn.dev.rentplace.entities.Image;
import kattsyn.dev.rentplace.enums.ImageType;
import kattsyn.dev.rentplace.exceptions.NotFoundException;
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
    public List<CategoryDTO> findAll() {
        return categoryMapper.fromCategories(categoryRepository.findAll());
    }

    @Transactional
    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Category id: %d not found", id)));
    }

    @Transactional
    @Override
    public CategoryDTO findById(Long id) {
        return categoryMapper.fromCategory(getCategoryById(id));
    }

    @Transactional
    @Override
    public CategoryDTO createWithImage(CategoryCreateEditDTO categoryCreateEditDTO) {
        Category category = categoryMapper.fromCategoryCreateEditDTO(categoryCreateEditDTO);
        category = categoryRepository.save(category);
        uploadImage(categoryCreateEditDTO.getFile(), category.getCategoryId());
        return findById(category.getCategoryId());
    }

    @Transactional
    @Override
    public CategoryDTO update(Long categoryId, CategoryCreateEditDTO categoryCreateEditDTO) {
        Category category = getCategoryById(categoryId);

        if (categoryCreateEditDTO.getName() != null) {
            category.setName(categoryCreateEditDTO.getName());
        }

        if (categoryCreateEditDTO.getFile() != null && !categoryCreateEditDTO.getFile().isEmpty()) {
            return uploadImage(categoryCreateEditDTO.getFile(), category);
        }

        return categoryMapper.fromCategory(categoryRepository.save(category));
    }

    @Transactional
    @Override
    public CategoryDTO deleteById(Long id) {
        Category categoryForDeletion = getCategoryById(id);
        categoryRepository.delete(categoryForDeletion);
        return categoryMapper.fromCategory(categoryForDeletion);
    }

    @Transactional
    @Override
    public ImageDTO uploadImage(MultipartFile file, long id) {
        Category category = getCategoryById(id);
        return uploadImage(file, category).getImageDTO();
    }

    private CategoryDTO uploadImage(MultipartFile file, Category category) {
        String path = PathResolver.resolvePath(ImageType.CATEGORY, category.getCategoryId());

        if (category.getImage() != null) {
            imageService.deleteImage(category.getImage().getImageId());
        }
        Image image = imageService.uploadImage(file, path);
        category.setImage(image);


        return categoryMapper.fromCategory(categoryRepository.save(category));
    }
}
