package kattsyn.dev.rentplace.services;

import kattsyn.dev.rentplace.services.impl.ReservationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import kattsyn.dev.rentplace.dtos.ReservationCreateEditDTO;
import kattsyn.dev.rentplace.entities.Reservation;
import kattsyn.dev.rentplace.entities.User;
import kattsyn.dev.rentplace.enums.Role;
import kattsyn.dev.rentplace.exceptions.ForbiddenException;
import kattsyn.dev.rentplace.repositories.ReservationRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplSecurityTest {

    @Mock
    private UserService userService;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationServiceImpl reservationService;


    @Test
    void ownsReservationOrAdmin_UserIsAdmin_ReturnsTrue() {
        User adminUser = new User();
        adminUser.setRole(Role.ROLE_ADMIN);

        Reservation reservation = new Reservation();
        reservation.setReservationId(1L);

        when(userService.getUserByEmail("admin@test.com")).thenReturn(adminUser);
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        assertTrue(reservationService.ownsReservationOrAdmin(1L, "admin@test.com"));
    }

    @Test
    void ownsReservationOrAdmin_UserIsOwner_ReturnsTrue() {
        User ownerUser = new User();
        ownerUser.setUserId(100L);
        ownerUser.setRole(Role.ROLE_USER);

        Reservation reservation = new Reservation();
        reservation.setReservationId(1L);
        reservation.setRenter(ownerUser);

        when(userService.getUserByEmail("owner@test.com")).thenReturn(ownerUser);
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        assertTrue(reservationService.ownsReservationOrAdmin(1L, "owner@test.com"));
    }

    @Test
    void ownsReservationOrAdmin_UserNotAdminNorOwner_ThrowsForbiddenException() {
        User randomUser = new User();
        randomUser.setUserId(999L);
        randomUser.setRole(Role.ROLE_USER);

        Reservation reservation = new Reservation();
        reservation.setReservationId(1L);
        reservation.setRenter(new User());

        when(userService.getUserByEmail("random@test.com")).thenReturn(randomUser);
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        ForbiddenException exception = assertThrows(
                ForbiddenException.class,
                () -> reservationService.ownsReservationOrAdmin(1L, "random@test.com")
        );
        assertTrue(exception.getMessage().contains("FORBIDDEN. You are not allowed"));
    }


    @Test
    void allowedToCreateReservationOrAdmin_UserIsAdmin_ReturnsTrue() {
        User adminUser = new User();
        adminUser.setRole(Role.ROLE_ADMIN);

        ReservationCreateEditDTO dto = new ReservationCreateEditDTO();
        dto.setRenterId(123L);

        when(userService.getUserByEmail("admin@test.com")).thenReturn(adminUser);

        assertTrue(reservationService.allowedToCreateReservationOrAdmin(dto, "admin@test.com"));
    }

    @Test
    void allowedToCreateReservationOrAdmin_UserIsRenter_ReturnsTrue() {
        User renterUser = new User();
        renterUser.setUserId(123L);
        renterUser.setRole(Role.ROLE_USER);

        ReservationCreateEditDTO dto = new ReservationCreateEditDTO();
        dto.setRenterId(123L);

        when(userService.getUserByEmail("renter@test.com")).thenReturn(renterUser);

        assertTrue(reservationService.allowedToCreateReservationOrAdmin(dto, "renter@test.com"));
    }

    @Test
    void allowedToCreateReservationOrAdmin_UserNotAllowed_ThrowsForbiddenException() {
        User user = new User();
        user.setUserId(456L);
        user.setRole(Role.ROLE_USER);

        ReservationCreateEditDTO dto = new ReservationCreateEditDTO();
        dto.setRenterId(123L); // Не совпадает с ID пользователя

        when(userService.getUserByEmail("user@test.com")).thenReturn(user);


        ForbiddenException exception = assertThrows(
                ForbiddenException.class,
                () -> reservationService.allowedToCreateReservationOrAdmin(dto, "user@test.com")
        );
        assertTrue(exception.getMessage().contains("FORBIDDEN. You are not allowed"));
    }
}
