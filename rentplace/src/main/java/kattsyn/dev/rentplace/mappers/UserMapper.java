package kattsyn.dev.rentplace.mappers;

import kattsyn.dev.rentplace.dtos.ImageDTO;
import kattsyn.dev.rentplace.dtos.UserDTO;
import kattsyn.dev.rentplace.entities.Image;
import kattsyn.dev.rentplace.entities.User;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {ImageMapper.class})
public interface UserMapper {


    User fromDTO(UserDTO userDTO);
    @Mapping(target = "imageDTO", source = "image")
    UserDTO fromUser(User user);
    List<UserDTO> fromUsers(List<User> users);

    default ImageDTO fromImage(Image image, @Context ImageMapper imageMapper) {
        return imageMapper.fromImage(image);
    }

}
