package pe.edu.pucp.tesisrest.common.model.person;

import jakarta.persistence.Column;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
@NoArgsConstructor
public class PersonElectronicAddressId implements Serializable {
    @Column(name = "idPerson")
    private String idPerson;

    @Column(name = "ElectronicAddress")
    private String electronicAddress;

    @Column(name = "idElectronicAddress_Type")
    private String idElectronicAddressType;

    // Sobrescribe equals() y hashCode() para comparar objetos de esta clase correctamente
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonElectronicAddressId that = (PersonElectronicAddressId) o;
        return Objects.equals(idPerson, that.idPerson) &&
                Objects.equals(idElectronicAddressType, that.idElectronicAddressType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPerson, idElectronicAddressType);
    }
}
