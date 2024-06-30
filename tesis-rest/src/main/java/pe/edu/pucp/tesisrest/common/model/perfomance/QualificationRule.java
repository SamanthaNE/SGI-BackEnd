package pe.edu.pucp.tesisrest.common.model.perfomance;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pe.edu.pucp.tesisrest.common.model.base.Model;

@Entity
@Table(name = "qualification_rule")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class QualificationRule extends Model {

    @Id
    @Column(name = "id_rule")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idRule;

    @Column(name = "id_subcategory")
    private int idSubcategory;

    @Column(name = "qr_name")
    @Size(max = 255, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String ruleName;

    @Column(name = "qr_scientific_type")
    @Size(max = 50, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String scientificType;

    @Column(name = "qr_conditions")
    private String conditions;

    @Column(name = "qr_actions")
    private String actions;

    @Column(name = "status")
    private Integer status;
}
