package kattsyn.dev.rentplace.repositories;

import kattsyn.dev.rentplace.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("""
            SELECT DISTINCT r from Reservation r
            LEFT JOIN FETCH r.property p
            LEFT JOIN FETCH r.renter
            LEFT JOIN FETCH p.images
            LEFT JOIN FETCH p.owner
            LEFT JOIN FETCH p.facilities
            LEFT JOIN FETCH p.categories
            """
    )
    List<Reservation> findAllWithRelations();

    @Query("""
            SELECT DISTINCT r from Reservation r
            LEFT JOIN FETCH r.property p
            LEFT JOIN FETCH r.renter
            LEFT JOIN FETCH p.images
            LEFT JOIN FETCH p.owner
            LEFT JOIN FETCH p.facilities
            LEFT JOIN FETCH p.categories
            WHERE r.renter.email = :email
            """
    )
    List<Reservation> findAllByRenterEmail(@Param("email") String email);

    @Query("""
            SELECT DISTINCT r from Reservation r
            LEFT JOIN FETCH r.property p
            LEFT JOIN FETCH r.renter
            LEFT JOIN FETCH p.images
            LEFT JOIN FETCH p.owner
            LEFT JOIN FETCH p.facilities
            LEFT JOIN FETCH p.categories
            WHERE r.reservationId = :reservationId
            """
    )
    Optional<Reservation> findById(@Param("reservationId") long reservationId);

}
