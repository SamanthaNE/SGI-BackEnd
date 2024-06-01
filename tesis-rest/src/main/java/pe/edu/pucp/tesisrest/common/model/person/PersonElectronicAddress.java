package pe.edu.pucp.tesisrest.common.model.person;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pe.edu.pucp.tesisrest.common.model.base.Model;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "person_electronicaddress")
@Data
public class PersonElectronicAddress extends Model {

    @EmbeddedId
    private PersonElectronicAddressId id;

    public PersonElectronicAddressId getId() {
        return id;
    }

    // Setter para id
    public void setId(PersonElectronicAddressId id) {
        this.id = id;
    }

    public String getIdPerson() {
        return id.getIdPerson();
    }

    public void setIdPerson(String idPerson) {
        if (id != null) {
            id.setIdPerson(idPerson);
        }
    }

    public String getElectronicAddress() {
        return id.getElectronicAddress();
    }

    public void setElectronicAddress(String electronicAddress) {
        if (id != null) {
            id.setElectronicAddress(electronicAddress);
        }
    }

    public String getElectronicAddressType() {
        return id.getIdElectronicAddressType();
    }

    public void setElectronicAddressType(String electronicAddressType) {
        if (id != null) {
            id.setIdElectronicAddressType(electronicAddressType);
        }
    }
}
