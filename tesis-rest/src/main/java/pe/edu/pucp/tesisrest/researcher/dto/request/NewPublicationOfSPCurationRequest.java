package pe.edu.pucp.tesisrest.researcher.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pe.edu.pucp.tesisrest.common.dto.ResearchGroupDto;
import pe.edu.pucp.tesisrest.common.dto.base.Request;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class NewPublicationOfSPCurationRequest extends Request {
    private String scopusPublicationId;
    private String scopusAuthorId;
    private String idPerson;
    private List<String> projectIds = new ArrayList<>();
    private String researchGroupIdOrg;
}
