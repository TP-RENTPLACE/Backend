package kattsyn.dev.rentplace.services.impl;

import jakarta.transaction.Transactional;
import kattsyn.dev.rentplace.dtos.PropertyDTO;
import kattsyn.dev.rentplace.entities.Image;
import kattsyn.dev.rentplace.entities.Property;
import kattsyn.dev.rentplace.enums.ImageType;
import kattsyn.dev.rentplace.mappers.PropertyMapper;
import kattsyn.dev.rentplace.repositories.PropertyRepository;
import kattsyn.dev.rentplace.services.ImageService;
import kattsyn.dev.rentplace.services.PropertyService;
import kattsyn.dev.rentplace.utils.PathResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final PropertyMapper propertyMapper;
    private final ImageService imageService;

    @Transactional
    @Override
    public List<Property> findAll() {
        return propertyRepository.findAll();
    }

    @Transactional
    @Override
    public Property findById(long id) {
        return propertyRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException(String.format("Property not found with id: %d", id))
        );
    }

    @Transactional
    @Override
    public Property save(PropertyDTO propertyDTO) {
        return propertyMapper.fromPropertyDto(propertyDTO);
    }

    @Transactional
    @Override
    public Property update(long id, PropertyDTO propertyDTO) {
        Property property = propertyMapper.fromPropertyDto(propertyDTO);
        property.setPropertyId(id);
        return propertyRepository.save(property);
    }

    @Transactional
    @Override
    public Property deleteById(long id) {
        Property propertyForDeletion = findById(id);
        propertyRepository.delete(propertyForDeletion);
        return propertyForDeletion;
    }

    @Transactional
    @Override
    public List<Image> uploadImages(MultipartFile[] files, long id) {
        Property property = findById(id);

        String path = PathResolver.resolvePath(ImageType.PROPERTY, id);
        List<Image> savedImages = new ArrayList<>();

        for (MultipartFile file : files) {
            Image image = imageService.uploadImage(file, path);
            property.getImages().add(image);
            savedImages.add(image);
        }
        return savedImages;
    }

}
