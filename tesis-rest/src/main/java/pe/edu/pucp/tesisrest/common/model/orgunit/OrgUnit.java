package pe.edu.pucp.tesisrest.common.model.orgunit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pe.edu.pucp.tesisrest.common.model.base.Model;

@Entity
@Table(name = "orgunit")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class OrgUnit extends Model {

    @Id
    @Column(name = "idOrgUnit")
    private String idOrgUnit;

    @Column(name = "Name")
    private String name;

    @Column(name = "OriginalName")
    private String originalName;

    @Column(name = "Acronym")
    private String acronym;

    @Column(name = "PartOf")
    private String partOf;

    @Column(name = "RelWith")
    private String relWith;

    @Column(name = "idOrgUnit_Type")
    private String idOrgUnit_Type;
}
