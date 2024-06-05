package pe.edu.pucp.tesisrest.worker.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pe.edu.pucp.tesisrest.common.dto.ResearchGroupDetailDto;
import pe.edu.pucp.tesisrest.common.dto.base.Response;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResearchGroupDetailResponse extends Response {
    private ResearchGroupDetailDto researchGroupDetail;
}
