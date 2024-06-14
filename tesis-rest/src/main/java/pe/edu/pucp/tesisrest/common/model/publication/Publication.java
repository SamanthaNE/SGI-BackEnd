package pe.edu.pucp.tesisrest.common.model.publication;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pe.edu.pucp.tesisrest.common.model.base.Model;

import java.util.Date;

@Entity
@Table(name = "publication")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Publication extends Model {
    @Id
    @Column(name = "idPublication")
    private int idPublication;

    @Column(name = "Title")
    @Size(max = 1000, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String title;

    @Column(name = "idResource_Type_COAR")
    @Size(max = 45, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String idResourceTypeCOAR;

    @Column(name = "idAccess_Right_COAR")
    @Size(max = 10, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String idAccessRightCOAR;

    @Column(name = "Handle")
    @Size(max = 128, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String handle;

    @Column(name = "DOI")
    @Size(max = 128, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String DOI;

    @Column(name = "PMCID")
    @Size(max = 128, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String PMCID;

    @Column(name = "ISINumber")
    @Size(max = 128, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String ISINumber;

    @Column(name = "SCPNumber")
    @Size(max = 128, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String SCPNumber;

    @Column(name = "URL")
    @Size(max = 650, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String URL;

    @Column(name = "PublishedInCode")
    private Integer publishedInCode;

    @Column(name = "PublishedInDesc")
    @Size(max = 300, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String publishedInDesc;

    @Column(name = "PartOf")
    private Integer partOf;

    @Column(name = "PublicationDateDesc")
    @Size(max = 45, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String publicationDateDesc;

    @Column(name = "PublicationDate")
    private Date publicationDate;

    @Column(name = "PublicationYear")
    private int publicationYear;

    @Column(name = "Number")
    @Size(max = 45, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String number;

    @Column(name = "Volume")
    @Size(max = 200, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String volume;

    @Column(name = "Issue")
    @Size(max = 45, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String issue;

    @Column(name = "Edition")
    @Size(max = 45, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String edition;

    @Column(name = "StartPage")
    @Size(max = 45, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String startPage;

    @Column(name = "EndPage")
    @Size(max = 45, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String endPage;

    @Column(name = "LangCode")
    @Size(max = 2, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String langCode;

    @Lob
    @Column(name = "Abstract", columnDefinition = "TEXT")
    private String abstractText;

    @Column(name = "idResearch_Type_RENATI")
    @Size(max = 30, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String idResearchTypeRENATI;

    @Column(name = "idAcademic_Degree_PUCP")
    @Size(max = 128, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String idAcademicDegreePUCP;

    @Column(name = "idLicense_Type")
    @Size(max = 10, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String idLicenseType;
}
