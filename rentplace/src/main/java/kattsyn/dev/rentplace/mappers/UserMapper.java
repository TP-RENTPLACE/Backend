package kattsyn.dev.rentplace.mappers;

import kattsyn.dev.rentplace.dtos.requests.RegisterRequest;
import kattsyn.dev.rentplace.dtos.users.UserCreateEditDTO;
import kattsyn.dev.rentplace.dtos.users.UserDTO;
import kattsyn.dev.rentplace.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {ImageMapper.class})
public interface UserMapper {


    User fromRegisterRequest(RegisterRequest registerRequest);
    User fromDTO(UserDTO userDTO);
    @Mapping(target = "imageDTO", source = "image")
    UserDTO fromUser(User user);
    List<UserDTO> fromUsers(List<User> users);
    User fromUserCreateEditDTO(UserCreateEditDTO userCreateEditDTO);


}
