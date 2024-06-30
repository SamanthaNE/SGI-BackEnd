package pe.edu.pucp.tesisrest.worker.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pe.edu.pucp.tesisrest.common.dto.base.Request;
import pe.edu.pucp.tesisrest.worker.dto.NewFactAttributeDto;
import pe.edu.pucp.tesisrest.worker.dto.NewFactGeneralDto;
import pe.edu.pucp.tesisrest.worker.dto.NewFactRangeDto;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class NewRuleRequest extends Request {
    private int idSubcategory;
    private String ruleName;
    private String scientificType;

    // Lista de condiciones - fact_attribute
    //private List<NewFactAttributeDto> factAttributeList = new ArrayList<>();

    // Lista de condiciones - fact_range
    //private List<NewFactRangeDto> factRangeList = new ArrayList<>();

    // Lista de condiciones - general
    private List<NewFactGeneralDto> factGeneralList = new ArrayList<>();

    // Lista de factores de multiplicacion
    private List<NewFactGeneralDto> multFactorList = new ArrayList<>();
}
