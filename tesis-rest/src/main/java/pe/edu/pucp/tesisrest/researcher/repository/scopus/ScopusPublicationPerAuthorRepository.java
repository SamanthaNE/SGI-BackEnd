package pe.edu.pucp.tesisrest.researcher.repository.scopus;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.pucp.tesisrest.common.model.scopus.ScopusAuthor;
import pe.edu.pucp.tesisrest.common.model.scopus.ScopusPublicationPerAuthor;

import java.util.List;

public interface ScopusPublicationPerAuthorRepository extends JpaRepository<ScopusPublicationPerAuthor, Long> {

    List<ScopusPublicationPerAuthor> findByScopusAuthor(ScopusAuthor author);
    List<ScopusPublicationPerAuthor> findByEidAndScopusAuthor(String eid, ScopusAuthor scopusAuthor);
}
