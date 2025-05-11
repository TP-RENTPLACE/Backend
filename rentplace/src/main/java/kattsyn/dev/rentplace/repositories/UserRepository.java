package kattsyn.dev.rentplace.repositories;

import kattsyn.dev.rentplace.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    @Query("""
        SELECT DISTINCT u FROM User u
        LEFT JOIN FETCH u.image
""")
    List<User> findAllWithRelations();

    @Query("""
                    SELECT DISTINCT u FROM User u
                    LEFT JOIN FETCH u.image
                    WHERE u.userId = :userId
            """)
    Optional<User> findById(@Param("userId") long userId);

    @Query("""
                    SELECT DISTINCT u FROM User u
                    LEFT JOIN FETCH u.image
                    WHERE u.email = :email
            """)
    Optional<User> findByEmail(@Param("email") String email);
}
