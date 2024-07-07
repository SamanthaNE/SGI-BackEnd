package pe.edu.pucp.tesisrest.worker.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pe.edu.pucp.tesisrest.common.dto.base.PaginatedRequest;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProjectListRequest extends PaginatedRequest {
    //Filters
    private String title;
    private String startDate;
    private String endDate;
    private String status;
    private String fundingType;
}
