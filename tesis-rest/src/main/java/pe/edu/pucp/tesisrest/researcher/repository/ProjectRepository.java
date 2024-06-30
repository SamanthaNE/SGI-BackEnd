package pe.edu.pucp.tesisrest.researcher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.edu.pucp.tesisrest.common.model.project.Project;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
    @Query(value = "SELECT p.idProject FROM project p ORDER BY CAST(SUBSTRING(p.idProject, 1) AS UNSIGNED) DESC LIMIT 1", nativeQuery = true)
    String findLastIdProject();

    Project findProjectByIdProject(String idProject);
}
