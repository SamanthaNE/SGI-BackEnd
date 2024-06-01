package pe.edu.pucp.tesisrest.researcher.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pe.edu.pucp.tesisrest.common.dto.base.Response;
import pe.edu.pucp.tesisrest.researcher.dto.FundingListDto;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class FundingListResponse extends Response {
    private List<FundingListDto> result = new ArrayList<>();
    private Long total = 0L;
}
