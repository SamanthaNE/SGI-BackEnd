package pe.edu.pucp.tesisrest.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.pucp.tesisrest.common.model.publication.PublicationAuthor;
import pe.edu.pucp.tesisrest.common.model.publication.PublicationAuthorId;

public interface PublicationAuthorRepository extends JpaRepository<PublicationAuthor, PublicationAuthorId> {
}
