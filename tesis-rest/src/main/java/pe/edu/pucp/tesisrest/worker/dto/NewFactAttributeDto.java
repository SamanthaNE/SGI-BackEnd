package pe.edu.pucp.tesisrest.worker.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class NewFactAttributeDto {
    private String attribute;
    private String conditionType;
    private String attributeValue;
    private BigDecimal score;
    private String connector;
}
