package pe.edu.pucp.tesisrest.common.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ProjectDto {
    private Long idProject;
    private String title;
    private String description;
    private Date StartDate;
    private Date EndDate;
    private String idProjectStatusTypeCONCYTEC;
    private List<FundingListDto> relatedFundingList = new ArrayList<>();
}
