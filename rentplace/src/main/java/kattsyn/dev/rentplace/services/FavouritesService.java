package kattsyn.dev.rentplace.services;

import kattsyn.dev.rentplace.dtos.properties.PropertyDTO;

import java.util.List;

public interface FavouritesService {

    void addPropertyToFavourites(long propertyId, String userEmail);
    void removePropertyFromFavourites(long propertyId, String userEmail);
    List<PropertyDTO> getUserFavouritesByUserEmail(String userEmail);
}
