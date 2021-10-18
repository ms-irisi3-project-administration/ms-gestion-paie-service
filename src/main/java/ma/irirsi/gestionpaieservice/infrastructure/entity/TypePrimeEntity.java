package ma.irirsi.gestionpaieservice.infrastructure.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class TypePrimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String libelle;
    @JsonIgnoreProperties("typePrimeEntity")
    @OneToMany(mappedBy = "typePrimeEntity")
    List<PrimeEntity> primeEntities;
}
