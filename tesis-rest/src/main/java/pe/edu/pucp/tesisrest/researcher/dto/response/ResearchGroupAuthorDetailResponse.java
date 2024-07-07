package pe.edu.pucp.tesisrest.researcher.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pe.edu.pucp.tesisrest.common.dto.ResearchGroupEvaluationDetail;
import pe.edu.pucp.tesisrest.common.dto.base.Response;
import pe.edu.pucp.tesisrest.common.dto.ResearchGroupDetailDto;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResearchGroupAuthorDetailResponse extends Response {
    private ResearchGroupDetailDto researchGroupDetail;
}
