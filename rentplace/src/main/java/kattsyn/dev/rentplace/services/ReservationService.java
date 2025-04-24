package kattsyn.dev.rentplace.services;

import kattsyn.dev.rentplace.dtos.ReservationCreateEditDTO;
import kattsyn.dev.rentplace.dtos.ReservationDTO;
import kattsyn.dev.rentplace.entities.Reservation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReservationService {

    Reservation getReservationById(long reservationId);

    ReservationDTO getReservationDTOById(long reservationId);

    ReservationDTO createReservation(ReservationCreateEditDTO reservationCreateEditDTO);

    ReservationDTO updateReservation(long reservationId, ReservationCreateEditDTO reservationCreateEditDTO);

    ReservationDTO deleteById(long reservationId);

    List<ReservationDTO> findAllReservations();

}
