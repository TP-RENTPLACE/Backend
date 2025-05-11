package kattsyn.dev.rentplace.mappers;


import kattsyn.dev.rentplace.dtos.categories.CategoryCreateEditDTO;
import kattsyn.dev.rentplace.dtos.categories.CategoryDTO;
import kattsyn.dev.rentplace.entities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {ImageMapper.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryMapper {

    Category fromCategoryCreateEditDTO(CategoryCreateEditDTO categoryCreateEditDTO);
    @Mapping(target = "imageDTO", source = "image")
    CategoryDTO fromCategory(Category category);
    List<CategoryDTO> fromCategories(List<Category> categories);

}
