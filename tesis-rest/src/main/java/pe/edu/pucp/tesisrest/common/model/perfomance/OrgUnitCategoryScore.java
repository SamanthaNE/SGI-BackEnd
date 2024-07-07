package pe.edu.pucp.tesisrest.common.model.perfomance;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pe.edu.pucp.tesisrest.common.model.base.Model;

import java.math.BigDecimal;

@Entity
@Table(name = "orgunit_category_score")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class OrgUnitCategoryScore extends Model {

    @Id
    @Column(name = "id_score")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idScore;

    @Column(name = "id_evaluation")
    private int idEvaluation;

    @Column(name = "id_category")
    private int idCategory;

    @Column(name = "score")
    private BigDecimal score;

    @Column(name = "quantity")
    private int quantity;
}
