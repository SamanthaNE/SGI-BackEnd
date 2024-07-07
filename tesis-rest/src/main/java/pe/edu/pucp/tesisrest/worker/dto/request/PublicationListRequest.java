package pe.edu.pucp.tesisrest.worker.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pe.edu.pucp.tesisrest.common.dto.base.PaginatedRequest;

@EqualsAndHashCode(callSuper = true)
@Data
public class PublicationListRequest extends PaginatedRequest {

    private String title;
    private String publishedIn;
    private String author;
    private String resourceType;
    private String year;

}
