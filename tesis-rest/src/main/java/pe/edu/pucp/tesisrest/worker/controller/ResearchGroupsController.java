package pe.edu.pucp.tesisrest.worker.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.pucp.tesisrest.worker.dto.request.ResearchGroupDetailRequest;
import pe.edu.pucp.tesisrest.worker.dto.request.ResearchGroupListRequest;
import pe.edu.pucp.tesisrest.worker.dto.response.ResearchGroupDetailResponse;
import pe.edu.pucp.tesisrest.worker.dto.response.ResearchGroupListResponse;
import pe.edu.pucp.tesisrest.worker.service.ResearchGroupService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/researchgroups")
public class ResearchGroupsController {

    private final ResearchGroupService researchGroupService;

    @GetMapping(value = "/list")
    @Operation(summary = "Obtain list of research groups")
    public ResearchGroupListResponse getResearchGroupList(@ModelAttribute ResearchGroupListRequest request){
        return researchGroupService.getResearchGroupList(request);
    }

    @GetMapping(value = "/detail")
    @Operation(summary = "Obtain detail of a research group by idOrgUnit")
    public ResearchGroupDetailResponse getResearchGroupDetail(@ModelAttribute ResearchGroupDetailRequest request){
        return researchGroupService.getResearchGroupDetail(request);
    }
}
