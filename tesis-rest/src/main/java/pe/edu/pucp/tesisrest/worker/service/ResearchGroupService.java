package pe.edu.pucp.tesisrest.worker.service;

import pe.edu.pucp.tesisrest.worker.dto.request.ResearchGroupDetailRequest;

import pe.edu.pucp.tesisrest.worker.dto.request.ResearchGroupListRequest;
import pe.edu.pucp.tesisrest.worker.dto.response.ResearchGroupDetailResponse;
import pe.edu.pucp.tesisrest.worker.dto.response.ResearchGroupListResponse;


public interface ResearchGroupService {

    ResearchGroupListResponse getResearchGroupList(ResearchGroupListRequest request);
    ResearchGroupDetailResponse getResearchGroupDetail(ResearchGroupDetailRequest request);
}
