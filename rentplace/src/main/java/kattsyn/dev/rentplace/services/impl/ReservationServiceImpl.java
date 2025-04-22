package kattsyn.dev.rentplace.services.impl;

import kattsyn.dev.rentplace.dtos.ReservationCreateEditDTO;
import kattsyn.dev.rentplace.dtos.ReservationDTO;
import kattsyn.dev.rentplace.entities.Reservation;
import kattsyn.dev.rentplace.enums.PaymentStatus;
import kattsyn.dev.rentplace.exceptions.NotFoundException;
import kattsyn.dev.rentplace.mappers.ReservationMapper;
import kattsyn.dev.rentplace.repositories.ReservationRepository;
import kattsyn.dev.rentplace.services.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReservationServiceImpl implements ReservationService {

    @Value("${commission.for_renter.in_percent}")
    private int commissionForRenterInPercent;
    @Value("${commission.for_owner.in_percent}")
    private int commissionForOwnerInPercent;

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;

    @Override
    public List<ReservationDTO> findAllReservations() {
        return reservationMapper.fromReservations(reservationRepository.findAll());
    }

    @Override
    public Reservation getReservationById(long reservationId) {
        return reservationRepository.findById(reservationId).orElseThrow(
                () -> new NotFoundException(String.format("Reservation not found with id: %d", reservationId))
        );
    }

    @Override
    public ReservationDTO getReservationDTOById(long reservationId) {
        return reservationMapper.fromReservation(getReservationById(reservationId));
    }

    @Override
    public ReservationDTO createReservation(ReservationCreateEditDTO reservationCreateEditDTO) {
        Reservation reservation = reservationMapper.fromReservationCreateEditDTO(reservationCreateEditDTO);
        setPrices(reservation);
        reservation.setPaymentStatus(PaymentStatus.NOT_PAID);

        return reservationMapper.fromReservation(reservationRepository.save(reservation));
    }

    @Override
    public ReservationDTO updateReservation(long reservationId, ReservationCreateEditDTO reservationCreateEditDTO) {
        Reservation reservation = getReservationById(reservationId);
        Reservation updatedReservation = reservationMapper.fromReservationCreateEditDTO(reservationCreateEditDTO);
        setPrices(updatedReservation);
        updatedReservation.setPaymentStatus(reservation.getPaymentStatus());
        updatedReservation.setReservationId(reservation.getReservationId());
        return reservationMapper.fromReservation(reservationRepository.save(updatedReservation));
    }

    @Override
    public ReservationDTO deleteById(long reservationId) {
        Reservation reservation = getReservationById(reservationId);
        reservationRepository.delete(reservation);
        return reservationMapper.fromReservation(reservation);
    }

    private int countRentPrice(Reservation reservation) {
        int period;
        if (reservation.isLongTermRent()) {
            period = reservation.getEndDate().getMonthValue() - reservation.getStartDate().getMonthValue();
        } else {
            period = reservation.getEndDate().getDayOfYear() - reservation.getStartDate().getDayOfYear();
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
