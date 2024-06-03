package pe.edu.pucp.tesisrest.common.service;

import pe.edu.pucp.tesisrest.common.dto.FundingListDto;
import pe.edu.pucp.tesisrest.common.dto.ProjectDto;
import pe.edu.pucp.tesisrest.common.dto.ResearcherDto;
import pe.edu.pucp.tesisrest.researcher.dto.AuthorResearcherDto;

import java.util.List;

public interface CommonService {
    List<FundingListDto> getFundingsOfProject(Long idProject);
    List<AuthorResearcherDto> getAuthorsOfPublication(Long publicationId);
    List<ResearcherDto> getProjectTeam(Long idProject);
    List<ResearcherDto> getResearchGroupTeam(String idOrgUnit);
    List<ProjectDto> getProjectsRelatedToPublication(Long publicationId);
}
