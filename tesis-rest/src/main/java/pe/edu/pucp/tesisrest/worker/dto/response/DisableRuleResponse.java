package pe.edu.pucp.tesisrest.worker.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pe.edu.pucp.tesisrest.common.dto.base.Response;

@EqualsAndHashCode(callSuper = true)
@Data
public class DisableRuleResponse extends Response {
    private String messageConfirm;
}
