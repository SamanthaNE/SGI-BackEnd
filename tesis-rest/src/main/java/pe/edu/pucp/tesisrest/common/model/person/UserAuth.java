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
@Table(name = "user_authentication")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class UserAuth extends Model {
    @Id
    @Column(name = "idPerson")
    @Size(max = 128, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String idPerson;

    @Column(name = "password_hash")
    @Size(max = 255, message = MUST_NOT_BE_GREATER_THAN_CHARS)
    private String passwordHash;

}
