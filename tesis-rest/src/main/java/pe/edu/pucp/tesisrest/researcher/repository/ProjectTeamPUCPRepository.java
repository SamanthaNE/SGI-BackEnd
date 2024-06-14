package pe.edu.pucp.tesisrest.researcher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.pucp.tesisrest.common.model.project.ProjectTeamPUCP;
import pe.edu.pucp.tesisrest.common.model.project.ProjectTeamPUCPId;

public interface ProjectTeamPUCPRepository extends JpaRepository<ProjectTeamPUCP, ProjectTeamPUCPId> {
    @Query(value = "SELECT MAX(ptp.seqMember) FROM project_team_pucp ptp WHERE ptp.idProject = :idProject", nativeQuery = true)
    Integer findLastSeq(@Param(value = "idProject") String idProject);
}
