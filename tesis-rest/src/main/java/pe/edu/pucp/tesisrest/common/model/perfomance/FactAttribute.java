package pe.edu.pucp.tesisrest.common.model.perfomance;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pe.edu.pucp.tesisrest.common.model.base.Model;

import java.math.BigDecimal;

@Entity
@Table(name = "fact_attribute")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class FactAttribute extends Model {

    @EmbeddedId
    private FactAttributeId id;

    @Column(name = "scientific_type")
    @Size(max = 50, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String scientificType;

    @Column(name = "attribute")
    @Size(max = 255, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String attribute;

    @Column(name = "condition_type")
    @Size(max = 10, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String conditionType;

    @Column(name = "attribute_value")
    @Size(max = 255, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String attributeValue;

    @Column(name = "score")
    private BigDecimal score;

    @Column(name = "connector")
    @Size(max = 10, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String connector;

    @Column(name = "id_qfactor")
    private int idQFactor;
}
