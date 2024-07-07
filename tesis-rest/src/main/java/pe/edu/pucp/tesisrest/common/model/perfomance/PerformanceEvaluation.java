package pe.edu.pucp.tesisrest.common.model.perfomance;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pe.edu.pucp.tesisrest.common.model.base.Model;

import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Table(name = "performance_evaluation")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PerformanceEvaluation extends Model {

    @Id
    @Column(name = "id_evaluation")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idEvaluation;

    @Column(name = "idOrgUnit")
    @Size(max = 128, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String idOrgUnit;

    @Column(name = "evaluation_year")
    private Date evaluationYear;

    @Column(name = "total_score")
    private BigDecimal totalScore;

    @Column(name = "id_category_range")
    private int idCategoryRange;
}
