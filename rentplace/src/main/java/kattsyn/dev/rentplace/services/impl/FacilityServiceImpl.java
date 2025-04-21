package kattsyn.dev.rentplace.services.impl;

import jakarta.transaction.Transactional;
import kattsyn.dev.rentplace.dtos.FacilityCreateEditDTO;
import kattsyn.dev.rentplace.dtos.FacilityDTO;
import kattsyn.dev.rentplace.dtos.ImageDTO;
import kattsyn.dev.rentplace.entities.Facility;
import kattsyn.dev.rentplace.entities.Image;
import kattsyn.dev.rentplace.enums.ImageType;
import kattsyn.dev.rentplace.mappers.FacilityMapper;
import kattsyn.dev.rentplace.mappers.ImageMapper;
import kattsyn.dev.rentplace.repositories.FacilityRepository;
import kattsyn.dev.rentplace.services.FacilityService;
import kattsyn.dev.rentplace.services.ImageService;
import kattsyn.dev.rentplace.utils.PathResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FacilityServiceImpl implements FacilityService {

    private final FacilityRepository facilityRepository;
    private final FacilityMapper facilityMapper;
    private final ImageService imageService;
    private final ImageMapper imageMapper;

    @Transactional
    @Override
    public List<FacilityDTO> findAll() {
        return facilityMapper.fromFacilities(facilityRepository.findAll());
    }

    @Transactional
    @Override
    public Facility getFacilityById(Long id) {
        return facilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Facility id: %d not found", id)));
    }

    @Transactional
    @Override
    public List<Facility> getFacilitiesByIds(Long[] ids) {
        List<Facility> facilities = new ArrayList<>();

        for(Long id : ids) {
            facilities.add(getFacilityById(id));
        }

        return facilities;
    }

    @Transactional
    @Override
    public FacilityDTO createWithImage(FacilityCreateEditDTO facilityDTO) {
        Facility facility = facilityMapper.fromFacilityCreateEditDTO(facilityDTO);
        facility = facilityRepository.save(facility);
        uploadImage(facilityDTO.getFile(), facility.getFacilityId());
        return findById(facility.getFacilityId());
    }

    @Transactional
    @Override
    public FacilityDTO findById(Long id) {
        return facilityMapper.fromFacility(getFacilityById(id));
    }


    @Transactional
    @Override
    public FacilityDTO update(FacilityCreateEditDTO facilityCreateEditDTO, long facilityId) {
        Facility facility = getFacilityById(facilityId);

        if (facilityCreateEditDTO.getName() != null) {
            facility.setName(facilityCreateEditDTO.getName());
        }

        if (facilityCreateEditDTO.getFile() != null && !facilityCreateEditDTO.getFile().isEmpty()) {
            String path = PathResolver.resolvePath(ImageType.FACILITY, facilityId);

            if (facility.getImage() != null) {
                imageService.deleteImage(facility.getImage().getImageId());
            }
            Image image = imageService.uploadImage(facilityCreateEditDTO.getFile(), path);
            facility.setImage(image);
        }
        return facilityMapper.fromFacility(facilityRepository.save(facility));
    }

    @Transactional
    @Override
    public FacilityDTO deleteById(Long id) {
        Facility facility = getFacilityById(id);
        facilityRepository.delete(facility);

        return facilityMapper.fromFacility(facility);
    }

    @Transactional
    @Override
    public ImageDTO uploadImage(MultipartFile file, long id) {
        Facility facility = getFacilityById(id);
        String path = PathResolver.resolvePath(ImageType.FACILITY, id);

        if (facility.getImage() != null) {
            imageService.deleteImage(facility.getImage().getImageId());
        }
        Image image = imageService.uploadImage(file, path);
        facility.setImage(image);
        facilityRepository.save(facility);

        return imageMapper.fromImage(image);
    }
}
