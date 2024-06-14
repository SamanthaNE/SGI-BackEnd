package pe.edu.pucp.tesisrest.common.model.publication;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pe.edu.pucp.tesisrest.common.model.base.Model;
import pe.edu.pucp.tesisrest.common.model.project.ProjectTeamPUCPId;

@Entity
@Table(name = "publication_originatesfrom")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class PublicationOriginatesFrom extends Model {
    @EmbeddedId
    private PublicationOriginatesFromId id;
}
