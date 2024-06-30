package pe.edu.pucp.tesisrest.worker.dto;

import lombok.Data;
import pe.edu.pucp.tesisrest.common.model.perfomance.FactAttribute;
import pe.edu.pucp.tesisrest.common.model.perfomance.FactRange;
import pe.edu.pucp.tesisrest.common.model.perfomance.QualificationRule;

import java.util.ArrayList;
import java.util.List;

@Data
public class NewRuleDto {
    private QualificationRule rule;

    // Lista de condiciones - fact_attribute
    private List<FactAttribute> factAttributeList = new ArrayList<>();

    // Lista de condiciones - fact_range
    private List<FactRange> factRangeList = new ArrayList<>();
}
