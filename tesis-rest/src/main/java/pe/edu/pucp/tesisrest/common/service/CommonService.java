package pe.edu.pucp.tesisrest.common.service;

import pe.edu.pucp.tesisrest.common.dto.*;
import pe.edu.pucp.tesisrest.researcher.dto.AuthorResearcherDto;

import java.util.List;

public interface CommonService {
    List<FundingListDto> getFundingsOfProject(Long idProject);
    List<AuthorResearcherDto> getAuthorsOfPublication(Long publicationId);
    List<ResearcherDto> getProjectTeam(Long idProject);
    List<ResearcherDto> getResearchGroupTeam(String idOrgUnit);
    List<ProjectDto> getProjectsRelatedToPublication(Long publicationId);

    List<EvaluationDetailDto> getEvaluationDetailOfPublication(Long publicationId);
    List<EvaluationDetailDto> getEvaluationDetailOfProject(Long idProject);

    List<PublicationDto> getPublicationListOfAResearchGroup(String idOrgUnit);

    List<ProjectDto> getProjectListOfAResearchGroup(String idOrgUnit);

    List<ResearchGroupSciProdDetailDto> getResearchGroupNamesOfPublication(Long publicationId);
    List<ResearchGroupSciProdDetailDto> getResearchGroupNamesOfProject(Long idProject);

    List<ResearchGroupEvaluationDetail> getResearchGroupEvaluationDetail(String idOrgUnit);
}
