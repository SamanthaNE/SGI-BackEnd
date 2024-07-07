package pe.edu.pucp.tesisrest.common.model.perfomance;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pe.edu.pucp.tesisrest.common.model.base.Model;

import java.math.BigDecimal;

@Entity
@Table(name = "orgunit_eval_category_range")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class OrgUnitEvaluationCategoryRange extends Model {

    @Id
    @Column(name = "id_category_range")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCategoryRange;

    @Column(name = "category")
    @Size(max = 50, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String category;

    @Column(name = "min_score")
    private BigDecimal minScore;

    @Column(name = "max_score")
    private BigDecimal maxScore;
}
