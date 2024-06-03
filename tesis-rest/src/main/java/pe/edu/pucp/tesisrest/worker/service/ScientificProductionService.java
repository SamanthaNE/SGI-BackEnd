package pe.edu.pucp.tesisrest.worker.service;

import pe.edu.pucp.tesisrest.worker.dto.request.ProjectDetailRequest;
import pe.edu.pucp.tesisrest.worker.dto.request.ProjectListRequest;
import pe.edu.pucp.tesisrest.worker.dto.request.PublicationDetailRequest;
import pe.edu.pucp.tesisrest.worker.dto.request.PublicationListRequest;
import pe.edu.pucp.tesisrest.worker.dto.response.ProjectDetailResponse;
import pe.edu.pucp.tesisrest.worker.dto.response.ProjectListResponse;
import pe.edu.pucp.tesisrest.worker.dto.response.PublicationDetailResponse;
import pe.edu.pucp.tesisrest.worker.dto.response.PublicationListResponse;

public interface ScientificProductionService {

    PublicationListResponse getPublicationList(PublicationListRequest request);
    PublicationDetailResponse getPublicationDetailById(PublicationDetailRequest request);

    ProjectListResponse getProjectList(ProjectListRequest request);
    ProjectDetailResponse getProjectDetailById(ProjectDetailRequest request);
}
