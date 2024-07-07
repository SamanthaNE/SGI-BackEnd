package pe.edu.pucp.tesisrest.common.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
public class EvaluationDetailDto {
    private String categoryName;
    private String subcategoryName;
    private BigDecimal evaluationScore;
}
