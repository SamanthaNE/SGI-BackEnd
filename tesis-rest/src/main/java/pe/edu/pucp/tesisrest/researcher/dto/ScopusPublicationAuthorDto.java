package pe.edu.pucp.tesisrest.researcher.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ScopusPublicationAuthorDto {

    private Long scopusPublicationId;
    private String title;
    private String publicationName;
    private String coverDate;
    private String subTypeDescription;
    private String aggregationType;
    private List<ScopusAuthorDto> authorsList = new ArrayList<>();
}
