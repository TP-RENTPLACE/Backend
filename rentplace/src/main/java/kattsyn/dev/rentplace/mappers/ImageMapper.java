package kattsyn.dev.rentplace.mappers;

import kattsyn.dev.rentplace.dtos.ImageDTO;
import kattsyn.dev.rentplace.entities.Image;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ImageMapper {

    ImageDTO fromImage(Image image);
    List<ImageDTO> fromImages(List<Image> images);

}
