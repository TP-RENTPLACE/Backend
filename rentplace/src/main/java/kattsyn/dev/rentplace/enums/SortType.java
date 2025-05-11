package kattsyn.dev.rentplace.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Тип сортировки объявлений"
)
public enum SortType {

    @Schema(name = "Сначала новые")
    MOST_RECENT,
    @Schema(name = "Сначала старые")
    MOST_OLD,
    @Schema(name = "Сначала дорогие, убывание по цене")
    MOST_EXPENSIVE,
    @Schema(name = "Сначала дешевые, возрастание по цене")
    MOST_CHEAP

}
