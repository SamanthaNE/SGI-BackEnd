package pe.edu.pucp.tesisrest.researcher.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pe.edu.pucp.tesisrest.common.dto.base.Response;
import pe.edu.pucp.tesisrest.researcher.dto.ProjectAuthorDto;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProjectAuthorListResponse extends Response {
    private List<ProjectAuthorDto> result = new ArrayList<>();
    private Long total = 0L;
}
