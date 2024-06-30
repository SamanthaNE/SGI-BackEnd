package pe.edu.pucp.tesisrest.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum ProjectAttributesEnums {

    FUNDING_AMOUNT("amount", "numeric"),
    STATUS_CONCYTEC("idProject_Status_Type_CONCYTEC", "textual"),
    FUNDING_TYPE("funding_type", "textual");

    private final String attribute;
    private final String type;

    private static final Map<String, ProjectAttributesEnums> BY_CODE = new HashMap<>();

    static {
        Arrays.asList(values()).forEach(value -> BY_CODE.put(value.attribute, value));
    }
}
