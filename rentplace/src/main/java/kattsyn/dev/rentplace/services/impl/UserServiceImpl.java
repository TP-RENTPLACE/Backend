package kattsyn.dev.rentplace.services.impl;

import jakarta.transaction.Transactional;
import kattsyn.dev.rentplace.dtos.*;
import kattsyn.dev.rentplace.entities.Image;
import kattsyn.dev.rentplace.entities.User;
import kattsyn.dev.rentplace.enums.ImageType;
import kattsyn.dev.rentplace.enums.Role;
import kattsyn.dev.rentplace.exceptions.ForbiddenException;
import kattsyn.dev.rentplace.exceptions.NotFoundException;
import kattsyn.dev.rentplace.mappers.UserMapper;
import kattsyn.dev.rentplace.repositories.UserRepository;
import kattsyn.dev.rentplace.services.ImageService;
import kattsyn.dev.rentplace.services.UserService;
import kattsyn.dev.rentplace.utils.PathResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ImageService imageService;

    @Transactional
    @Override
    public List<UserDTO> findAll() {
        return userMapper.fromUsers(userRepository.findAllWithRelations());
    }

    @Transactional
    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException(String.format("User with email %s not found", email))
        );
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public UserDTO getUserDTOByEmail(String email) {
        return userMapper.fromUser(userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(String.format("User with email %s not found", email))));
    }

    @Transactional
    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("User with id %s not found", id))
        );
    }

    @Transactional
    @Override
    public UserDTO findById(Long id) {
        return userMapper.fromUser(getUserById(id));
    }

    @Transactional
    @Override
    public UserDTO createWithImage(UserCreateEditDTO userCreateEditDTO) {
        User user = userMapper.fromUserCreateEditDTO(userCreateEditDTO);
        user = userRepository.save(user);
        if (userCreateEditDTO.getFile() != null && !userCreateEditDTO.getFile().isEmpty()) {
            return uploadImage(userCreateEditDTO.getFile(), user);
        }

        return userMapper.fromUser(user);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new RuntimeException(String.format("User with id %s not found", id))
        );
        userRepository.delete(user);
    }

    @Transactional
    @Override
    public ImageDTO uploadImage(MultipartFile file, long id) {
        //Хорошо было бы вынести куда-то эту логику всё-таки, возможно в imageService
        User user = userRepository.findById(id).orElseThrow(
                () -> new RuntimeException(String.format("User with id %s not found", id))
        );

        return uploadImage(file, user).getImageDTO();
    }

    @Transactional
    @Override
    public UserDTO updateUserByEmail(String email, UserCreateEditDTO userCreateEditDTO) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException(String.format("User with email %s not found", email))
        );

        return updateUser(user, userCreateEditDTO);
    }

    @Transactional
    @Override
    public UserDTO updateUserById(long id, UserCreateEditDTO userCreateEditDTO) {
        User user = getUserById(id);

        return updateUser(user, userCreateEditDTO);
    }

    @Transactional
    @Override
    public UserDTO updateUser(User user, UserCreateEditDTO userCreateEditDTO) {

        User updatedUser = userMapper.fromUserCreateEditDTO(userCreateEditDTO);

        updatedUser.setUserId(user.getUserId());
        updatedUser.setRegistrationDate(user.getRegistrationDate());

        if (userCreateEditDTO.getFile() != null && !userCreateEditDTO.getFile().isEmpty()) {
            return uploadImage(userCreateEditDTO.getFile(), updatedUser);
        }

        return userMapper.fromUser(userRepository.save(updatedUser));
    }

    @Override
    @Transactional
    public boolean allowedToEditUser(long id, String email) {
        User user = getUserByEmail(email);

        if (user.getRole() == Role.ROLE_ADMIN || id == user.getUserId()) {
            return true;
        }
        throw new ForbiddenException(String.format("FORBIDDEN. You are not allowed to edit user email: %s.", email));
    }


    @Override
    public User createUserWithRegisterRequest(RegisterRequest registerRequest) {
        User user = userMapper.fromRegisterRequest(registerRequest);
        user.setRegistrationDate(LocalDate.now());
        user.setRole(Role.ROLE_USER);

        return userRepository.save(user);
    }

    private UserDTO uploadImage(MultipartFile file, User user) {
        String path = PathResolver.resolvePath(ImageType.USER, user.getUserId());

        if (user.getImage() != null) {
            imageService.deleteImage(user.getImage().getImageId());
        }
        Image image = imageService.uploadImage(file, path);
        user.setImage(image);


        return userMapper.fromUser(userRepository.save(user));
    }

}
