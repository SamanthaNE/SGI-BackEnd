package pe.edu.pucp.tesisrest.researcher.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pe.edu.pucp.tesisrest.common.dto.base.PaginatedRequest;
import pe.edu.pucp.tesisrest.common.dto.base.Request;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProjectAuthorListRequest extends PaginatedRequest {
    private String idPerson;

    //Filters
    private String title;
    private String startDate;
    private String endDate;
    private String status;
    private String fundingType;
}
