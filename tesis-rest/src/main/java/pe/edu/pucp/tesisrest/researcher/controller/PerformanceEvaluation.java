package pe.edu.pucp.tesisrest.researcher.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.pucp.tesisrest.researcher.dto.request.PublicationAuthorDetailRequest;
import pe.edu.pucp.tesisrest.researcher.dto.request.PublicationAuthorListRequest;
import pe.edu.pucp.tesisrest.researcher.dto.response.PublicationAuthorDetailResponse;
import pe.edu.pucp.tesisrest.researcher.dto.response.PublicationAuthorListResponse;
import pe.edu.pucp.tesisrest.researcher.service.PerformanceEvaluationService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/evaluation")
public class PerformanceEvaluation {

    private final PerformanceEvaluationService performanceEvaluationService;

    @GetMapping(value = "/publications")
    @Operation(summary = "Search publications by scopus_author_id")
    public PublicationAuthorListResponse getPublicationsOfAuthor(@ModelAttribute PublicationAuthorListRequest request){
        return performanceEvaluationService.getPublicationAuthorList(request);
    }

    @GetMapping(value = "/publicationdetail")
    @Operation(summary = "Obtain detail of a publication by publication_id")
    public PublicationAuthorDetailResponse getPublicationDetailById(@ModelAttribute PublicationAuthorDetailRequest request){
        return performanceEvaluationService.getPublicationDetailById(request);
    }
}
