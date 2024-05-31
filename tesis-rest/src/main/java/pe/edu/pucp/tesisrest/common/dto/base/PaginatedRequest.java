package pe.edu.pucp.tesisrest.common.dto.base;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PaginatedRequest extends Request {

    private Integer page;
    private Integer size;

}
