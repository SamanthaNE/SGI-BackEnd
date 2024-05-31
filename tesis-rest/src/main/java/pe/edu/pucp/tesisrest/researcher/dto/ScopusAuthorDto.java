package pe.edu.pucp.tesisrest.researcher.dto;

import lombok.Data;

@Data
public class ScopusAuthorDto {

    private Long scopusAuthorId;
    private Integer seq;
    private String authid;
    private String authorName;
}
