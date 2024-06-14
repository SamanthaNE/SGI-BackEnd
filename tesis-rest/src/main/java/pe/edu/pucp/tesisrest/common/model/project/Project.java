package pe.edu.pucp.tesisrest.common.model.project;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pe.edu.pucp.tesisrest.common.model.base.Model;

import java.util.Date;

@Entity
@Table(name = "project")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Project extends Model {
    @Id
    @Column(name = "idProject")
    @Size(max = 128, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String idProject;

    @Column(name = "Identifier")
    @Size(max = 128, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String identifier;

    @Column(name = "Title")
    @Size(max = 500, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String title;

    @Column(name = "Acronym")
    @Size(max = 128, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String acronym;

    @Column(name = "StartDate")
    private Date startDate;

    @Column(name = "EndDate")
    private Date endDate;

    @Column(name = "Abstract")
    @Size(max = 2000, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String abstractProject;

    @Column(name = "Resumen")
    @Size(max = 2000, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String resume;

    @Column(name = "LangCode")
    @Size(max = 2, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String langCode;

    @Column(name = "idProject_Status_Type_CONCYTEC")
    @Size(max = 45, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String idProjectStatusTypeCONCYTEC;

    @Column(name = "URL")
    @Size(max = 255, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String url;
}
