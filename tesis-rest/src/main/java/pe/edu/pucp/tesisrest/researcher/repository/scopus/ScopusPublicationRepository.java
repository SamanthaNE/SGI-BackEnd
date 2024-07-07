package pe.edu.pucp.tesisrest.researcher.repository.scopus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pe.edu.pucp.tesisrest.common.model.scopus.ScopusPublication;

import java.util.Optional;

public interface ScopusPublicationRepository extends JpaRepository<ScopusPublication, Integer>, JpaSpecificationExecutor<ScopusPublication> {

    Optional<ScopusPublication> findScopusPublicationByScopusPublicationId(Long scopusPublicationId);
    ScopusPublication findScopusPublicationByDoi(String doi);
}
