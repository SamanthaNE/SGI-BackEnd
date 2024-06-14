package pe.edu.pucp.tesisrest.common.model.project;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectFundedId implements Serializable {
    private String idProject;
    private String idFunding;
}
