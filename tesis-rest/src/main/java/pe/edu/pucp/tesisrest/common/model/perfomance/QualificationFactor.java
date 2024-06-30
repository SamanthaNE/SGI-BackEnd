package pe.edu.pucp.tesisrest.common.model.perfomance;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pe.edu.pucp.tesisrest.common.model.base.Model;

import java.math.BigDecimal;

@Entity
@Table(name = "qualification_factor")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class QualificationFactor extends Model {

    @Id
    @Column(name = "id_factor")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idFactor;

    @Column(name = "id_rule")
    private int idRule;

    @Column(name = "scientific_type")
    @Size(max = 50, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String scientificType;

    @Column(name = "conditions")
    private String conditions;

    @Column(name = "multiplying_factor")
    private BigDecimal multiplyingFactor;
}
