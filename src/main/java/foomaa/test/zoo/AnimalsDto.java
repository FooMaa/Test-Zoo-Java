package foomaa.test.zoo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnimalsDto {
    private Integer id;
    private String name;
    private String type;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Procedures> procedures = new ArrayList<>();
    private ZoosDto zoo;
}
