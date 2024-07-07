package pe.edu.pucp.tesisrest.researcher.dto;

import lombok.Data;

@Data
public class FilterScopusPublicationDto {
    private String title;
    private String year;
    private String author;
    private String publisher;
    private String aggregationType;
    private String subTypeDescription;
}
