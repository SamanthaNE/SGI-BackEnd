package pe.edu.pucp.tesisrest.researcher.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pe.edu.pucp.tesisrest.common.dto.base.Response;
import pe.edu.pucp.tesisrest.researcher.dto.NewFundingDto;

@EqualsAndHashCode(callSuper = true)
@Data
public class NewFundingResponse extends Response {
    private NewFundingDto funding;
}
