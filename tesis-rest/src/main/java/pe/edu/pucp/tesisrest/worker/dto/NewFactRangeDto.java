package pe.edu.pucp.tesisrest.worker.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class NewFactRangeDto {
    private String attribute;
    private String conditionType;
    private BigDecimal minValue;
    private BigDecimal maxValue;
    private BigDecimal score;
    private String connector;
}
