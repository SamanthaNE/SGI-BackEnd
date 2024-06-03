package pe.edu.pucp.tesisrest.common.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResearchGroupDetailDto extends ResearchGroupDto {
    List<ResearcherDto> researchersList = new ArrayList<>();
    List<PublicationDto> relatedPublications = new ArrayList<>();
    List<ProjectDto> relatedProjects = new ArrayList<>();
}
