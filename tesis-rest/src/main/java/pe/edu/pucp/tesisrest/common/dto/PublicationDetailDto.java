package pe.edu.pucp.tesisrest.common.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class PublicationDetailDto extends PublicationDto {
    private String abstractPublication;
    private String volume;
    private String startPage;
    private String endPage;

    private List<ProjectDto> relatedProjects = new ArrayList<>();
}
