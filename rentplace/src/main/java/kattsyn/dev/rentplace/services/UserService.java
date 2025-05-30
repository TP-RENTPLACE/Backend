package kattsyn.dev.rentplace.services;

import kattsyn.dev.rentplace.dtos.*;
import kattsyn.dev.rentplace.entities.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    List<UserDTO> findAll();

    User getUserByEmail(String email);

    boolean existsByEmail(String email);

    UserDTO getUserDTOByEmail(String email);

    UserDTO findById(Long id);

    User getUserById(Long id);

    UserDTO save(UserDTO userDTO);

    UserDTO createWithImage(UserCreateEditDTO userCreateEditDTO);

    void deleteById(long id);

    ImageDTO uploadImage(MultipartFile file, long id);

    UserDTO update(long id, UserCreateEditDTO userCreateEditDTO);

    boolean allowedToEditUser(long id, String email);

    User createUserWithRegisterRequest(RegisterRequest registerRequest);
}
