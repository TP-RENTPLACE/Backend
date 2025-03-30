package kattsyn.dev.rentplace.mappers;


import kattsyn.dev.rentplace.dtos.CategoryDTO;
import kattsyn.dev.rentplace.entities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {

    Category fromCategoryDTO(CategoryDTO categoryDTO);

}
