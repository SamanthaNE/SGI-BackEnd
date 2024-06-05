package pe.edu.pucp.tesisrest.common.dto;

import lombok.Data;
import pe.edu.pucp.tesisrest.researcher.dto.AuthorResearcherDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class PublicationDto {
    private Long publicationId;
    private String title;
    private String publishedIn;
    private LocalDate publicationDate;
    private String idResourceTypeCOAR;
    private String resourceTypeCOARName;
    private List<AuthorResearcherDto> authorsList = new ArrayList<>();
}
