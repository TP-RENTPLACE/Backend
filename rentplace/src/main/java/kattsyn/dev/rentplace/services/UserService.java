package kattsyn.dev.rentplace.services;

import kattsyn.dev.rentplace.dtos.images.ImageDTO;
import kattsyn.dev.rentplace.dtos.requests.RegisterRequest;
import kattsyn.dev.rentplace.dtos.users.UserCreateEditDTO;
import kattsyn.dev.rentplace.dtos.users.UserDTO;
import kattsyn.dev.rentplace.entities.User;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserDTO> findAll();

    User getUserByEmail(String email);

    Optional<User> getUserOptionalByEmail(String email);

    UserDTO getUserDTOByEmail(String email);

    UserDTO findById(Long id);

    User getUserById(Long id);

    UserDTO createWithImage(UserCreateEditDTO userCreateEditDTO);

    void deleteById(long id);

    void deleteMe(Authentication authentication);

    ImageDTO uploadImage(MultipartFile file, long id);

    UserDTO updateUserById(long id, UserCreateEditDTO userCreateEditDTO);

    UserDTO updateUser(User user, UserCreateEditDTO userCreateEditDTO);

    UserDTO updateUserByEmail(String email, UserCreateEditDTO userCreateEditDTO);

    boolean allowedToEditUser(long id, String email);

    User createUserWithRegisterRequest(RegisterRequest registerRequest);

    void blockUser(long userId);

    void activateUser(long userId);

    void deactivateUser(long userId);
}
