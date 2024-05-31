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
@Table(name = "scopus_publication_per_author")
@Data
@NoArgsConstructor
public class ScopusPublicationPerAuthor {

    @Id
    @Column(name = "scopus_publication_per_author_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scopusPublicationPerAuthorId;

    @JoinColumn(name = "scopus_author_id")
    @ManyToOne
    private ScopusAuthor scopusAuthor;

    @Column(name = "eid")
    private String eid;

    @Column(name = "identifier")
    private String identifier;

    @Column(name = "title")
    private String title;

    @Column(name = "creator")
    private String creator;

    @Column(name = "publication_name")
    private String publicationName;

    @Column(name = "issn")
    private String issn;

    @Column(name = "eissn")
    private String eissn;

    @Column(name = "volume")
    private String volume;

    @Column(name = "page_range")
    private String pageRange;

    @Column(name = "cover_date")
    private String coverDate;

    @Column(name = "cover_display_date")
    private String coverDisplayDate;

    @Column(name = "doi")
    private String doi;

    @Column(name = "description")
    private String description;

    @Column(name = "cited_by_count")
    private Integer citedByCount;

    @Column(name = "sub_type")
    private String subType;

    @Column(name = "sub_type_description")
    private String subTypeDescription;

    @Column(name = "author_keywords")
    private String authorKeywords;

    @Column(name = "issue_identifier")
    private String issueIdentifier;

    @Column(name = "pubmed_id")
    private String pubmedId;

    @Column(name = "source_id")
    private String sourceId;

    @Column(name = "fund_sponsor")
    private String fundSponsor;

    @Column(name = "fund_no")
    private String fundNo;

    @Column(name = "article_number")
    private String articleNumber;

    @Column(name = "openaccess")
    private Integer openaccess;

    @Column(name = "fund_acr")
    private String fundAcr;

    @Column(name = "aggregation_type")
    private String aggregationType;

    @Column(name = "pii")
    private String pii;

    @Column(name = "author_count_limit")
    private Integer authorCountLimit;

    @Column(name = "author_count_total")
    private Integer authorCountTotal;

    @Column(name = "free_to_read_values")
    private String freeToReadValues;

    @Column(name = "free_to_read_labels")
    private String freeToReadLabels;

    @Embedded
    private Audit audit;

    public ScopusPublicationPerAuthor(Long scopusPublicationPerAuthorId) {
        this.scopusPublicationPerAuthorId = scopusPublicationPerAuthorId;
    }

}
