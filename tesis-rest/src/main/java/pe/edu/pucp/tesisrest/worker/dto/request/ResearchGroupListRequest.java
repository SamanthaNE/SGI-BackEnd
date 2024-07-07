package pe.edu.pucp.tesisrest.worker.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pe.edu.pucp.tesisrest.common.dto.base.PaginatedRequest;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResearchGroupListRequest extends PaginatedRequest {
    //Filters
    private String nameGroup;
    //private String nameOrgUnitPO;
    private String category;
}
