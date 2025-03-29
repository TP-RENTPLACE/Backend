package kattsyn.dev.rentplace.services.impl;

import kattsyn.dev.rentplace.dtos.CategoryDTO;
import kattsyn.dev.rentplace.entities.Category;
import kattsyn.dev.rentplace.mappers.CategoryMapper;
import kattsyn.dev.rentplace.repositories.CategoryRepository;
import kattsyn.dev.rentplace.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Category id: %d not found", id)));
    }

    @Override
    public Category save(CategoryDTO categoryDTO) {
        return categoryRepository.save(categoryMapper.fromCategoryDTO(categoryDTO));
    }

    @Override
    public Category update(Long id, CategoryDTO categoryDTO) {
        Category category = categoryMapper.fromCategoryDTO(categoryDTO);
        category.setCategoryId(id);
        return categoryRepository.save(category);
    }

    @Override
    public Category deleteById(Long id) {
        Category categoryForDeletion = findById(id);
        categoryRepository.delete(categoryForDeletion);
        return categoryForDeletion;
    }
}
