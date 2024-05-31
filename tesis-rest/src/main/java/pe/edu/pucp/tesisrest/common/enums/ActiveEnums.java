package pe.edu.pucp.tesisrest.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum ActiveEnums {

    INACTIVE(0),
    ACTIVE(1);

    private final Integer code;

    private static final Map<Integer, ActiveEnums> BY_CODE = new HashMap<>();

    static {
        Arrays.asList(values()).forEach(value -> BY_CODE.put(value.code, value));
    }

    public static ActiveEnums fromCode(Integer code) {
        return BY_CODE.get(code);
    }

    public static boolean isValid(Integer code) {
        return BY_CODE.containsKey(code);
    }
}
