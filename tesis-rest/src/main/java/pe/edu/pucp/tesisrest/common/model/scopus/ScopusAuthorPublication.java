package pe.edu.pucp.tesisrest.common.model.scopus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Embedded;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.pucp.tesisrest.common.model.base.Audit;


@Entity
@Table(name = "scopus_author_publication")
@Data
@NoArgsConstructor
public class ScopusAuthorPublication {

    @Id
    @Column(name = "scopus_author_publication_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scopusAuthorPublicationId;

    @JoinColumn(name = "scopus_publication_id")
    @ManyToOne
    private ScopusPublication scopusPublication;

    @JoinColumn(name = "scopus_author_id")
    @ManyToOne
    private ScopusAuthor scopusAuthor;

    @Column(name = "seq")
    private Integer seq;

    @Embedded
    private Audit audit;

    public ScopusAuthorPublication(Long scopusAuthorPublicationId) {
        this.scopusAuthorPublicationId = scopusAuthorPublicationId;
    }
}
