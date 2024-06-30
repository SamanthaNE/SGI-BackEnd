package pe.edu.pucp.tesisrest.worker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.pucp.tesisrest.common.model.publication.PublicationEvaluation;

public interface PublicationEvaluationRepository extends JpaRepository<PublicationEvaluation, Integer> {
    PublicationEvaluation findPublicationEvaluationByIdRule(int idRule);
}
