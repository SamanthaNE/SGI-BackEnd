package pe.edu.pucp.tesisrest.researcher.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PublicationAuthorDto {
    private Long publicationId;
    private String title;
    private String publishedIn;
    private String publicationDate;
    private String idResourceTypeCOAR;
    private String resourceTypeCOARName;
    private List<AuthorResearcherDto> authorsList = new ArrayList<>();
}
