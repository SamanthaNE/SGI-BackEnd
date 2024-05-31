package pe.edu.pucp.tesisrest.researcher.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pe.edu.pucp.tesisrest.researcher.dto.PublicationAuthorDetailDto;
import pe.edu.pucp.tesisrest.common.dto.base.Response;

@EqualsAndHashCode(callSuper = true)
@Data
public class PublicationAuthorDetailResponse extends Response {
    private PublicationAuthorDetailDto publicationAuthorDetail;
}
