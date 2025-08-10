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
@RequestMapping("/zoos")
public class ZoosRestController {
    private final ZoosRepository zoosRepo;

    @Autowired
    ZoosRestController(ZoosRepository newZoosRepository) {
        zoosRepo = newZoosRepository;
    }

    @GetMapping
    @Transactional(readOnly = true)
    Iterable<ZoosDto> getZoos() {
        return StreamSupport.stream(zoosRepo.findAll().spliterator(), false)
                .peek(zoo -> Hibernate.initialize(zoo.getAnimals())) // Инициализация
                .map(zoo -> new ZoosDto(
                        zoo.getId(),
                        zoo.getName(),
                        zoo.getCreatedAt(),
                        zoo.getUpdatedAt(),
                        zoo.getAnimals() // Теперь коллекция доступна
                ))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    Optional<ZoosDto> getZooById(@PathVariable Integer id) {
        return zoosRepo.findById(id)
                .map(zoo -> {
                    Hibernate.initialize(zoo.getAnimals());  // Инициализация ленивой коллекции
                    return new ZoosDto(
                            zoo.getId(),
                            zoo.getName(),
                            zoo.getCreatedAt(),
                            zoo.getUpdatedAt(),
                            zoo.getAnimals()
                    );
                });
    }

    @PostMapping
    Zoos postZoo(@RequestBody Zoos zoo) {
        return zoosRepo.save(zoo);
    }

    @PutMapping("/{id}")
    ResponseEntity<Zoos> putZoo(@PathVariable Integer id, @RequestBody Zoos zoo) {
        return zoosRepo.existsById(id) ?
                new ResponseEntity<Zoos>(zoosRepo.save(zoo), HttpStatus.OK) :
                new ResponseEntity<Zoos>(zoosRepo.save(zoo), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    void deleteZoo(@PathVariable Integer id) {
        zoosRepo.deleteById(id);
    }
}

