package kattsyn.dev.rentplace.dtos.filters;

import jakarta.validation.constraints.Min;
import kattsyn.dev.rentplace.enums.SortType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PropertyFilterDTO {

    private SortType sortType;
    private List<Long> categoryIds;
    private List<Long> facilityIds;
    private String address;
    private Boolean isLongTermRent;
    @Min(0)
    private Integer minPrice;
    @Min(0)
    private Integer maxPrice;
    @Min(0)
    private Integer guestsAmount;
    @Min(0)
    private Integer bedsAmount;
    @Min(0)
    private Integer bedrooms;
    @Min(0)
    private Integer rooms;

}
