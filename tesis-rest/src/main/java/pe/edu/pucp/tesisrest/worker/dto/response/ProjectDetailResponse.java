package pe.edu.pucp.tesisrest.worker.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pe.edu.pucp.tesisrest.common.dto.ProjectDetailDto;
import pe.edu.pucp.tesisrest.common.dto.base.Response;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProjectDetailResponse extends Response {
    private ProjectDetailDto projectDetailDto;
}
