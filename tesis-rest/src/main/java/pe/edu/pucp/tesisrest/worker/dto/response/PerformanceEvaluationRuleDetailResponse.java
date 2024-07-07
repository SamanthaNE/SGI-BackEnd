package pe.edu.pucp.tesisrest.worker.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pe.edu.pucp.tesisrest.common.dto.base.Response;
import pe.edu.pucp.tesisrest.worker.dto.PerformanceEvaluationRuleDetailDto;

@EqualsAndHashCode(callSuper = true)
@Data
public class PerformanceEvaluationRuleDetailResponse extends Response {
    private PerformanceEvaluationRuleDetailDto ruleDetail;
}
