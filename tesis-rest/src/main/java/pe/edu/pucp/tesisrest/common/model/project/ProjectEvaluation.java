package pe.edu.pucp.tesisrest.common.model.project;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pe.edu.pucp.tesisrest.common.model.base.Model;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "project_evaluation")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ProjectEvaluation extends Model {

    @Id
    @Column(name = "id_project_eval")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idProjectEval;

    @Column(name = "idProject")
    @Size(max = 128, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String idProject;

    @Column(name = "id_subcategory")
    private Integer idSubcategory;

    @Column(name = "score")
    private BigDecimal score;

    @Column(name = "timestamp")
    private Timestamp timestamp;

    @Column(name = "id_rule")
    private Integer idRule;
}
