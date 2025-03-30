package kattsyn.dev.rentplace.repositories;

import kattsyn.dev.rentplace.entities.Facility;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacilityRepository extends JpaRepository<Facility, Long> {
}
