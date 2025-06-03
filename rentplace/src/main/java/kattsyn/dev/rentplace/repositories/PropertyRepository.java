package kattsyn.dev.rentplace.repositories;


import kattsyn.dev.rentplace.entities.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PropertyRepository extends JpaRepository<Property, Long>, JpaSpecificationExecutor<Property> {

    @Query("""
        SELECT DISTINCT p
        FROM Property p
        LEFT JOIN FETCH p.owner o
        LEFT JOIN FETCH p.categories c
        LEFT JOIN FETCH p.facilities f
        LEFT JOIN FETCH p.images
        LEFT JOIN FETCH o.image
        LEFT JOIN FETCH c.image
        LEFT JOIN FETCH f.image
    """)
    List<Property> findAllWithRelations();

    @Query("""
        SELECT DISTINCT p
        FROM Property p
        LEFT JOIN FETCH p.owner o
        LEFT JOIN FETCH p.categories c
        LEFT JOIN FETCH p.facilities f
        LEFT JOIN FETCH p.images
        LEFT JOIN FETCH o.image
        LEFT JOIN FETCH c.image
        LEFT JOIN FETCH f.image
        WHERE p.owner.email = :email
    """)
    List<Property> findAllByOwnerEmail(@Param("email") String email);

    @Query("""
        SELECT DISTINCT p
        FROM Property p
        LEFT JOIN FETCH p.owner o
        LEFT JOIN FETCH p.categories c
        LEFT JOIN FETCH p.facilities f
        LEFT JOIN FETCH p.images
        LEFT JOIN FETCH o.image
        LEFT JOIN FETCH c.image
        LEFT JOIN FETCH f.image
        WHERE p.propertyId = :id
    """)
    Optional<Property> findById(@Param("id") long id);

}
