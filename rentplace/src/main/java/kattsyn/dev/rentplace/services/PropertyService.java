package kattsyn.dev.rentplace.services;

import kattsyn.dev.rentplace.dtos.ImageDTO;
import kattsyn.dev.rentplace.dtos.PropertyCreateEditDTO;
import kattsyn.dev.rentplace.dtos.PropertyDTO;
import kattsyn.dev.rentplace.entities.Property;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PropertyService {

    List<PropertyDTO> findAll();

    PropertyDTO findById(long id);

    Property getPropertyById(long id);

    PropertyDTO createWithImages(PropertyCreateEditDTO propertyCreateEditDTO);

    PropertyDTO update(long id, PropertyCreateEditDTO propertyCreateEditDTO);

    void deleteById(long id);

    List<ImageDTO> uploadImages(MultipartFile[] files, long id);
}
