package pe.edu.pucp.tesisrest.worker.dto;

import lombok.Data;
import pe.edu.pucp.tesisrest.common.model.perfomance.FactAttribute;
import pe.edu.pucp.tesisrest.common.model.perfomance.FactRange;
import pe.edu.pucp.tesisrest.common.model.perfomance.QualificationFactor;
import pe.edu.pucp.tesisrest.common.model.perfomance.QualificationRule;

import java.util.ArrayList;
import java.util.List;

@Data
public class PerformanceEvaluationRuleDetailDto {
    private QualificationRule rule;
    private Integer idCategory;
    private Integer idFactor;

    // Lista de condiciones - fact_attribute
    private List<FactAttribute> factAttributeListRule = new ArrayList<>();
    // Lista de condiciones - fact_range
    private List<FactRange> factRangeListRule = new ArrayList<>();

    // Lista de condiciones - fact_attribute
    private List<FactAttribute> factAttributeListFactor = new ArrayList<>();
    // Lista de condiciones - fact_range
    private List<FactRange> factRangeListFactor = new ArrayList<>();
}
