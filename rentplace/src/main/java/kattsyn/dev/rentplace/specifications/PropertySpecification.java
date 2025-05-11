package kattsyn.dev.rentplace.specifications;

import jakarta.persistence.criteria.*;
import kattsyn.dev.rentplace.dtos.filters.PropertyFilterDTO;
import kattsyn.dev.rentplace.entities.Category;
import kattsyn.dev.rentplace.entities.Facility;
import kattsyn.dev.rentplace.entities.Property;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@AllArgsConstructor
public class PropertySpecification implements Specification<Property> {

    private final PropertyFilterDTO filter;

    @Override
    public Predicate toPredicate(Root<Property> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        Predicate predicate = criteriaBuilder.conjunction();

        root.fetch("images", JoinType.LEFT);
        root.fetch("facilities", JoinType.LEFT);
        root.fetch("categories", JoinType.LEFT);

        assert query != null;
        query.distinct(true);

        Join<Property, Category> categoryJoin = null;
        if (filter.getCategoryIds() != null && !filter.getCategoryIds().isEmpty()) {
            categoryJoin = root.join("categories", JoinType.INNER);
        }

        Join<Property, Facility> facilityJoin = null;
        if (filter.getFacilityIds() != null && !filter.getFacilityIds().isEmpty()) {
            facilityJoin = root.join("facilities", JoinType.INNER);
        }

        if (filter.getMinPrice() != null && filter.getMinPrice() >= 0) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("cost"), filter.getMinPrice()));
        }

        if (filter.getMaxPrice() != null && filter.getMaxPrice() >= 0) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get("cost"), filter.getMaxPrice()));
        }

        if (filter.getGuestsAmount() != null && !(filter.getGuestsAmount() == 0)) {
            predicate = filter.getGuestsAmount() >= 5
                    ? criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("maxGuests"), filter.getGuestsAmount()))
                    : criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("maxGuests"), filter.getGuestsAmount()));
        }

        if (filter.getBedsAmount() != null && !(filter.getBedsAmount() == 0)) {
            predicate = filter.getBedsAmount() >= 5
                    ? criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("sleepingPlaces"), filter.getBedsAmount()))
                    : criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("sleepingPlaces"), filter.getBedsAmount()));
        }

        if (filter.getBedrooms() != null && !(filter.getBedrooms() == 0)) {
            predicate = filter.getBedrooms() >= 5
                    ? criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("bedrooms"), filter.getBedrooms()))
                    : criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("bedrooms"), filter.getBedrooms()));
        }

        if (filter.getRooms() != null && !(filter.getRooms() == 0)) {
            predicate = filter.getRooms() >= 5
                    ? criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get("rooms"), filter.getRooms()))
                    : criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("rooms"), filter.getRooms()));
        }

        if (filter.getIsLongTermRent() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("isLongTermRent"), filter.getIsLongTermRent()));
        }

        if (filter.getCategoryIds() != null && !filter.getCategoryIds().isEmpty() && categoryJoin != null) {
            predicate = criteriaBuilder.and(
                    predicate,
                    categoryJoin.get("categoryId").in(filter.getCategoryIds())
            );
        }

        // Фильтр по удобствам
        if (filter.getFacilityIds() != null && !filter.getFacilityIds().isEmpty() && facilityJoin != null) {
            predicate = criteriaBuilder.and(
                    predicate,
                    facilityJoin.get("facilityId").in(filter.getFacilityIds())
            );
        }

        return predicate;
    }
}
