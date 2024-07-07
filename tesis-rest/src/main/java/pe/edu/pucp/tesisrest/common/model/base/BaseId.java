package pe.edu.pucp.tesisrest.common.model.base;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import pe.edu.pucp.tesisrest.common.model.perfomance.FactAttributeId;
import pe.edu.pucp.tesisrest.common.model.perfomance.FactRangeId;

import java.io.Serializable;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = FactAttributeId.class, name = "factAttributeId"),
        @JsonSubTypes.Type(value = FactRangeId.class, name = "factRangeId")
})
public abstract class BaseId implements Serializable {
}
