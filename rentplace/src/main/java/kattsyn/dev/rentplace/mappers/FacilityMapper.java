package kattsyn.dev.rentplace.mappers;

import kattsyn.dev.rentplace.dtos.FacilityDTO;
import kattsyn.dev.rentplace.entities.Facility;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FacilityMapper {

    Facility fromFacilityDTO(FacilityDTO facilityDTO);

}
