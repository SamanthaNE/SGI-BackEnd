package pe.edu.pucp.tesisrest.worker.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class NewFactGeneralDto {
    // Type
    private String type;

    private String attribute;
    private String conditionType;
    private BigDecimal score;
    private String connector;

    // Attribute
    private String attributeValue;
    // Range
    private BigDecimal minValue;
    private BigDecimal maxValue;
}
