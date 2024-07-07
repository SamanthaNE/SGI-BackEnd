package pe.edu.pucp.tesisrest.common.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pe.edu.pucp.tesisrest.common.dto.base.Request;

@EqualsAndHashCode(callSuper = true)
@Data
public class SearchPersonByRequest extends Request {
    private String personData;
}
