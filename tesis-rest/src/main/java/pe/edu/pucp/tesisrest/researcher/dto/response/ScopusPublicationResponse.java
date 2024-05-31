package pe.edu.pucp.tesisrest.researcher.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pe.edu.pucp.tesisrest.researcher.dto.ScopusPublicationAuthorDto;
import pe.edu.pucp.tesisrest.common.dto.base.Response;

@Data
@EqualsAndHashCode(callSuper = true)
public class ScopusPublicationResponse extends Response {
    private ScopusPublicationAuthorDto scopusPublicationAuthor;
}
