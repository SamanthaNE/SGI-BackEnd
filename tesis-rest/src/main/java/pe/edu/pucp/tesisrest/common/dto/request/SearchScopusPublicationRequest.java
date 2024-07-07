package pe.edu.pucp.tesisrest.common.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pe.edu.pucp.tesisrest.common.dto.base.Request;

@EqualsAndHashCode(callSuper = true)
@Data
public class SearchScopusPublicationRequest extends Request {
    private String title;
    private String year;
    private String author;
    private String publisher;
    private String aggregationType;
    private String subTypeDescription;
}
