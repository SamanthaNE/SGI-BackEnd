package pe.edu.pucp.tesisrest.researcher.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pe.edu.pucp.tesisrest.common.dto.base.Request;
import pe.edu.pucp.tesisrest.researcher.dto.NewFundingDto;
import pe.edu.pucp.tesisrest.researcher.dto.NewProjectDto;
import pe.edu.pucp.tesisrest.researcher.dto.ProjectTeamPUCPDto;

import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class NewProjectWithFundingRequest extends Request {
    // Project data
    private String title;
    private String startDate;
    private String endDate;
    private String idProjectStatusTypeConcytec;

    // Project team info
    private List<ProjectTeamPUCPDto> projectTeam;

    // Related funding info
    private List<NewFundingDto> funding;
}
