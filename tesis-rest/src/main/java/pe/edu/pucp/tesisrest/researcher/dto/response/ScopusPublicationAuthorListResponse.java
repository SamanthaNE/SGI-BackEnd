package pe.edu.pucp.tesisrest.researcher.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pe.edu.pucp.tesisrest.researcher.dto.ScopusPublicationAuthorDto;
import pe.edu.pucp.tesisrest.common.dto.base.Response;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ScopusPublicationAuthorListResponse extends Response {

    private List<ScopusPublicationAuthorDto> result = new ArrayList<>();
    private Long total = 0L;
}
