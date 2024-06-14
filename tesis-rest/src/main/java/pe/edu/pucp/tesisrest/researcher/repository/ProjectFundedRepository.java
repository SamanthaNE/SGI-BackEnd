package pe.edu.pucp.tesisrest.researcher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.pucp.tesisrest.common.model.project.ProjectFunded;
import pe.edu.pucp.tesisrest.common.model.project.ProjectFundedId;

public interface ProjectFundedRepository extends JpaRepository<ProjectFunded, ProjectFundedId> {
}
