package foomaa.test.zoo;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@RestController
@RequestMapping("/zoos/animals")
public class AnimalsZooRestController {
    private final AnimalsRepository animalsRepo;
    private final ZoosRepository zoosRepo;

    @Autowired
    public AnimalsZooRestController(AnimalsRepository animalsRepo, ZoosRepository zoosRepo) {
        this.animalsRepo = animalsRepo;
        this.zoosRepo = zoosRepo;
    }

    @GetMapping
    @Transactional(readOnly = true)
    public Iterable<AnimalsDto> getAnimals() {
        return StreamSupport.stream(animalsRepo.findAll().spliterator(), false)
                .peek(animal -> Hibernate.initialize(animal.getProcedures())) // Инициализация
                .map(animal -> new AnimalsDto(
                        animal.getId(),
                        animal.getName(),
                        animal.getType(),
                        animal.getCreatedAt(),
                        animal.getUpdatedAt(),
                        animal.getProcedures(),
                        new ZoosDto(
                                animal.getZoo().getId(),
                                animal.getZoo().getName(),
                                animal.getZoo().getCreatedAt(),
                                animal.getZoo().getUpdatedAt(),
                                null
                        )
                ))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public Optional<AnimalsDto> getAnimalById(@PathVariable Integer id) {
        return animalsRepo.findById(id)
                .map(animal -> {
                    Hibernate.initialize(animal.getProcedures());
                    return new AnimalsDto(
                            animal.getId(),
                            animal.getName(),
                            animal.getType(),
                            animal.getCreatedAt(),
                            animal.getUpdatedAt(),
                            animal.getProcedures(),
                            new ZoosDto(
                                    animal.getZoo().getId(),
                                    animal.getZoo().getName(),
                                    animal.getZoo().getCreatedAt(),
                                    animal.getZoo().getUpdatedAt(),
                                    null
                            )
                    );
                });
    }

    @PostMapping
    Animals postAnimal(@RequestBody Animals animal) {
        if (zoosRepo == null) {
            return null;
        }

        Zoos zoo = zoosRepo.findById(animal.getZooId()).orElseThrow(()
                -> new IllegalArgumentException("Zoo not found with id: " + animal.getZooId()));
        animal.setZoo(zoo);

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
