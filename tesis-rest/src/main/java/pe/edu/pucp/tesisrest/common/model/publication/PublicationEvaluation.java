package pe.edu.pucp.tesisrest.common.model.publication;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pe.edu.pucp.tesisrest.common.model.base.Model;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "publication_evaluation")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PublicationEvaluation extends Model {
    @Id
    @Column(name = "id_publication_eval")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPublicationEval;

    @Column(name = "idPublication")
    private Integer idPublication;

    @Column(name = "id_subcategory")
    private Integer idSubcategory;

    @Column(name = "score")
    private BigDecimal score;

    @Column(name = "timestamp")
    private Timestamp timestamp;

    @Column(name = "id_rule")
    private Integer idRule;
}
