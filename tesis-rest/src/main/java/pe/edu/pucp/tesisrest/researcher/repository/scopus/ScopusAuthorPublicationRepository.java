package pe.edu.pucp.tesisrest.researcher.repository.scopus;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.pucp.tesisrest.common.model.scopus.ScopusAuthor;
import pe.edu.pucp.tesisrest.common.model.scopus.ScopusAuthorPublication;
import pe.edu.pucp.tesisrest.common.model.scopus.ScopusPublication;

import java.util.List;

public interface ScopusAuthorPublicationRepository extends JpaRepository<ScopusAuthorPublication, Integer> {

    List<ScopusAuthorPublication> findByScopusPublicationAndScopusAuthor(ScopusPublication scopusPublication, ScopusAuthor scopusAuthor);
}
