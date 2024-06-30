package pe.edu.pucp.tesisrest.worker.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PerformanceEvaluationSubcategoryDto {
    private int idSubcategory;
    private String subcategoryName;
    private List<PerformanceEvaluationRuleDto> rules = new ArrayList<>();
}
