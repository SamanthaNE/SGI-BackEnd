package pe.edu.pucp.tesisrest.worker.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pe.edu.pucp.tesisrest.common.dto.ProjectDto;
import pe.edu.pucp.tesisrest.common.dto.base.Response;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProjectListResponse extends Response {
    private List<ProjectDto> projects = new ArrayList<>();
    private Long total = 0L;
}
