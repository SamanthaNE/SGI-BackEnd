package pe.edu.pucp.tesisrest.researcher.repository.scopus;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.pucp.tesisrest.common.model.scopus.ScopusPublication;

import java.util.List;
import java.util.Optional;

public interface ScopusPublicationRepository extends JpaRepository<ScopusPublication, Integer> {

    Optional<ScopusPublication> findScopusPublicationByScopusPublicationId(Long scopusPublicationId);
}
