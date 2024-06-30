package pe.edu.pucp.tesisrest.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.edu.pucp.tesisrest.common.model.publication.Publication;

import java.util.Optional;

public interface PublicationRepository extends JpaRepository<Publication, Integer> {
    @Query(value = "SELECT MAX(p.idPublication) FROM publication p", nativeQuery = true)
    Integer findLastIdPublication();

    Publication findPublicationByDOI(String doi);

    Publication findPublicationByIdPublication(Integer idPublication);

    String findDOIByIdPublication(Integer idPublication);
}
