package kattsyn.dev.rentplace.repositories;

import kattsyn.dev.rentplace.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
