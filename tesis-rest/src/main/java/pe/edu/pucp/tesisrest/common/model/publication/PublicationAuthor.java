package pe.edu.pucp.tesisrest.common.model.publication;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pe.edu.pucp.tesisrest.common.model.base.Model;

import java.time.LocalDate;

@Entity
@Table(name = "publication_author")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class PublicationAuthor extends Model {
    @EmbeddedId
    private PublicationAuthorId id;

    @Column(name = "idPerson")
    @Size(max = 128, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String idPerson;

    @Column(name = "idOrgUnit")
    @Size(max = 128, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String idOrgUnit;

    @Column(name = "DisplayName")
    @Size(max = 500, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String displayName;

    @Column(name = "Surname")
    @Size(max = 100, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String surname;

    @Column(name = "GivenName")
    @Size(max = 100, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String givenName;

    @Column(name = "ScopusAuthorID")
    @Size(max = 128, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String scopusAuthorID;

    @Column(name = "LinkPersonDate")
    private LocalDate linkPersonDate;

    @Column(name = "UpdatePerson")
    private LocalDate updatePerson;
}
