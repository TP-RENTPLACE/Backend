package kattsyn.dev.rentplace.repositories;


import kattsyn.dev.rentplace.entities.Property;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyRepository extends JpaRepository<Property, Long> {
}
