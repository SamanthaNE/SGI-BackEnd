package pe.edu.pucp.tesisrest.common.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResearchGroupDetailDto extends ResearchGroupDto {
    private List<ResearcherDto> researchersList = new ArrayList<>();
    private List<PublicationDto> relatedPublications = new ArrayList<>();
    private List<ProjectDto> relatedProjects = new ArrayList<>();
    private List<ResearchGroupEvaluationDetail> researchGroupEvaluationDetail  = new ArrayList<>();
}
