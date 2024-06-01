package pe.edu.pucp.tesisrest.researcher.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProjectAuthorDetailDto extends ProjectAuthorDto{
    private List<ResearcherDto> researchers = new ArrayList<>();
    // Categoria / puntaje / grupos
}
