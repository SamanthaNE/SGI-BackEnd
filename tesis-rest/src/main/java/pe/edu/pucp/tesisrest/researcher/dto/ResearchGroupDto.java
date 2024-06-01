package pe.edu.pucp.tesisrest.researcher.dto;

import lombok.Data;

@Data
public class ResearchGroupDto {
    private String idOrgUnit;
    private String status;
    private String name;
    private String acronym;
    private String partOf;
    private String idPersonRole;
    private String statusGroup;
    private String categoryGroup;
}
