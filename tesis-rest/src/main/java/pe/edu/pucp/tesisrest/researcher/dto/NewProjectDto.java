package pe.edu.pucp.tesisrest.researcher.dto;

import lombok.Data;
import pe.edu.pucp.tesisrest.common.model.funding.Funding;
import pe.edu.pucp.tesisrest.common.model.project.Project;
import pe.edu.pucp.tesisrest.common.model.project.ProjectTeamPUCP;

import java.util.ArrayList;
import java.util.List;

@Data
public class NewProjectDto {
    private Project project;
    private List<ProjectTeamPUCP> projectTeam = new ArrayList<>();
    private List<Funding> relatedFunding = new ArrayList<>();
}
