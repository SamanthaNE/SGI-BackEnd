package pe.edu.pucp.tesisrest.researcher.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pe.edu.pucp.tesisrest.common.dto.PublicationDto;
import pe.edu.pucp.tesisrest.common.dto.base.Response;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class PublicationAuthorListResponse extends Response {
    private List<PublicationDto> result = new ArrayList<>();
    private Long total = 0L;
}
