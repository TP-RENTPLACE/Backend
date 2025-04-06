package kattsyn.dev.rentplace.services;

import kattsyn.dev.rentplace.dtos.ImageDTO;
import kattsyn.dev.rentplace.dtos.UserDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    List<UserDTO> findAll();
    UserDTO findByEmail(String email);
    UserDTO findById(Long id);
    UserDTO save(UserDTO userDTO);
    void deleteById(long id);
    ImageDTO uploadImage(MultipartFile file, long id);
    UserDTO update(long id, UserDTO userDTO);
}
