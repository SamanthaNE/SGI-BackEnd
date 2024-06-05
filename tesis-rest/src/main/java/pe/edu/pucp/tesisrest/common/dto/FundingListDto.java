package pe.edu.pucp.tesisrest.common.dto;

import lombok.Data;

@Data
public class FundingListDto {
    private Long idFunding;
    private String fundedAs;
    private String category;
    private String currCode;
    private Double amount;
    private String identifier;
    private String fundingType;
    private String fundedBy;
}
