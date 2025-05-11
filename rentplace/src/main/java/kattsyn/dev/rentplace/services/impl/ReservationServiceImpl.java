package kattsyn.dev.rentplace.services.impl;

import jakarta.transaction.Transactional;
import kattsyn.dev.rentplace.dtos.reservations.ReservationCreateEditDTO;
import kattsyn.dev.rentplace.dtos.reservations.ReservationDTO;
import kattsyn.dev.rentplace.entities.Reservation;
import kattsyn.dev.rentplace.entities.User;
import kattsyn.dev.rentplace.enums.PaymentStatus;
import kattsyn.dev.rentplace.enums.Role;
import kattsyn.dev.rentplace.exceptions.ForbiddenException;
import kattsyn.dev.rentplace.exceptions.NotFoundException;
import kattsyn.dev.rentplace.exceptions.ValidationException;
import kattsyn.dev.rentplace.mappers.ReservationMapper;
import kattsyn.dev.rentplace.repositories.ReservationRepository;
import kattsyn.dev.rentplace.services.ReservationService;
import kattsyn.dev.rentplace.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReservationServiceImpl implements ReservationService {

    @Value("${commission.for-renter.in-percent}")
    private int commissionForRenterInPercent;
    @Value("${commission.for-owner.in-percent}")
    private int commissionForOwnerInPercent;

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final UserService userService;

    @Override
    @Transactional
    public boolean ownsReservationOrAdmin(long reservationId, String email) {
        User user = userService.getUserByEmail(email);
        Reservation reservation = getReservationById(reservationId);

        if (user.getRole() == Role.ROLE_ADMIN || reservation.getRenter().getUserId() == user.getUserId()) {
            return true;
        }
        throw new ForbiddenException(String.format("FORBIDDEN. You are not allowed to edit or delete reservation id: %s.", reservationId));
    }

    @Override
    @Transactional
    public boolean allowedToCreateReservationOrAdmin(ReservationCreateEditDTO reservationCreateEditDTO, String email) {
        User user = userService.getUserByEmail(email);

        if (user.getRole() == Role.ROLE_ADMIN || user.getUserId() == reservationCreateEditDTO.getRenterId()) {
            return true;
        }
        throw new ForbiddenException(String.format("FORBIDDEN. You are not allowed to rent for user email: %s.", email));
    }

    @Override
    @Transactional
    public List<ReservationDTO> findAllReservations() {
        return reservationMapper.fromReservations(reservationRepository.findAllWithRelations());
    }

    @Override
    @Transactional
    public List<ReservationDTO> findAllReservationsByRenterEmail(String email) {
        return reservationMapper.fromReservations(reservationRepository.findAllByRenterEmail(email));
    }

    @Override
    @Transactional
    public Reservation getReservationById(long reservationId) {
        return reservationRepository.findById(reservationId).orElseThrow(
                () -> new NotFoundException(String.format("Reservation not found with id: %d", reservationId))
        );
    }

    @Override
    @Transactional
    public ReservationDTO getReservationDTOById(long reservationId) {
        return reservationMapper.fromReservation(getReservationById(reservationId));
    }

    @Override
    @Transactional
    public ReservationDTO createReservation(ReservationCreateEditDTO reservationCreateEditDTO) {
        Reservation reservation = reservationMapper.fromReservationCreateEditDTO(reservationCreateEditDTO);
        setPrices(reservation);
        reservation.setPaymentStatus(PaymentStatus.NOT_PAID);

        return reservationMapper.fromReservation(reservationRepository.save(reservation));
    }

    @Override
    @Transactional
    public ReservationDTO updateReservation(long reservationId, ReservationCreateEditDTO reservationCreateEditDTO) {
        Reservation reservation = getReservationById(reservationId);
        Reservation updatedReservation = reservationMapper.fromReservationCreateEditDTO(reservationCreateEditDTO);
        setPrices(updatedReservation);
        updatedReservation.setPaymentStatus(reservation.getPaymentStatus());
        updatedReservation.setReservationId(reservation.getReservationId());
        return reservationMapper.fromReservation(reservationRepository.save(updatedReservation));
    }

    @Override
    @Transactional
    public ReservationDTO deleteById(long reservationId) {
        Reservation reservation = getReservationById(reservationId);
        reservationRepository.delete(reservation);
        return reservationMapper.fromReservation(reservation);
    }


    public int countRentPrice(Reservation reservation) {
        LocalDate startDate = reservation.getStartDate();
        LocalDate endDate = reservation.getEndDate();

        if (startDate.isBefore(LocalDate.now())) {
            throw new ValidationException("Reservations start date can't be before now.");
        }

        if (endDate.isBefore(startDate)) {
            throw new ValidationException("End date can't be before start date.");
        }

        int period;
        if (reservation.isLongTermRent()) {
            period = (int) ChronoUnit.MONTHS.between(startDate, endDate);
        } else {
            period = (int) ChronoUnit.DAYS.between(startDate, endDate);
        }

        return period * reservation.getCostInPeriod();
    }

    private void setPrices(Reservation reservation) {
        int rentPrice = countRentPrice(reservation);
        int renterCommission = rentPrice * commissionForRenterInPercent / 100;
        int ownerCommission = rentPrice * commissionForOwnerInPercent / 100;

        reservation.setRenterCommission(renterCommission);
        reservation.setOwnerCommission(ownerCommission);
        reservation.setTotalCost(rentPrice + renterCommission);
    }

}
