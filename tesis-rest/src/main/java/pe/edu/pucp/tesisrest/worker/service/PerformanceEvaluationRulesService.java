package pe.edu.pucp.tesisrest.worker.service;

import pe.edu.pucp.tesisrest.worker.dto.request.*;
import pe.edu.pucp.tesisrest.worker.dto.response.*;

public interface PerformanceEvaluationRulesService {
    PerformanceEvaluationRulesListResponse getPerformanceEvaluationRulesList(PerformanceEvaluationRulesListRequest request);

    NewRuleResponse addNewRule(NewRuleRequest request);

    DisableRuleResponse disableRule(DisableRuleRequest request);

    UpdateRuleResponse updateRule(UpdateRuleRequest request);

    PerformanceEvaluationRuleDetailResponse getPerformanceEvaluationRuleDetail(PerformanceEvaluationRuleDetailRequest request);
}
