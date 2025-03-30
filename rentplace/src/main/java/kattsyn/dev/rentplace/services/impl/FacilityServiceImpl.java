package kattsyn.dev.rentplace.services.impl;

import jakarta.transaction.Transactional;
import kattsyn.dev.rentplace.dtos.FacilityDTO;
import kattsyn.dev.rentplace.entities.Facility;
import kattsyn.dev.rentplace.mappers.FacilityMapper;
import kattsyn.dev.rentplace.repositories.FacilityRepository;
import kattsyn.dev.rentplace.services.FacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacilityServiceImpl implements FacilityService {

    private final FacilityRepository facilityRepository;
    private final FacilityMapper facilityMapper;

    @Transactional
    @Override
    public List<Facility> findAll() {
        return facilityRepository.findAll();
    }

    @Transactional
    @Override
    public Facility findById(Long id) {
        return facilityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Facility id: %d not found", id)));
    }

    @Transactional
    @Override
    public Facility save(FacilityDTO facilityDTO) {
        return facilityRepository.save(facilityMapper.fromFacilityDTO(facilityDTO));
    }

    @Transactional
    @Override
    public Facility update(Long id, FacilityDTO facilityDTO) {
        Facility facility = facilityMapper.fromFacilityDTO(facilityDTO);
        facility.setFacilityId(id);
        return facilityRepository.save(facility);
    }

    @Transactional
    @Override
    public Facility deleteById(Long id) {
        Facility facilityForDeletion = findById(id);
        facilityRepository.delete(facilityForDeletion);
        return facilityForDeletion;
    }
}
