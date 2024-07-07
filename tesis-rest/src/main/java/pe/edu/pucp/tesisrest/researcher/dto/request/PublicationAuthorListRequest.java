package pe.edu.pucp.tesisrest.researcher.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pe.edu.pucp.tesisrest.common.dto.base.PaginatedRequest;

@EqualsAndHashCode(callSuper = true)
@Data
public class PublicationAuthorListRequest extends PaginatedRequest {
    private String scopusAuthorId;

    //Filters
    private String title;
    private String publishedIn;
    private String resourceType;
    private String year;

    private String author;
    private String idOrgUnit;
}
