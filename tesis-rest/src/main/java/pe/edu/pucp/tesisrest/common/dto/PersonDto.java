package pe.edu.pucp.tesisrest.common.dto;

import lombok.Data;
import pe.edu.pucp.tesisrest.common.model.person.Person;

@Data
public class PersonDto {
    private Person personInfo;
}
