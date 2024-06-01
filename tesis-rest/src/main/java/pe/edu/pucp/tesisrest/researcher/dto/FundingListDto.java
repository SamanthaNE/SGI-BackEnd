package pe.edu.pucp.tesisrest.researcher.dto;

import lombok.Data;

@Data
public class FundingListDto {
    private Long idFunding;
    private String fundedAs;
    private String category;
    private String CurrCode;
    private Double Amount;
    private String fundingType;
    private String fundedBy;
}
