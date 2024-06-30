package pe.edu.pucp.tesisrest.worker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.pucp.tesisrest.common.model.project.ProjectEvaluation;

public interface ProjectEvaluationRepository extends JpaRepository<ProjectEvaluation, Integer> {
    ProjectEvaluation findProjectEvaluationByIdRule(int idRule);
}
