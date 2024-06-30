package pe.edu.pucp.tesisrest.researcher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.pucp.tesisrest.common.model.perfomance.FactRange;
import pe.edu.pucp.tesisrest.common.model.project.ProjectFunded;
import pe.edu.pucp.tesisrest.common.model.project.ProjectFundedId;

import java.util.List;

public interface ProjectFundedRepository extends JpaRepository<ProjectFunded, ProjectFundedId> {
    @Query(value = "SELECT * FROM project_funded pf WHERE pf.idProject = :idProject", nativeQuery = true)
    ProjectFunded findProjectFundedByIdProject(@Param(value = "idProject") String idProject);
}
