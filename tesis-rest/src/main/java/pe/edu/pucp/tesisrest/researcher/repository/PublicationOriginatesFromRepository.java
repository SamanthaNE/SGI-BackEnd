package pe.edu.pucp.tesisrest.researcher.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.pucp.tesisrest.common.model.publication.PublicationOriginatesFrom;
import pe.edu.pucp.tesisrest.common.model.publication.PublicationOriginatesFromId;

public interface PublicationOriginatesFromRepository extends JpaRepository<PublicationOriginatesFrom, PublicationOriginatesFromId> {
}
