package pe.edu.pucp.tesisrest.researcher.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProjectAuthorDto {
    private Long idProject;
    private String title;
    private String description;
    private String StartDate;
    private String EndDate;
    private String idProjectStatusTypeCONCYTEC;
    private List<FundingListDto> relatedFundingList = new ArrayList<>();
}
