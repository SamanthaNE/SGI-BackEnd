package pe.edu.pucp.tesisrest.worker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.pucp.tesisrest.common.model.perfomance.FactRange;
import pe.edu.pucp.tesisrest.common.model.perfomance.FactRangeId;

import java.util.List;
import java.util.Optional;

public interface FactRangeRepository extends JpaRepository<FactRange, FactRangeId> {
    @Query(value = "SELECT MAX(fr.seq) FROM fact_range fr WHERE fr.id_rule = :idRule", nativeQuery = true)
    Integer findLastSeq(@Param(value = "idRule") int idRule);

    @Query(value = "SELECT * FROM fact_range fr WHERE fr.id_rule = :idRule AND fr.id_qfactor = :idFactor AND fr.scientific_type = :scientificType", nativeQuery = true)
    List<FactRange> findAllByIdRuleAndIdQFactor(@Param(value = "idRule") int idRule,
                                                @Param(value = "idFactor") int idFactor,
                                                @Param(value = "scientificType") String scientificType);

    @Query(value = "SELECT * FROM fact_range fr WHERE fr.id_rule = :idRule AND fr.id_qfactor = :idFactor", nativeQuery = true)
    List<FactRange> findAllByIdRuleAndIdQFactor(@Param(value = "idRule") int idRule,
                                                @Param(value = "idFactor") int idFactor);

    FactRange findFactRangeByIdFR(FactRangeId id);
}
