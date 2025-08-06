package foomaa.test.zoo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/zoo/animals")
public class AnimalsZooRestController {
    private final AnimalsRepository animalsRepo;

    @Autowired
    AnimalsZooRestController(AnimalsRepository newAnimalsRepository) {
        animalsRepo = newAnimalsRepository;
    }

    @GetMapping
    Iterable<Animals> getAnimals() {
        return animalsRepo.findAll();
    }

    @GetMapping("/{id}")
    Optional<Animals> getAnimalById(@PathVariable Integer id) {
        return animalsRepo.findById(id);
    }

    @PostMapping
    Animals postAnimal(@RequestBody Animals animal) {
        return animalsRepo.save(animal);
    }

    @PutMapping("/{id}")
    ResponseEntity<Animals> putAnimal(@PathVariable Integer id, @RequestBody Animals animal) {
        return animalsRepo.existsById(id) ?
                new ResponseEntity<Animals>(animalsRepo.save(animal), HttpStatus.OK) :
                new ResponseEntity<Animals>(animalsRepo.save(animal), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    void deleteAnimal(@PathVariable Integer id) {
        animalsRepo.deleteById(id);
    }
}
