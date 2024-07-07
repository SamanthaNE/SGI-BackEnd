package pe.edu.pucp.tesisrest.common.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pe.edu.pucp.tesisrest.common.dto.base.Response;
import pe.edu.pucp.tesisrest.common.model.scopus.ScopusPublication;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class SearchScopusPublicationResponse extends Response {
    private List<ScopusPublication> resultList = new ArrayList<>();
    private Long totalResults = 0L;
}
