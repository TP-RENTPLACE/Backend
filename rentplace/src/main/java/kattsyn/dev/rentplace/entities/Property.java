package kattsyn.dev.rentplace.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "properties")
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "property_id", nullable = false)
    private long propertyId;
    @Column(name = "address", nullable = false)
    private String address;
    @Column(name = "description")
    private String description;
    @Column(name = "rating", nullable = false)
    private float rating;
    @Column(name = "cost_per_day", nullable = false)
    private int costPerDay;
    @Column(name = "area")
    private float area;
    @Column(name = "bedrooms")
    private int bedrooms;
    @Column(name = "sleeping_places")
    private int sleepingPlaces;
    @Column(name = "bathrooms")
    private int bathrooms;
    @Column(name = "max_guests")
    private int maxGuests;

}
