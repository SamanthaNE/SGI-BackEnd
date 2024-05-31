package pe.edu.pucp.tesisrest.researcher.dto;

import lombok.Data;

@Data
public class AuthorResearcherDto {
    private Long idPerson;
    private String idOrgUnit;
    private String surname;
    private String givenName;
    private Long scopusAuthorId;
}
