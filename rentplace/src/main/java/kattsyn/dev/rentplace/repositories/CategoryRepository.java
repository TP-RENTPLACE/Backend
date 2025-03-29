package kattsyn.dev.rentplace.repositories;

import kattsyn.dev.rentplace.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
