package kattsyn.dev.rentplace.mappers;

import kattsyn.dev.rentplace.dtos.facilities.FacilityCreateEditDTO;
import kattsyn.dev.rentplace.dtos.facilities.FacilityDTO;
import kattsyn.dev.rentplace.entities.Facility;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {ImageMapper.class}, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface FacilityMapper {

    Facility fromFacilityCreateEditDTO(FacilityCreateEditDTO facilityCreateEditDTO);
    @Mapping(target = "imageDTO", source = "image")
    FacilityDTO fromFacility(Facility facility);
    List<FacilityDTO> fromFacilities(List<Facility> facilities);

}
