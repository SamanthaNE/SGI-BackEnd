package pe.edu.pucp.tesisrest.researcher.service;

import pe.edu.pucp.tesisrest.researcher.dto.request.*;
import pe.edu.pucp.tesisrest.researcher.dto.response.*;

public interface PerformanceEvaluationService {

    PublicationAuthorListResponse getPublicationAuthorList(PublicationAuthorListRequest request);
    PublicationAuthorDetailResponse getPublicationDetailById(PublicationAuthorDetailRequest request);

    ProjectAuthorListResponse getProjectAuthorList(ProjectAuthorListRequest request);
    ProjectAuthorDetailResponse getProjectDetailById(ProjectAuthorDetailRequest request);

    FundingListResponse getFundingRelatedList(FundingListRequest request);
    FundingListResponse getFundingRelatedDetailByPersonId(FundingListRequest request);

    ResearchGroupAuthorListResponse getResearchGroupListOfResearcher(ResearchGroupAuthorListRequest request);
    ResearchGroupAuthorDetailResponse getResearchGroupDetailOfResearcher(ResearchGroupAuthorDetailRequest request);
}
