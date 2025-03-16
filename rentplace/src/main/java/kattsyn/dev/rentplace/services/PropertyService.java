package kattsyn.dev.rentplace.services;

import kattsyn.dev.rentplace.dtos.PropertyDTO;
import kattsyn.dev.rentplace.entities.Property;

import java.util.List;

public interface PropertyService {

    List<Property> findAll();
    Property findById(long id);
    Property save(PropertyDTO propertyDTO);
    Property update(long id, PropertyDTO propertyDTO);
    Property deleteById(long id);

}
