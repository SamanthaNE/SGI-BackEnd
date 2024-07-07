package pe.edu.pucp.tesisrest.worker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.pucp.tesisrest.common.model.perfomance.PerformanceEvaluationSubcategory;

import java.math.BigDecimal;
import java.util.List;

public interface PerformanceEvaluationSubcategoryRepository extends JpaRepository<PerformanceEvaluationSubcategory, Integer> {
    @Query(value = "SELECT pes.id_category FROM performance_evaluation_subcategory pes WHERE pes.id_subcategory = :idSubcategory", nativeQuery = true)
    Integer findIdCategoryByIdSubcategory(@Param(value = "idSubcategory") int idSubcategory);

    @Query(value = "SELECT pes.id_category FROM performance_evaluation_subcategory pes GROUP BY pes.id_category", nativeQuery = true)
    List<Integer> findAllIdCategories();
}
