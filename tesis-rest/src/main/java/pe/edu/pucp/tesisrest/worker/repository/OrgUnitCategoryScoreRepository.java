package pe.edu.pucp.tesisrest.worker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.pucp.tesisrest.common.model.perfomance.OrgUnitCategoryScore;

public interface OrgUnitCategoryScoreRepository extends JpaRepository<OrgUnitCategoryScore, Integer> {
    OrgUnitCategoryScore findByIdEvaluationAndIdCategory(Integer idEvaluation, Integer idCategory);
}
