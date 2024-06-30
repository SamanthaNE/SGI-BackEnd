package pe.edu.pucp.tesisrest.worker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.pucp.tesisrest.common.model.perfomance.FactAttribute;
import pe.edu.pucp.tesisrest.common.model.perfomance.FactAttributeId;

import java.util.List;

public interface FactAttributeRepository extends JpaRepository<FactAttribute, FactAttributeId> {
    @Query(value = "SELECT MAX(fa.seq) FROM fact_attribute fa WHERE fa.id_rule = :idRule", nativeQuery = true)
    Integer findLastSeq(@Param(value = "idRule") int idRule);

    @Query(value = "SELECT * FROM fact_attribute fa WHERE fa.id_rule = :idRule AND fa.id_qfactor = :idFactor AND fa.scientific_type = :scientificType", nativeQuery = true)
    List<FactAttribute> findAllByIdRuleAndIdQFactor(@Param(value = "idRule") int idRule,
                                                    @Param(value = "idFactor") int idFactor,
                                                    @Param(value = "scientificType") String scientificType);
}
