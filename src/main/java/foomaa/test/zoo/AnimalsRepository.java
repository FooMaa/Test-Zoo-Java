package foomaa.test.zoo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import java.util.Optional;


public interface AnimalsRepository extends CrudRepository<Animals, Integer> {
    @Query("SELECT a FROM Animals a LEFT JOIN FETCH a.procedures WHERE a.id = :id")
    Optional<Animals> findByIdWithProcedures(@Param("id") Integer id);
}