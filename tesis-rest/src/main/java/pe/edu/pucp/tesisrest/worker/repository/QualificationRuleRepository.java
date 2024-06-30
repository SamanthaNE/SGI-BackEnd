package pe.edu.pucp.tesisrest.worker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.pucp.tesisrest.common.model.perfomance.QualificationRule;

import java.util.List;

public interface QualificationRuleRepository extends JpaRepository<QualificationRule, Integer> {
    QualificationRule findByIdRule(int idRule);
    List<QualificationRule> getAllByScientificTypeAndStatus(String scientificType, Integer status);
}
