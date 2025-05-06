package kattsyn.dev.rentplace.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import kattsyn.dev.rentplace.enums.PropertyStatus;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "property_status")
    @Schema(description = """
            Статус жилья.
            PUBLISHED (опубликовано) - объявление доступно всем пользователям.
            ON MODERATION (на модерации) - объявление на проверке у модерации. Видно только во вкладке "Сдать жилье" у владельца.
            REJECTED (отклонено) - объявление не прошло модерацию, его нужно отредактировать, чтобы попробовать еще раз отправить на модерацию.
            NOT PUBLISHED (не опубликовано) - объявление уже проходило модерацию и было опубликовано, но пользователь скрыл его.
            Если не будет внесено изменений, то можно спокойно публиковать. В случае внесения изменений нужно заново пройти модерацию.
            """)
    private PropertyStatus propertyStatus;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "address", nullable = false)
    @Schema(description = "Адрес имущества", example = "Россия, Воронеж, ул. Новосибирская, д.21")
    private String address;

    @Column(name = "description", length = 2000)
    @Schema(description = "Описание имущества", example = "Уютная квартира с видом на водохранилище")
    private String description;

    @Column(name = "is_long_term_rent", nullable = false)
    @Schema(description = "Является ли долгосрочной арендой. True - долгосрочная аренда (месяц и более). False - по дням.")
    private boolean isLongTermRent;

    @Column(name = "cost", nullable = false)
    @Schema(description = "Стоимость жилья. Если isLongTermRent true, то цена за месяц, иначе за сутки", example = "3500")
    private int cost;

    @Schema(description = "Сдаваемая площадь", example = "34.2")
    @Column(name = "area")
    private float area;

    @Schema(description = "Количество комнат", example = "4")
    @Column(name = "rooms")
    private int rooms;

    @Schema(description = "Количество спален", example = "3")
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

    @Schema(description = "Владелец жилья")
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "owner_id", referencedColumnName = "user_id")
    private User owner;

    @Schema(description = "Фотографии жилья")
    @OneToMany(fetch=FetchType.EAGER)
    @JoinTable(name = "properties_images",
            joinColumns = @JoinColumn(name = "property_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id")
    )
    private Set<Image> images = new HashSet<>();

    @Schema(description = "Категории жилья")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "properties_categories",
            joinColumns = @JoinColumn(name = "property_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();


    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
            name = "properties_facilities",
            joinColumns = @JoinColumn(name = "property_id"),
            inverseJoinColumns = @JoinColumn(name = "facility_id")
    )
    private Set<Facility> facilities = new HashSet<>();

}
