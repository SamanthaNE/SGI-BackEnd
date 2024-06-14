package pe.edu.pucp.tesisrest.common.model.project;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pe.edu.pucp.tesisrest.common.model.base.Model;

@Entity
@Table(name = "project_funded")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProjectFunded extends Model {

    @EmbeddedId
    private ProjectFundedId id;

    @Column(name = "FundedAs")
    @Size(max = 128, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String fundedAs;

    @Column(name = "Categoria")
    @Size(max = 150, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String categoria;

    @Column(name = "Codigo_Propuesta")
    @Size(max = 150, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String codigoPropuesta;

    @Column(name = "Codigo_OGP")
    @Size(max = 150, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String codigoOGP;
}
