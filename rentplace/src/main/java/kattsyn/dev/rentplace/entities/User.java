package kattsyn.dev.rentplace.entities;

import jakarta.persistence.*;
import kattsyn.dev.rentplace.enums.Gender;
import kattsyn.dev.rentplace.enums.Role;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long userId;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;
    @Column(name = "birth_date")
    private LocalDate birthDate;
    @Column(name = "registration_date")
    private LocalDate registrationDate;
    @Column(name = "email")
    private String email;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "image_id")
    private Image image;

    @OneToMany(mappedBy = "renter")
    private List<Reservation> reservations;
}
