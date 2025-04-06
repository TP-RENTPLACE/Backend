package kattsyn.dev.rentplace.services.impl;

import jakarta.transaction.Transactional;
import kattsyn.dev.rentplace.dtos.ImageDTO;
import kattsyn.dev.rentplace.dtos.UserDTO;
import kattsyn.dev.rentplace.entities.Image;
import kattsyn.dev.rentplace.entities.User;
import kattsyn.dev.rentplace.enums.ImageType;
import kattsyn.dev.rentplace.mappers.ImageMapper;
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
    private final ImageMapper imageMapper;

    @Transactional
    @Override
    public List<UserDTO> findAll() {
        return userMapper.fromUsers(userRepository.findAll());
    }

    @Transactional
    @Override
    public UserDTO findByEmail(String email) {
        return userMapper.fromUser(userRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException(String.format("User with email %s not found", email))
        ));
    }

    @Transactional
    @Override
    public UserDTO findById(Long id) {
        return userMapper.fromUser(userRepository.findById(id).orElseThrow(
                () -> new RuntimeException(String.format("User with id %s not found", id))
        ));
    }

    @Transactional
    @Override
    public UserDTO save(UserDTO userDTO) {
        User user = userMapper.fromDTO(userDTO);
        user.setRegistrationDate(LocalDate.now());

        user = userRepository.save(user);
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
        String path = PathResolver.resolvePath(ImageType.USER, id);

        if (user.getImage() != null) {
            imageService.deleteImage(user.getImage().getImageId());
        }
        Image image = imageService.uploadImage(file, path);
        user.setImage(image);
        userRepository.save(user);

        return imageMapper.fromImage(image);
    }

    @Transactional
    @Override
    public UserDTO update(long id, UserDTO userDTO) {
        User user = userMapper.fromDTO(userDTO);
        user.setUserId(id);
        return userMapper.fromUser(userRepository.save(user));
    }
}
