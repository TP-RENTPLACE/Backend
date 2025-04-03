package kattsyn.dev.rentplace.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
    @Schema(description = "Адрес имущества", example = "Россия, Воронеж, ул. Новосибирская, д.21")
    private String address;

    @Column(name = "description", length = 2000)
    @Schema(description = "Описание имущества", example = "Уютная квартира с видом на водохранилище")
    private String description;

    @Column(name = "rating", nullable = false)
    @Schema(description = "Рейтинг жилья", example = "4.41")
    private float rating;

    @Column(name = "cost_per_day", nullable = false)
    @Schema(description = "Стоимость жилья в сутки", example = "3500")
    private int costPerDay;

    @Schema(description = "Сдаваемая площадь", example = "34.2")
    @Column(name = "area")
    private float area;

    @Schema(description = "Количество спален", example = "4")
    @Column(name = "bedrooms")
    private int bedrooms;

    @Schema(description = "Количество спальных мест", example = "9")
    @Column(name = "sleeping_places")
    private int sleepingPlaces;

    @Schema(description = "Количество ванных комнат", example = "2")
    @Column(name = "bathrooms")
    private int bathrooms;

    @Schema(description = "Максимум гостей", example = "7")
    @Column(name = "max_guests")
    private int maxGuests;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "property_id")
    List<Image> images;

}
