package kattsyn.dev.rentplace.services;

import kattsyn.dev.rentplace.dtos.reservations.PropertyReservationDTO;
import kattsyn.dev.rentplace.dtos.reservations.ReservationCreateEditDTO;
import kattsyn.dev.rentplace.dtos.reservations.ReservationDTO;
import kattsyn.dev.rentplace.entities.Reservation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReservationService {

    boolean ownsReservationOrAdmin(long reservationId, String email);

    boolean allowedToCreateReservationOrAdmin(ReservationCreateEditDTO reservationCreateEditDTO, String email);

    Reservation getReservationById(long reservationId);

    ReservationDTO getReservationDTOById(long reservationId);

    ReservationDTO createReservation(ReservationCreateEditDTO reservationCreateEditDTO);

    ReservationDTO updateReservation(long reservationId, ReservationCreateEditDTO reservationCreateEditDTO);

    ReservationDTO deleteById(long reservationId);

    List<ReservationDTO> findAllReservations();

    List<ReservationDTO> findAllReservationsByRenterEmail(String email);

    List<PropertyReservationDTO> findAllByPropertyId(long propertyId);

}
