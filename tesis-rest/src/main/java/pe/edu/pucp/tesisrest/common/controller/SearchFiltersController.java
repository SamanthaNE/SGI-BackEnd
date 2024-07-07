package pe.edu.pucp.tesisrest.common.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.pucp.tesisrest.common.dto.request.SearchPersonByRequest;
import pe.edu.pucp.tesisrest.common.dto.request.SearchScopusPublicationRequest;
import pe.edu.pucp.tesisrest.common.dto.response.SearchPersonByResponse;
import pe.edu.pucp.tesisrest.common.dto.response.SearchScopusPublicationResponse;
import pe.edu.pucp.tesisrest.common.service.SearchFiltersService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/search")
public class SearchFiltersController {

    private final SearchFiltersService searchFiltersService;

    @GetMapping(value = "/person")
    @Operation(summary = "Search person by name")
    public SearchPersonByResponse getPublicationsOfAuthor(@ModelAttribute SearchPersonByRequest request){
        return searchFiltersService.findByPersonNameContainingWords(request);
    }
}
