package pe.edu.pucp.tesisrest.researcher.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class PublicationAuthorDetailDto extends PublicationAuthorDto {
    private String abstractPublication;
    private String volume;
    private String startPage;
    private String endPage;

    private List<ProjectAuthorDto> relatedProjects = new ArrayList<>();
}
