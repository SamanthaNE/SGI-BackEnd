package pe.edu.pucp.tesisrest.common.dto;

import lombok.Data;

@Data
public class UserDto {
    private String idPerson;
    private String scopusAuthorId;
    //private List<String> altScopusAuthorId = new ArrayList<>();
    private String firstNames;
    private String familyNames;
    private String idPersonRole;
    private String roleName;
}
