package pe.edu.pucp.tesisrest.researcher.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pe.edu.pucp.tesisrest.researcher.dto.ResearchGroupDto;
import pe.edu.pucp.tesisrest.common.dto.base.Response;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResearchGroupListResponse extends Response {
    private List<ResearchGroupDto> result = new ArrayList<>();
    private Long total = 0L;
}
