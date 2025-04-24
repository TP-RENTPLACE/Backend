package kattsyn.dev.rentplace.mappers;

import kattsyn.dev.rentplace.dtos.ReservationCreateEditDTO;
import kattsyn.dev.rentplace.dtos.ReservationDTO;
import kattsyn.dev.rentplace.entities.Property;
import kattsyn.dev.rentplace.entities.Reservation;
import kattsyn.dev.rentplace.entities.User;
import kattsyn.dev.rentplace.services.PropertyService;
import kattsyn.dev.rentplace.services.UserService;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {PropertyService.class, PropertyMapper.class,
                UserMapper.class, UserService.class})
public interface ReservationMapper {

    @Mapping(target = "propertyDTO", source = "property")
    @Mapping(target = "renterDTO", source = "renter")
    ReservationDTO fromReservation(Reservation reservation);

    @Mapping(target = "property", source = "propertyId")
    @Mapping(target = "renter", source = "renterId")
    Reservation fromReservationCreateEditDTO(ReservationCreateEditDTO reservationCreateEditDTO);

    List<ReservationDTO> fromReservations(List<Reservation> reservations);

    default User getRenterById(Long renterId, @Context UserService userService) {
        return userService.getUserById(renterId);
    }

    default Property getPropertyById(Long propertyId, @Context PropertyService propertyService) {
        return propertyService.getPropertyById(propertyId);
    }

}
