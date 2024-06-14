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
@Table(name = "project_team_pucp")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProjectTeamPUCP extends Model {
    @EmbeddedId
    private ProjectTeamPUCPId id;

    @Column(name = "idOrgUnit")
    @Size(max = 128, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String idOrgUnit;

    @Column(name = "idPerson")
    @Size(max = 128, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String idPerson;

    @Column(name = "idPerson_Role")
    @Size(max = 50, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String idPersonRole;

    @Column(name = "Tipo_Recurso")
    @Size(max = 50, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String tipoRecurso;

    @Column(name = "Name")
    @Size(max = 128, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String name;
}
