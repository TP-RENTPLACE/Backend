package kattsyn.dev.rentplace.services;

import kattsyn.dev.rentplace.dtos.images.ImageDTO;
import kattsyn.dev.rentplace.dtos.properties.PropertyCreateEditDTO;
import kattsyn.dev.rentplace.dtos.properties.PropertyDTO;
import kattsyn.dev.rentplace.dtos.filters.PropertyFilterDTO;
import kattsyn.dev.rentplace.entities.Property;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PropertyService {

    boolean ownsPropertyOrAdmin(long propertyId, String email);

    boolean allowedToCreatePropertyOrAdmin(PropertyCreateEditDTO propertyCreateEditDTO, String email);

    List<PropertyDTO> findAll();

    List<PropertyDTO> findAllByOwnerEmail(String email);

    List<PropertyDTO> findAllByFilter(PropertyFilterDTO filter);

    PropertyDTO findById(long id);

    Property getPropertyById(long id);

    PropertyDTO createWithImages(PropertyCreateEditDTO propertyCreateEditDTO);

    PropertyDTO update(long id, PropertyCreateEditDTO propertyCreateEditDTO);

    void deleteById(long id);

    List<ImageDTO> uploadImages(MultipartFile[] files, long id);
}
