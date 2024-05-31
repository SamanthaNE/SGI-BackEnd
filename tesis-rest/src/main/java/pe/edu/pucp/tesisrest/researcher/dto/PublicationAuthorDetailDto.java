package pe.edu.pucp.tesisrest.researcher.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PublicationAuthorDetailDto extends PublicationAuthorDto {
    private String abstractPublication;
    private String volume;
    private String startPage;
    private String endPage;
}
