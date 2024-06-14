package pe.edu.pucp.tesisrest.researcher.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pe.edu.pucp.tesisrest.common.dto.base.Request;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
public class NewFundingRequest extends Request {
    private String fundedAs;
    private String category;
    private String currCode;
    private BigDecimal amount;
    private String fundingType;
    private String fundedBy;
}
