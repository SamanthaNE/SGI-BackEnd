package pe.edu.pucp.tesisrest.common.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProjectDetailDto extends ProjectDto{
    private List<ResearcherDto> researchers = new ArrayList<>();
    // Categoria / puntaje / grupos
}
