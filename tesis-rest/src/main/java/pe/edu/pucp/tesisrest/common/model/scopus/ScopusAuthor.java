package pe.edu.pucp.tesisrest.common.model.scopus;

import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import pe.edu.pucp.tesisrest.common.model.base.Audit;
import pe.edu.pucp.tesisrest.common.model.base.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Embedded;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "scopus_author")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ScopusAuthor extends Model {

    @Id
    @Column(name = "scopus_author_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scopusAuthorId;

    @Column(name = "authid")
    @Size(max = 50, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String authid;

    @Column(name = "author_name")
    @Size(max = 50, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String authorName;

    @Column(name = "surname")
    @Size(max = 50, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String surname;

    @Column(name = "initials")
    @Size(max = 50, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String initials;

    @Column(name = "given_name")
    @Size(max = 50, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String givenName;

    @Column(name = "orcid")
    @Size(max = 50, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String orcid;

    @Embedded
    private Audit audit;

    public ScopusAuthor(Long scopusAuthorId) {
        this.scopusAuthorId = scopusAuthorId;
    }
}
