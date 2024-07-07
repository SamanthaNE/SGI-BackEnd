package pe.edu.pucp.tesisrest.common.model.person;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pe.edu.pucp.tesisrest.common.model.base.Model;

@Entity
@Table(name = "person")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class Person extends Model {

    @Id
    @Column(name = "idPerson")
    @Size(max = 128, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String idPerson;

    @Column(name = "Name")
    @Size(max = 255, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String personName;

    @Column(name = "FamilyNames")
    @Size(max = 255, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String familyNames;

    @Column(name = "FirstNames")
    @Size(max = 255, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String firstNames;

    @Column(name = "DNI")
    @Size(max = 8, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String DNI;
}
