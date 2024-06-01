package pe.edu.pucp.tesisrest.researcher.dto;

import lombok.Data;

@Data
public class ResearcherDto {
    private String idPerson;
    private String firstNames;
    private String familyNames;
    private String idRolePerson;
    private String roleName;
}
