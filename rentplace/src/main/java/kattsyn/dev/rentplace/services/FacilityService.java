package kattsyn.dev.rentplace.services;

import kattsyn.dev.rentplace.dtos.FacilityDTO;
import kattsyn.dev.rentplace.entities.Facility;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FacilityService {

    List<Facility> findAll();
    Facility findById(Long id);
    Facility save(FacilityDTO facilityDTODTO);
    Facility update(Long id, FacilityDTO facilityDTO);
    Facility deleteById(Long id);

    void uploadImage(MultipartFile file, long id);
}
