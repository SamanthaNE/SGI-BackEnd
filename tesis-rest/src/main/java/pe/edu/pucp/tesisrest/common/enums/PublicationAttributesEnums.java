package pe.edu.pucp.tesisrest.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum PublicationAttributesEnums {

    SUB_TYPE_DESCRIPTION("sub_type_description"),
    AGGREGATION_TYPE("aggregation_type");

    private final String attribute;

    private static final Map<String, PublicationAttributesEnums> BY_CODE = new HashMap<>();

    static {
        Arrays.asList(values()).forEach(value -> BY_CODE.put(value.attribute, value));
    }
}
