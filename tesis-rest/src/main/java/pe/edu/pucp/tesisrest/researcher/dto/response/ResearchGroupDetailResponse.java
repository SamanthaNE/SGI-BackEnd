package pe.edu.pucp.tesisrest.researcher.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pe.edu.pucp.tesisrest.common.dto.base.Response;
import pe.edu.pucp.tesisrest.researcher.dto.ResearchGroupDetailDto;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResearchGroupDetailResponse extends Response {
    private ResearchGroupDetailDto researchGroupDetail;
}
