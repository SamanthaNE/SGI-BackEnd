package pe.edu.pucp.tesisrest.researcher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.pucp.tesisrest.common.model.orgunit.Funder;
import pe.edu.pucp.tesisrest.common.model.orgunit.FunderId;

public interface FunderRepository extends JpaRepository<Funder, FunderId> {
}
