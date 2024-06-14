package pe.edu.pucp.tesisrest.researcher.repository.scopus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.pucp.tesisrest.common.model.scopus.ScopusAuthorPublication;

public interface ScopusAuthorPublicationRepository extends JpaRepository<ScopusAuthorPublication, Integer> {
    @Modifying
    @Transactional
    @Query(value = "UPDATE scopus_author_publication sap SET sap.estado = :estado WHERE sap.scopus_author_id = :scopusAuthorId AND sap.scopus_publication_id = :scopusPublicationId", nativeQuery = true)
    int updateStatusByScopusAuthorId(@Param(value = "estado") int estado,
                                     @Param(value = "scopusAuthorId") Long scopusAuthorId,
                                     @Param(value = "scopusPublicationId") String scopusPublicationId);
}
