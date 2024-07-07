package pe.edu.pucp.tesisrest.worker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.pucp.tesisrest.common.model.perfomance.PerformanceEvaluation;

public interface PerformanceEvaluationRepository extends JpaRepository<PerformanceEvaluation, Long> {
    PerformanceEvaluation findPerformanceEvaluationByIdOrgUnit(String idOrgUnit);
}
