package pe.edu.pucp.tesisrest.researcher.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResearchGroupDetailDto {
    List<AuthorResearcherDto> researchersList = new ArrayList<>();
    List<PublicationAuthorDto> relatedPublications = new ArrayList<>();
}
