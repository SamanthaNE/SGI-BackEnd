package pe.edu.pucp.tesisrest.worker.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pe.edu.pucp.tesisrest.common.model.perfomance.FactAttributeId;
import pe.edu.pucp.tesisrest.common.model.perfomance.FactRangeId;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateRuleDto extends NewFactGeneralDto {
    private FactAttributeId idFA;
    private FactRangeId idFR;
}
