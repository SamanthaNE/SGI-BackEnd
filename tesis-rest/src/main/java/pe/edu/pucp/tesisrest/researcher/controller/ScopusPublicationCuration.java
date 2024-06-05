package pe.edu.pucp.tesisrest.researcher.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pe.edu.pucp.tesisrest.researcher.dto.request.ResearchGroupAuthorListRequest;
import pe.edu.pucp.tesisrest.researcher.dto.request.ScopusPublicationAuthorListRequest;
import pe.edu.pucp.tesisrest.researcher.dto.request.ScopusPublicationRequest;
import pe.edu.pucp.tesisrest.researcher.dto.response.ResearchGroupAuthorListResponse;
import pe.edu.pucp.tesisrest.researcher.dto.response.ScopusPublicationAuthorListResponse;
import pe.edu.pucp.tesisrest.researcher.dto.response.ScopusPublicationResponse;
import pe.edu.pucp.tesisrest.researcher.service.ScopusPublicationAuthorService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/scopus")
public class ScopusPublicationCuration {

    private final ScopusPublicationAuthorService scopusPublicationAuthorService;

    @GetMapping(value = "/publicationsauthor")
    @Operation(summary = "Search scopus publications by scopus_author_id")
    public ScopusPublicationAuthorListResponse searchScopusPublicationOfAuthor(@ModelAttribute ScopusPublicationAuthorListRequest request){
        return scopusPublicationAuthorService.searchScopusPublicationOfAuthor(request);
    }

    @GetMapping(value = "/publicationdetail")
    @Operation(summary = "Obtain detail of a scopus publication by scopus_publication_id")
    public ScopusPublicationResponse getScopusPublicationById(@ModelAttribute ScopusPublicationRequest request){
        return scopusPublicationAuthorService.getScopusPublicationById(request);
    }

    @GetMapping(value = "/researchgroupsauthor")
    @Operation(summary = "Search research groups of an author by idperson")
    public ResearchGroupAuthorListResponse searchResearchGroupOfAuthor(@ModelAttribute ResearchGroupAuthorListRequest request){
        return scopusPublicationAuthorService.searchResearchGroupOfAuthor(request);
    }
}
