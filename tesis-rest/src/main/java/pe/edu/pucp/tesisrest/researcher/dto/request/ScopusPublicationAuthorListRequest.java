package pe.edu.pucp.tesisrest.researcher.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pe.edu.pucp.tesisrest.common.dto.base.PaginatedRequest;

@Data
@EqualsAndHashCode(callSuper = true)
public class ScopusPublicationAuthorListRequest extends PaginatedRequest {
    private String authid;
}
