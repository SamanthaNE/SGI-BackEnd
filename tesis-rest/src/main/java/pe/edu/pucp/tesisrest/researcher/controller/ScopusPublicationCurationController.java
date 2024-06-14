package pe.edu.pucp.tesisrest.researcher.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pe.edu.pucp.tesisrest.researcher.dto.request.*;
import pe.edu.pucp.tesisrest.researcher.dto.response.*;
import pe.edu.pucp.tesisrest.researcher.service.ScopusPublicationAuthorService;
import pe.edu.pucp.tesisrest.researcher.service.ScopusPublicationCurationService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/scopus")
public class ScopusPublicationCurationController {

    private final ScopusPublicationAuthorService scopusPublicationAuthorService;
    private final ScopusPublicationCurationService scopusPublicationCurationService;

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

    @PostMapping(value = "/newproject")
    @Operation(summary = "Register a new project (with its funding) related to a publication")
    public NewProjectWithFundingResponse register(@RequestBody NewProjectWithFundingRequest request) {
        return scopusPublicationCurationService.createNewProjectWithFunding(request);
    }

    @PostMapping(value = "/newpublication")
    @Operation(summary = "Register a new publication related to its scopus publication")
    public NewPublicationOfSPCurationResponse register(@RequestBody NewPublicationOfSPCurationRequest request) {
        return scopusPublicationCurationService.createNewPublicationOfSPCuration(request);
    }
}
