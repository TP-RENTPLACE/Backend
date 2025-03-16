package kattsyn.dev.rentplace.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PropertyDTO {

    private String address;
    private String description;
    private float rating;
    private int costPerDay;
    private float area;
    private int bedrooms;
    private int sleepingPlaces;
    private int bathrooms;
    private int maxGuests;

}
