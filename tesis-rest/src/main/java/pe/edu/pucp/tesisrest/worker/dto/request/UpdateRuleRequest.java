package pe.edu.pucp.tesisrest.worker.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pe.edu.pucp.tesisrest.common.dto.base.Request;
import pe.edu.pucp.tesisrest.worker.dto.NewFactGeneralDto;
import pe.edu.pucp.tesisrest.worker.dto.UpdateRuleDto;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateRuleRequest extends Request {
    private int idRule;
    private int idSubcategory;
    private String ruleName;
    private String scientificType;

    // Lista de condiciones - general para actualizar
    private List<UpdateRuleDto> factGeneralListToUpdate = new ArrayList<>();

    // Lista de factores de multiplicacion para actualizar
    private List<UpdateRuleDto> multFactorListToUpdate = new ArrayList<>();

    // Lista de condiciones - general nuevas
    private List<NewFactGeneralDto> factGeneralList = new ArrayList<>();

    // Lista de factores de multiplicacion nuevas
    private List<NewFactGeneralDto> multFactorList = new ArrayList<>();
}
