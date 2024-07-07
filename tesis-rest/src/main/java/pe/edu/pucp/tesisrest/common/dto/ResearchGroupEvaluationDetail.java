package pe.edu.pucp.tesisrest.common.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ResearchGroupEvaluationDetail {
    private String categoryName;
    private BigDecimal categoryScore;
    private Integer quantity;
    private BigDecimal minimumScore;
    private BigDecimal totalScore;
}
