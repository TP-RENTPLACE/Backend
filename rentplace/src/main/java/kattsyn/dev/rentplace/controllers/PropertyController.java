package kattsyn.dev.rentplace.controllers;

import kattsyn.dev.rentplace.dtos.PropertyDTO;
import kattsyn.dev.rentplace.entities.Property;
import kattsyn.dev.rentplace.services.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.path}/properties")
public class PropertyController {

    private final PropertyService propertyService;

    @GetMapping("/")
    public ResponseEntity<List<Property>> getProperties() {
        List<Property> properties = propertyService.findAll();
        return ResponseEntity.ok(properties);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Property> findById(@PathVariable long id) {
        Property property = propertyService.findById(id);
        return ResponseEntity.ok(property);
    }

    @PostMapping("/")
    public ResponseEntity<Property> createProperty(@RequestBody PropertyDTO propertyDTO) {
        return new ResponseEntity<>(propertyService.save(propertyDTO), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Property> updateProperty(@PathVariable long id, @RequestBody PropertyDTO propertyDTO) {
        return ResponseEntity.ok(propertyService.update(id, propertyDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Property> deleteProperty(@PathVariable long id) {
        return new ResponseEntity<>(propertyService.deleteById(id), HttpStatus.NO_CONTENT);
    }


}
