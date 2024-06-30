package pe.edu.pucp.tesisrest.common.model.perfomance;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FactAttributeId implements Serializable {
    private int id_fact_attribute;
    private int id_rule;
    private int seq;
}
