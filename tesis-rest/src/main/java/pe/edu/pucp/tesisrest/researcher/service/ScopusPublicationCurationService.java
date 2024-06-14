package pe.edu.pucp.tesisrest.researcher.service;

import pe.edu.pucp.tesisrest.researcher.dto.request.NewFundingRequest;
import pe.edu.pucp.tesisrest.researcher.dto.request.NewProjectWithFundingRequest;
import pe.edu.pucp.tesisrest.researcher.dto.request.NewPublicationOfSPCurationRequest;
import pe.edu.pucp.tesisrest.researcher.dto.response.NewFundingResponse;
import pe.edu.pucp.tesisrest.researcher.dto.response.NewProjectWithFundingResponse;
import pe.edu.pucp.tesisrest.researcher.dto.response.NewPublicationOfSPCurationResponse;

public interface ScopusPublicationCurationService {
    NewProjectWithFundingResponse createNewProjectWithFunding(NewProjectWithFundingRequest request);

    NewPublicationOfSPCurationResponse createNewPublicationOfSPCuration(NewPublicationOfSPCurationRequest request);
}
