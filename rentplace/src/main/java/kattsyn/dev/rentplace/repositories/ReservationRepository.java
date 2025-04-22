package kattsyn.dev.rentplace.repositories;

import kattsyn.dev.rentplace.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
