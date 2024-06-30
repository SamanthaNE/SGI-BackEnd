package pe.edu.pucp.tesisrest.worker.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pe.edu.pucp.tesisrest.common.dto.base.Response;
import pe.edu.pucp.tesisrest.worker.dto.PerformanceEvaluationCategoryDto;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class PerformanceEvaluationRulesListResponse extends Response {
    private List<PerformanceEvaluationCategoryDto> result = new ArrayList<>();
    private Long total = 0L;
}
