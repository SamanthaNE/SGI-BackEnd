package pe.edu.pucp.tesisrest.worker.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pe.edu.pucp.tesisrest.common.dto.PublicationDetailDto;
import pe.edu.pucp.tesisrest.common.dto.base.Response;

@EqualsAndHashCode(callSuper = true)
@Data
public class PublicationDetailResponse extends Response {
    private PublicationDetailDto publicationDetail;
}
