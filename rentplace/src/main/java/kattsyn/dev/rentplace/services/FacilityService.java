package kattsyn.dev.rentplace.services;

import kattsyn.dev.rentplace.dtos.FacilityCreateEditDTO;
import kattsyn.dev.rentplace.dtos.FacilityDTO;
import kattsyn.dev.rentplace.dtos.ImageDTO;
import kattsyn.dev.rentplace.entities.Facility;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FacilityService {

    List<FacilityDTO> findAll();

    FacilityDTO findById(Long id);

    Facility getFacilityById(Long id);

    FacilityDTO createWithImage(FacilityCreateEditDTO facilityCreateEditDTO);

    FacilityDTO update(FacilityCreateEditDTO facilityCreateEditDTO, long facilityId);

    FacilityDTO deleteById(Long id);

    ImageDTO uploadImage(MultipartFile file, long id);
}
