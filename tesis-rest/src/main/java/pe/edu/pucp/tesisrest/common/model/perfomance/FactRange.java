package pe.edu.pucp.tesisrest.common.model.perfomance;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pe.edu.pucp.tesisrest.common.model.base.Model;
import pe.edu.pucp.tesisrest.common.model.project.ProjectFundedId;

import java.math.BigDecimal;

@Entity
@Table(name = "fact_range")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class FactRange extends Model {

    @EmbeddedId
    private FactRangeId id;

    @Column(name = "scientific_type")
    @Size(max = 50, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String scientificType;

    @Column(name = "attribute")
    @Size(max = 255, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String attribute;

    @Column(name = "condition_type")
    @Size(max = 10, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String conditionType;

    @Column(name = "min_value")
    private BigDecimal minValue;

    @Column(name = "max_value")
    private BigDecimal maxValue;

    @Column(name = "score")
    private BigDecimal score;

    @Column(name = "connector")
    @Size(max = 10, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String connector;

    @Column(name = "id_qfactor")
    private int idQFactor;
}
