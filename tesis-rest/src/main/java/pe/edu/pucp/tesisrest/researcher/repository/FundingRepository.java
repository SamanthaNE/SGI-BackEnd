package pe.edu.pucp.tesisrest.researcher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.edu.pucp.tesisrest.common.model.funding.Funding;

public interface FundingRepository extends JpaRepository<Funding, Long> {
    @Query(value = "SELECT f.idFunding FROM funding f ORDER BY CAST(SUBSTRING(f.idFunding, 1) AS UNSIGNED) DESC LIMIT 1", nativeQuery = true)
    String findLastIdFunding();
}
