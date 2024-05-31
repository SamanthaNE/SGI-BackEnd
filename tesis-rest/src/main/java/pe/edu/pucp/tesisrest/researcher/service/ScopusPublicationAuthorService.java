package pe.edu.pucp.tesisrest.researcher.service;

import pe.edu.pucp.tesisrest.researcher.dto.request.ResearchGroupListRequest;
import pe.edu.pucp.tesisrest.researcher.dto.request.ScopusPublicationAuthorListRequest;
import pe.edu.pucp.tesisrest.researcher.dto.request.ScopusPublicationRequest;
import pe.edu.pucp.tesisrest.researcher.dto.response.ResearchGroupListResponse;
import pe.edu.pucp.tesisrest.researcher.dto.response.ScopusPublicationAuthorListResponse;
import pe.edu.pucp.tesisrest.researcher.dto.response.ScopusPublicationResponse;

public interface ScopusPublicationAuthorService {

    ScopusPublicationAuthorListResponse searchScopusPublicationOfAuthor (ScopusPublicationAuthorListRequest request);
    ScopusPublicationResponse getScopusPublicationById(ScopusPublicationRequest request);
    ResearchGroupListResponse searchResearchGroupOfAuthor (ResearchGroupListRequest request);
}
