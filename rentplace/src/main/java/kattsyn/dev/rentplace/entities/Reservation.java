package kattsyn.dev.rentplace.entities;

import jakarta.persistence.*;
import kattsyn.dev.rentplace.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reservations")
public class Reservation {

    @Column(name = "reservation_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reservationId;

    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

    @ManyToOne
    @JoinColumn(name = "renter_id")
    private User renter;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "is_long_term_rent")
    private boolean isLongTermRent;

    @Column(name = "cost_in_period")
    private int costInPeriod;

    @Column(name = "renter_commission")
    private int renterCommission;

    @Column(name = "owner_commission")
    private int ownerCommission;

    @Column(name = "total_cost")
    private int totalCost;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

}
