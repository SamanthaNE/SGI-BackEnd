package pe.edu.pucp.tesisrest.researcher.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResearchGroupDetailDto extends ResearchGroupDto {
    List<ResearcherDto> researchersList = new ArrayList<>();
    List<PublicationAuthorDto> relatedPublications = new ArrayList<>();
    List<ProjectAuthorDto> relatedProjects = new ArrayList<>();
}
