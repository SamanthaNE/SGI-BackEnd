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
public class ProjectTeamPUCPId implements Serializable {
    private String idProject;
    private int seqMember;
}
