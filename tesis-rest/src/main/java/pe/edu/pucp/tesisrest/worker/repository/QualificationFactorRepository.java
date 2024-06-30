package pe.edu.pucp.tesisrest.worker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.pucp.tesisrest.common.model.perfomance.QualificationFactor;

import java.util.List;

public interface QualificationFactorRepository extends JpaRepository<QualificationFactor, Long> {
    QualificationFactor findQualificationFactorByIdRule(int IdRule);
}
