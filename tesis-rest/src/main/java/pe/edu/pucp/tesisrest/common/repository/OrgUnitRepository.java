package pe.edu.pucp.tesisrest.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.pucp.tesisrest.common.model.orgunit.OrgUnit;

public interface OrgUnitRepository extends JpaRepository<OrgUnit, String> {
    OrgUnit findByIdOrgUnit(String idOrgUnit);
}
