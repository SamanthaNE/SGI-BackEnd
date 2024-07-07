package pe.edu.pucp.tesisrest.worker.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pe.edu.pucp.tesisrest.researcher.dto.request.NewProjectWithFundingRequest;
import pe.edu.pucp.tesisrest.researcher.dto.response.NewProjectWithFundingResponse;
import pe.edu.pucp.tesisrest.worker.dto.request.*;
import pe.edu.pucp.tesisrest.worker.dto.response.*;
import pe.edu.pucp.tesisrest.worker.service.PerformanceEvaluationRulesService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/evaluationrules")
public class PerformanceEvaluationRulesController {

    private final PerformanceEvaluationRulesService performanceEvaluationRulesService;

    @GetMapping(value = "/list")
    @Operation(summary = "Obtain list of performance evaluation categories and subcategories with their evaluation rules")
    public PerformanceEvaluationRulesListResponse getEvaluationRules(@ModelAttribute PerformanceEvaluationRulesListRequest request){
        return performanceEvaluationRulesService.getPerformanceEvaluationRulesList(request);
    }

    @GetMapping(value = "/detailrule")
    @Operation(summary = "Obtain detail of a rule")
    public PerformanceEvaluationRuleDetailResponse getPerformanceEvaluationRuleDetail(@ModelAttribute PerformanceEvaluationRuleDetailRequest request){
        return performanceEvaluationRulesService.getPerformanceEvaluationRuleDetail(request);
    }

    @PostMapping(value = "/newrule")
    @Operation(summary = "Register a new rule")
    public NewRuleResponse newRule(@RequestBody NewRuleRequest request){
        return performanceEvaluationRulesService.addNewRule(request);
    }

    @PostMapping(value = "/disablerule")
    @Operation(summary = "Disable a rule")
    public DisableRuleResponse disableRule(@RequestBody DisableRuleRequest request){
        return performanceEvaluationRulesService.disableRule(request);
    }

    @PostMapping(value = "/updaterule")
    @Operation(summary = "Update a rule")
    public UpdateRuleResponse updateRule(@RequestBody UpdateRuleRequest request){
        return performanceEvaluationRulesService.updateRule(request);
    }
}
