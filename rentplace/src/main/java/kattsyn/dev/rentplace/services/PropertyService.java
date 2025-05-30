package kattsyn.dev.rentplace.services;

import kattsyn.dev.rentplace.dtos.ImageDTO;
import kattsyn.dev.rentplace.dtos.PropertyCreateEditDTO;
import kattsyn.dev.rentplace.dtos.PropertyDTO;
import kattsyn.dev.rentplace.entities.Property;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PropertyService {

    boolean ownsPropertyOrAdmin(long propertyId, String email);

    boolean allowedToCreatePropertyOrAdmin(PropertyCreateEditDTO propertyCreateEditDTO, String email);

    List<PropertyDTO> findAll();

    List<PropertyDTO> findAllByOwnerEmail(String email);

    PropertyDTO findById(long id);

    Property getPropertyById(long id);

    PropertyDTO createWithImages(PropertyCreateEditDTO propertyCreateEditDTO);

    PropertyDTO update(long id, PropertyCreateEditDTO propertyCreateEditDTO);

    void deleteById(long id);

    List<ImageDTO> uploadImages(MultipartFile[] files, long id);
}
