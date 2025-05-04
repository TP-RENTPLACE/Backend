package kattsyn.dev.rentplace.repositories;


import kattsyn.dev.rentplace.entities.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long> {

    @Query("""
        SELECT DISTINCT p
        FROM Property p
        LEFT JOIN FETCH p.categories
        LEFT JOIN FETCH p.facilities
        LEFT JOIN FETCH p.images
    """)
    List<Property> findAllWithRelations();

    List<Property> findAllByOwnerEmail(String email);

}
