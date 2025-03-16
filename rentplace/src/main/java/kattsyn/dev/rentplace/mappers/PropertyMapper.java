package kattsyn.dev.rentplace.mappers;


import kattsyn.dev.rentplace.dtos.PropertyDTO;
import kattsyn.dev.rentplace.entities.Property;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PropertyMapper {

    Property fromPropertyDto(PropertyDTO propertyDTO);
    PropertyDTO toPropertyDto(Property property);

}
