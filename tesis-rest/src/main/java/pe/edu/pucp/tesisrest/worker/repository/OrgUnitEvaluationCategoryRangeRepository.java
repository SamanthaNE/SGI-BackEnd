package pe.edu.pucp.tesisrest.worker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.pucp.tesisrest.common.model.perfomance.OrgUnitEvaluationCategoryRange;

import java.math.BigDecimal;

public interface OrgUnitEvaluationCategoryRangeRepository extends JpaRepository<OrgUnitEvaluationCategoryRange, Integer> {
    @Query(value = "SELECT id_category_range FROM orgunit_eval_category_range oecr WHERE :scoreValue BETWEEN min_score AND max_score", nativeQuery = true)
    Integer findIdCategoryBetweenMinValueAndMaxValue(@Param(value = "scoreValue") BigDecimal scoreValue);
}
