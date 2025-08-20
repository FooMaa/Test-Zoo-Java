package foomaa.test.zoo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.*;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Animals {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Integer id;

    @JdbcTypeCode(SqlTypes.UUID)
    @Column(unique = true, nullable = false)
    @JsonProperty("uuid")
    private UUID uuid = UUID.randomUUID();

    @JsonProperty("name")
    @Column(nullable = false)
    private String name;

    @Column(name = "animal_type")
    @JsonProperty("type")
    private String type;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "animal",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Procedures> procedures = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zoo", nullable = false)
    private Zoos zoo;

    @Transient
    @JsonProperty("zoo_id")
    private Integer zooId;
}
