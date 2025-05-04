package kattsyn.dev.rentplace.repositories;

import kattsyn.dev.rentplace.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllByRenterEmail(String email);

}
