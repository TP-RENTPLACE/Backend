package kattsyn.dev.rentplace.services.impl;

import jakarta.transaction.Transactional;
import kattsyn.dev.rentplace.dtos.PropertyDTO;
import kattsyn.dev.rentplace.entities.Property;
import kattsyn.dev.rentplace.entities.User;
import kattsyn.dev.rentplace.exceptions.NotFoundException;
import kattsyn.dev.rentplace.mappers.PropertyMapper;
import kattsyn.dev.rentplace.repositories.PropertyRepository;
import kattsyn.dev.rentplace.repositories.UserRepository;
import kattsyn.dev.rentplace.services.FavouritesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavouritesServiceImpl implements FavouritesService {

    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;
    private final PropertyMapper propertyMapper;

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException(String.format("User email %s NOT FOUND", email))
        );
    }

    private Property getPropertyById(long propertyId) {
        return propertyRepository.findById(propertyId).orElseThrow(
                () -> new NotFoundException(String.format("Property id %s NOT FOUND", propertyId))
        );
    }

    @Transactional
    @Override
    public void addPropertyToFavourites(long propertyId, String userEmail) {
        User user = getUserByEmail(userEmail);
        user.getFavourites().add(getPropertyById(propertyId));
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void removePropertyFromFavourites(long propertyId, String userEmail) {
        User user = getUserByEmail(userEmail);
        user.getFavourites().remove(getPropertyById(propertyId));
        userRepository.save(user);
    }

    @Transactional
    @Override
    public List<PropertyDTO> getUserFavouritesByUserEmail(String userEmail) {
        return propertyMapper.fromProperties(getUserByEmail(userEmail).getFavourites().stream().toList());
    }

}
