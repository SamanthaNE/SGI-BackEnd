package pe.edu.pucp.tesisrest.worker.service;

import pe.edu.pucp.tesisrest.worker.dto.request.DisableRuleRequest;
import pe.edu.pucp.tesisrest.worker.dto.request.NewRuleRequest;
import pe.edu.pucp.tesisrest.worker.dto.request.PerformanceEvaluationRulesListRequest;
import pe.edu.pucp.tesisrest.worker.dto.response.DisableRuleResponse;
import pe.edu.pucp.tesisrest.worker.dto.response.NewRuleResponse;
import pe.edu.pucp.tesisrest.worker.dto.response.PerformanceEvaluationRulesListResponse;

public interface PerformanceEvaluationRulesService {
    PerformanceEvaluationRulesListResponse getPerformanceEvaluationRulesList(PerformanceEvaluationRulesListRequest request);

    NewRuleResponse addNewRule(NewRuleRequest request);

    DisableRuleResponse disableRule(DisableRuleRequest request);
}
