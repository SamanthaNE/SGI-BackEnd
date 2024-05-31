package pe.edu.pucp.tesisrest.researcher.service;

import pe.edu.pucp.tesisrest.researcher.dto.request.PublicationAuthorDetailRequest;
import pe.edu.pucp.tesisrest.researcher.dto.request.PublicationAuthorListRequest;
import pe.edu.pucp.tesisrest.researcher.dto.response.PublicationAuthorDetailResponse;
import pe.edu.pucp.tesisrest.researcher.dto.response.PublicationAuthorListResponse;

public interface PerformanceEvaluationService {

    PublicationAuthorListResponse getPublicationAuthorList(PublicationAuthorListRequest request);
    PublicationAuthorDetailResponse getPublicationDetailById(PublicationAuthorDetailRequest request);
}
