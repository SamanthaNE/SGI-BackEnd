package pe.edu.pucp.tesisrest.common.model.orgunit;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FunderId implements Serializable {
    private String idOrgUnit;
    private String idFunding;
}
