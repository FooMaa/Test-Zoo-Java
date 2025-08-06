package foomaa.test.zoo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/zoo/procedure")
public class ProceduresZooRestController {
    private final ProceduresRepository proceduresRepo;

    @Autowired
    ProceduresZooRestController(ProceduresRepository newProceduresRepository) {
        proceduresRepo = newProceduresRepository;
    }

    @GetMapping
    Iterable<Animals> getAnimals() {
        return proceduresRepo.findAll();
    }

    @GetMapping("/{id}")
    Optional<Animals> getAnimalById(@PathVariable Integer id) {
        return proceduresRepo.findById(id);
    }

    @PostMapping
    Animals postAnimal(@RequestBody Animals animal) {
        return proceduresRepo.save(animal);
    }

    @PutMapping("/{id}")
    ResponseEntity<Animals> putAnimal(@PathVariable Integer id, @RequestBody Animals animal) {
        return proceduresRepo.existsById(id) ?
                new ResponseEntity<Animals>(proceduresRepo.save(animal), HttpStatus.OK) :
                new ResponseEntity<Animals>(proceduresRepo.save(animal), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    void deleteAnimal(@PathVariable Integer id) {
        proceduresRepo.deleteById(id);
    }
}
