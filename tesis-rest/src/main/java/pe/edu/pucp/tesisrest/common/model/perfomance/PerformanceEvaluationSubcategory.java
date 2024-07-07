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
@Table(name = "performance_evaluation_subcategory")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PerformanceEvaluationSubcategory extends Model {

    @Id
    @Column(name = "id_subcategory")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idSubcategory;

    @Column(name = "id_category")
    private int idCategory;

    @Column(name = "subcategory_name")
    @Size(max = 255, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String subcategoryName;

    @Column(name = "subcategory_status")
    @Size(max = 50, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String subcategoryStatus;
}
