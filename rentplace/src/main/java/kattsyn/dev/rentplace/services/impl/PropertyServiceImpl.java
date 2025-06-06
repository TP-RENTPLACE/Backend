package kattsyn.dev.rentplace.services.impl;

import jakarta.transaction.Transactional;
import kattsyn.dev.rentplace.dtos.images.ImageDTO;
import kattsyn.dev.rentplace.dtos.properties.PropertyCreateEditDTO;
import kattsyn.dev.rentplace.dtos.properties.PropertyDTO;
import kattsyn.dev.rentplace.dtos.filters.PropertyFilterDTO;
import kattsyn.dev.rentplace.entities.Image;
import kattsyn.dev.rentplace.entities.Property;
import kattsyn.dev.rentplace.entities.User;
import kattsyn.dev.rentplace.enums.ImageType;
import kattsyn.dev.rentplace.enums.Role;
import kattsyn.dev.rentplace.enums.SortType;
import kattsyn.dev.rentplace.exceptions.ForbiddenException;
import kattsyn.dev.rentplace.exceptions.NotFoundException;
import kattsyn.dev.rentplace.mappers.ImageMapper;
import kattsyn.dev.rentplace.mappers.PropertyMapper;
import kattsyn.dev.rentplace.repositories.PropertyRepository;
import kattsyn.dev.rentplace.services.ImageService;
import kattsyn.dev.rentplace.services.PropertyService;
import kattsyn.dev.rentplace.services.UserService;
import kattsyn.dev.rentplace.specifications.PropertySpecification;
import kattsyn.dev.rentplace.utils.PathResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final PropertyMapper propertyMapper;
    private final ImageService imageService;
    private final ImageMapper imageMapper;
    private final UserService userService;

    @Override
    @Transactional
    public boolean ownsPropertyOrAdmin(long propertyId, String email) {
        User user = userService.getUserByEmail(email);
        Property property = getPropertyById(propertyId);

        if (user.getRole() == Role.ROLE_ADMIN || property.getOwner().getUserId() == user.getUserId()) {
            return true;
        }
        throw new ForbiddenException(String.format("FORBIDDEN. You are not allowed to edit or delete property id: %s.", propertyId));
    }

    @Override
    public boolean allowedToCreatePropertyOrAdmin(PropertyCreateEditDTO propertyCreateEditDTO, String email) {
        User user = userService.getUserByEmail(email);

        if (user.getRole() == Role.ROLE_ADMIN || user.getUserId() == propertyCreateEditDTO.getOwnerId()) {
            return true;
        }
        throw new ForbiddenException(String.format("FORBIDDEN. You are not allowed to create property for user email: %s.", email));
    }

    @Transactional
    @Override
    public List<PropertyDTO> findAll() {
        return returnWithSortedImages(propertyMapper.fromProperties(propertyRepository.findAllWithRelations()));
    }

    @Override
    public List<PropertyDTO> findAllByOwnerEmail(String email) {
        return returnWithSortedImages(propertyMapper.fromProperties(propertyRepository.findAllByOwnerEmail(email)));
    }

    @Override
    public List<PropertyDTO> findAllByFilter(PropertyFilterDTO filter) {
        return returnWithSortedImages(propertyMapper.fromProperties(
                propertyRepository.findAll(new PropertySpecification(filter), buildSort(filter.getSortType()))
        ));
    }

    private List<PropertyDTO> returnWithSortedImages(List<PropertyDTO> propertyDTOS) {
        propertyDTOS.forEach(propertyDTO
                -> propertyDTO
                .getImagesDTOs()
                .sort(Comparator
                        .comparing(ImageDTO::isPreviewImage).reversed().thenComparing(ImageDTO::getImageId)));
        return propertyDTOS;
    }

    private Sort buildSort(SortType sortType) {
        if (sortType == null) return Sort.unsorted();

        return switch (sortType) {
            case MOST_OLD -> Sort.by(Sort.Order.asc("propertyId"));
            case MOST_RECENT -> Sort.by(Sort.Order.desc("propertyId"));
            case MOST_EXPENSIVE -> Sort.by(Sort.Order.desc("cost"));
            case MOST_CHEAP -> Sort.by(Sort.Order.asc("cost"));
        };
    }

    @Override
    @Transactional
    public Property getPropertyById(long id) {
        return propertyRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Property not found with id: %d", id))
        );
    }

    @Transactional
    @Override
    public PropertyDTO findById(long id) {
        PropertyDTO propertyDTO = propertyMapper.fromProperty(getPropertyById(id));
        propertyDTO.getImagesDTOs().sort(Comparator.comparing(ImageDTO::isPreviewImage).reversed().thenComparing(ImageDTO::getImageId));
        return propertyDTO;
    }


    @Override
    public PropertyDTO createWithImages(PropertyCreateEditDTO propertyCreateEditDTO) {
        Property property = propertyMapper.fromPropertyCreateEditDTO(propertyCreateEditDTO);
        property = propertyRepository.save(property);

        if (propertyCreateEditDTO.getFiles() != null && propertyCreateEditDTO.getFiles().length != 0) {
            uploadImages(propertyCreateEditDTO.getFiles(), property);
        }

        return propertyMapper.fromProperty(propertyRepository.save(property));
    }

    @Transactional
    @Override
    public PropertyDTO update(long id, PropertyCreateEditDTO propertyCreateEditDTO) {
        Property property = getPropertyById(id);

        Property changedProperty = propertyMapper.fromPropertyCreateEditDTO(propertyCreateEditDTO);
        changedProperty.setPropertyId(property.getPropertyId());

        if (propertyCreateEditDTO.getFiles() != null && propertyCreateEditDTO.getFiles().length != 0) {
            property.setImages(new HashSet<>());
            uploadImages(propertyCreateEditDTO.getFiles(), property);
        }
        changedProperty.setImages(property.getImages());

        return propertyMapper.fromProperty(propertyRepository.save(changedProperty));
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        Property propertyForDeletion = getPropertyById(id);
        propertyRepository.delete(propertyForDeletion);
    }

    @Transactional
    @Override
    public List<ImageDTO> uploadImages(MultipartFile[] files, long id) {
        return uploadImages(files, getPropertyById(id));
    }


    public List<ImageDTO> uploadImages(MultipartFile[] files, Property property) {

        String path = PathResolver.resolvePath(ImageType.PROPERTY, property.getPropertyId());
        List<Image> savedImages = new ArrayList<>();

        for (int i = 0; i < files.length; i++) {
            Image image = imageService.uploadImage(files[i], path);

            if (i == 0 && property.getImages().isEmpty()) {
                image.setPreviewImage(true);
                imageService.save(image);
            }

            property.getImages().add(image);
            savedImages.add(image);
        }
        return imageMapper.fromImages(savedImages);
    }

    public ImageDTO uploadImage(MultipartFile file, Property property) {

        String path = PathResolver.resolvePath(ImageType.PROPERTY, property.getPropertyId());
        Image savedImage;

        Image image = imageService.uploadImage(file, path);
        property.getImages().add(image);
        savedImage = image;

        return imageMapper.fromImage(savedImage);
    }

}
