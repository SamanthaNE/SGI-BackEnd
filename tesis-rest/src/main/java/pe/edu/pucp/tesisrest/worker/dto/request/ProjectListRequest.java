package pe.edu.pucp.tesisrest.worker.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pe.edu.pucp.tesisrest.common.dto.base.PaginatedRequest;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProjectListRequest extends PaginatedRequest {
    /*
    private String title;
    private String idOrgUnit;
    private String dateStart;
    private String dateEnd;
    private String status
    private String fundingType
     */
}
