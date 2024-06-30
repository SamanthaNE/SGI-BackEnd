package pe.edu.pucp.tesisrest.worker.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PerformanceEvaluationCategoryDto {
    private int idCategory;
    private String categoryName;
    private List<PerformanceEvaluationSubcategoryDto> subcategories = new ArrayList<>();
}
