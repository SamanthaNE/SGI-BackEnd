package pe.edu.pucp.tesisrest.common.model.publication;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicationOriginatesFromId implements Serializable {

    private int idPublication;
    private String idProject;
}
