package kattsyn.dev.rentplace.services;

import kattsyn.dev.rentplace.services.impl.ReservationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import kattsyn.dev.rentplace.entities.Reservation;
import kattsyn.dev.rentplace.exceptions.ValidationException;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private Reservation reservation;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Test
    void countRentPrice_LongTerm_ReturnsCorrectMonths() {
        when(reservation.isLongTermRent()).thenReturn(true);
        when(reservation.getStartDate()).thenReturn(LocalDate.of(2026, 6, 1));
        when(reservation.getEndDate()).thenReturn(LocalDate.of(2026, 10, 1));
        when(reservation.getCostInPeriod()).thenReturn(100);

        int result = reservationService.countRentPrice(reservation);
        assertEquals(400, result); // 4 месяца по 100
    }

    @Test
    void countRentPrice_ShortTerm_ReturnsCorrectMonths() {
        when(reservation.isLongTermRent()).thenReturn(false);
        when(reservation.getStartDate()).thenReturn(LocalDate.of(2026, 4, 1));
        when(reservation.getEndDate()).thenReturn(LocalDate.of(2026, 4, 15));
        when(reservation.getCostInPeriod()).thenReturn(100);

        int result = reservationService.countRentPrice(reservation);
        assertEquals(1400, result); // 14 дней по 100
    }

    @Test
    void countRentPrice_LongTermCrossingYear_ReturnsCorrectMonths() {
        when(reservation.isLongTermRent()).thenReturn(true);
        when(reservation.getStartDate()).thenReturn(LocalDate.of(2026, 12, 1));
        when(reservation.getEndDate()).thenReturn(LocalDate.of(2027, 1, 1));
        when(reservation.getCostInPeriod()).thenReturn(100);

        int result = reservationService.countRentPrice(reservation);
        assertEquals(100, result); // 1 месяц
    }

    @Test
    void countRentPrice_ShortTermCrossingYear_ReturnsCorrectDays() {
        when(reservation.isLongTermRent()).thenReturn(false);
        when(reservation.getStartDate()).thenReturn(LocalDate.of(2026, 12, 31));
        when(reservation.getEndDate()).thenReturn(LocalDate.of(2027, 1, 2));
        when(reservation.getCostInPeriod()).thenReturn(50);

        int result = reservationService.countRentPrice(reservation);
        assertEquals(2 * 50, result); // 2 дня
    }

    @Test
    void countRentPrice_EndDateBeforeStartDate_ThrowsException() {
        when(reservation.getStartDate()).thenReturn(LocalDate.of(2024, 1, 10));
        when(reservation.getEndDate()).thenReturn(LocalDate.of(2024, 1, 5));

        assertThrows(ValidationException.class, () -> reservationService.countRentPrice(reservation));
    }

    @Test
    void countRentPrice_StartDateInPast_ThrowsException() {
        when(reservation.getStartDate()).thenReturn(LocalDate.now().minusDays(1));

        assertThrows(ValidationException.class, () -> reservationService.countRentPrice(reservation));
    }
}