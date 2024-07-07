package pe.edu.pucp.tesisrest.common.service;

import pe.edu.pucp.tesisrest.common.dto.request.SearchPersonByRequest;
import pe.edu.pucp.tesisrest.common.dto.request.SearchScopusPublicationRequest;
import pe.edu.pucp.tesisrest.common.dto.response.SearchPersonByResponse;
import pe.edu.pucp.tesisrest.common.dto.response.SearchScopusPublicationResponse;
import pe.edu.pucp.tesisrest.common.model.person.Person;

import java.util.List;


public interface SearchFiltersService {
    SearchPersonByResponse findByPersonNameContainingWords(SearchPersonByRequest request);
}
