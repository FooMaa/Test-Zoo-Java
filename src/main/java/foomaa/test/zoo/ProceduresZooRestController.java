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
@RequestMapping("/zoos/procedures")
public class ProceduresZooRestController {
    private final ProceduresRepository proceduresRepo;

    @Autowired
    ProceduresZooRestController(ProceduresRepository newProceduresRepository) {
        proceduresRepo = newProceduresRepository;
    }

    @GetMapping
    @Transactional(readOnly = true)
    Iterable<ProceduresDto> getProcedures() {
        return StreamSupport.stream(proceduresRepo.findAll().spliterator(), false)
                .map(procedure -> new ProceduresDto(
                        procedure.getId(),
                        procedure.getName(),
                        procedure.getCreatedAt(),
                        procedure.getUpdatedAt(),
                        procedure.getAnimal()
                ))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    Optional<ProceduresDto> getProceduresById(@PathVariable Integer id) {
        return proceduresRepo.findById(id)
                .map(procedure -> {
                    return new ProceduresDto(
                            procedure.getId(),
                            procedure.getName(),
                            procedure.getCreatedAt(),
                            procedure.getUpdatedAt(),
                            procedure.getAnimal()
                    );
                });
    }

    @PostMapping
    Procedures postProcedure(@RequestBody Procedures procedure) {
        return proceduresRepo.save(procedure);
    }

    @PutMapping("/{id}")
    ResponseEntity<Procedures> putProcedures(@PathVariable Integer id, @RequestBody Procedures procedure) {
        return proceduresRepo.existsById(id) ?
                new ResponseEntity<Procedures>(proceduresRepo.save(procedure), HttpStatus.OK) :
                new ResponseEntity<Procedures>(proceduresRepo.save(procedure), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    void deleteProcedures(@PathVariable Integer id) {
        proceduresRepo.deleteById(id);
    }
}
