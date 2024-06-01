package pe.edu.pucp.tesisrest.researcher.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pe.edu.pucp.tesisrest.common.dto.base.Request;

@EqualsAndHashCode(callSuper = true)
@Data
public class FundingListRequest extends Request {
    private Long idProject;
}
