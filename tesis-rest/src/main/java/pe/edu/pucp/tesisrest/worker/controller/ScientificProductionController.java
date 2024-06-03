package pe.edu.pucp.tesisrest.worker.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.pucp.tesisrest.worker.dto.request.ProjectDetailRequest;
import pe.edu.pucp.tesisrest.worker.dto.request.ProjectListRequest;
import pe.edu.pucp.tesisrest.worker.dto.request.PublicationDetailRequest;
import pe.edu.pucp.tesisrest.worker.dto.request.PublicationListRequest;
import pe.edu.pucp.tesisrest.worker.dto.response.ProjectDetailResponse;
import pe.edu.pucp.tesisrest.worker.dto.response.ProjectListResponse;
import pe.edu.pucp.tesisrest.worker.dto.response.PublicationDetailResponse;
import pe.edu.pucp.tesisrest.worker.dto.response.PublicationListResponse;
import pe.edu.pucp.tesisrest.worker.service.ScientificProductionService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/scientificprod")
public class ScientificProductionController {

    private final ScientificProductionService scientificProductionService;

    @GetMapping(value = "/publications")
    @Operation(summary = "Search all validated publications ")
    public PublicationListResponse getPublicationsOfAuthor(@ModelAttribute PublicationListRequest request){
        return scientificProductionService.getPublicationList(request);
    }

    @GetMapping(value = "/publicationdetail")
    @Operation(summary = "Obtain detail of a publication by publication_id")
    public PublicationDetailResponse getPublicationDetailById(@ModelAttribute PublicationDetailRequest request){
        return scientificProductionService.getPublicationDetailById(request);
    }

    @GetMapping(value = "/projects")
    @Operation(summary = "Obtain projects by idPerson")
    public ProjectListResponse getProjectList(@ModelAttribute ProjectListRequest request){
        return scientificProductionService.getProjectList(request);
    }

    @GetMapping(value = "/projectdetail")
    @Operation(summary = "Obtain detail of a project by idProject")
    public ProjectDetailResponse getProjectDetailById(@ModelAttribute ProjectDetailRequest request){
        return scientificProductionService.getProjectDetailById(request);
    }
}
