package pe.edu.pucp.tesisrest.researcher.repository.scopus;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.pucp.tesisrest.common.model.scopus.ScopusAuthor;

import java.util.Optional;

public interface ScopusAuthorRepository extends JpaRepository<ScopusAuthor, Long> {

    Optional<ScopusAuthor> findByAuthid(String authid);
}
