package kattsyn.dev.rentplace.mappers;


import kattsyn.dev.rentplace.dtos.PropertyCreateEditDTO;
import kattsyn.dev.rentplace.dtos.PropertyDTO;
import kattsyn.dev.rentplace.entities.Category;
import kattsyn.dev.rentplace.entities.Facility;
import kattsyn.dev.rentplace.entities.Property;
import kattsyn.dev.rentplace.entities.User;
import kattsyn.dev.rentplace.services.CategoryService;
import kattsyn.dev.rentplace.services.FacilityService;
import kattsyn.dev.rentplace.services.UserService;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {ImageMapper.class, FacilityMapper.class, CategoryMapper.class,
                UserMapper.class, UserService.class, FacilityService.class,
                CategoryService.class})
public interface PropertyMapper {

    @Mapping(target = "owner", source = "ownerId")
    @Mapping(target = "categories", source = "categoriesIds")
    @Mapping(target = "facilities", source = "facilitiesIds")
    Property fromPropertyCreateEditDTO(PropertyCreateEditDTO propertyCreateEditDTO);

    Property fromPropertyDto(PropertyDTO propertyDTO);

    @Mapping(target = "imagesDTOs", source = "images")
    @Mapping(target = "ownerDTO", source = "owner")
    @Mapping(target = "facilitiesDTOs", source = "facilities")
    @Mapping(target = "categoriesDTOs", source = "categories")
    PropertyDTO fromProperty(Property property);

    List<PropertyDTO> fromProperties(List<Property> properties);

    default User getUserById(Long userId, @Context UserService userService) {
        return userService.getUserById(userId);
    }

    default List<Facility> getFacilitiesByIds(Long[] facilityIds, @Context FacilityService facilityService) {
        return facilityService.getFacilitiesByIds(facilityIds);
    }

    default List<Category> getCategoriesByIds(Long[] categoryIds, @Context CategoryService categoryService) {
        return categoryService.getCategoriesByIds(categoryIds);
    }
}
