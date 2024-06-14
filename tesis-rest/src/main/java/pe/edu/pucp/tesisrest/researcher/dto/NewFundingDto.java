package pe.edu.pucp.tesisrest.researcher.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class NewFundingDto {
    private String idFunding;
    private String fundedAs;
    private String category;
    private String name;
    private String currCode;
    private BigDecimal amount;
    private String fundingType;
    private String fundedBy;
}
