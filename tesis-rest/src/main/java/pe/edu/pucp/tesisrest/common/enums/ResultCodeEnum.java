package pe.edu.pucp.tesisrest.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum ResultCodeEnum {

    OK("0000", "OK "),
    REQUIRED_VALUE("1000", "Required value. "),
    INVALID_VALUE("1001", "Invalid value. "),
    NO_RESULTS("1002", "No results. "),
    VALIDATION_ERRORS("1003", "Validation errors. ");

    private final String code;
    private final String message;

    private static final Map<String, ResultCodeEnum> BY_CODE = new HashMap<>();

    static {
        Arrays.asList(values()).forEach(value -> BY_CODE.put(value.code, value));
    }

    public static ResultCodeEnum fromCode(String code) {
        return BY_CODE.get(code);
    }

    public static boolean isValid(String code) {
        return BY_CODE.containsKey(code);
    }
}
