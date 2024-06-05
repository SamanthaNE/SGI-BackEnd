package pe.edu.pucp.tesisrest.researcher.service;

import pe.edu.pucp.tesisrest.researcher.dto.request.ResearchGroupAuthorListRequest;
import pe.edu.pucp.tesisrest.researcher.dto.request.ScopusPublicationAuthorListRequest;
import pe.edu.pucp.tesisrest.researcher.dto.request.ScopusPublicationRequest;
import pe.edu.pucp.tesisrest.researcher.dto.response.ResearchGroupAuthorListResponse;
import pe.edu.pucp.tesisrest.researcher.dto.response.ScopusPublicationAuthorListResponse;
import pe.edu.pucp.tesisrest.researcher.dto.response.ScopusPublicationResponse;

public interface ScopusPublicationAuthorService {

    ScopusPublicationAuthorListResponse searchScopusPublicationOfAuthor (ScopusPublicationAuthorListRequest request);
    ScopusPublicationResponse getScopusPublicationById(ScopusPublicationRequest request);
    ResearchGroupAuthorListResponse searchResearchGroupOfAuthor (ResearchGroupAuthorListRequest request);
}
