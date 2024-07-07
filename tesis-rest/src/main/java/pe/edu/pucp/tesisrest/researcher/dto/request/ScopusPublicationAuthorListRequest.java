package pe.edu.pucp.tesisrest.researcher.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pe.edu.pucp.tesisrest.common.dto.base.PaginatedRequest;
import pe.edu.pucp.tesisrest.researcher.dto.FilterScopusPublicationDto;

@Data
@EqualsAndHashCode(callSuper = true)
public class ScopusPublicationAuthorListRequest extends PaginatedRequest {
    private String authid;

    // Filters
    private String title = null;
    private String year = null;
    private String author = null;
    private String publisher = null;
    private String aggregationType = null;
    private String subTypeDescription = null;
    //private FilterScopusPublicationDto filterScopusPublicationDto;
}
