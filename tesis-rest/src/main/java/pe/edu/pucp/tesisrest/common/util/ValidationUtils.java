package pe.edu.pucp.tesisrest.common.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import pe.edu.pucp.tesisrest.common.dto.base.PaginatedRequest;
import pe.edu.pucp.tesisrest.common.enums.ResultCodeEnum;
import pe.edu.pucp.tesisrest.common.exception.CustomException;
import pe.edu.pucp.tesisrest.common.properties.ApplicationProperties;

@Component
@RequiredArgsConstructor
public class ValidationUtils {

    private final ApplicationProperties applicationProperties;

    public void validateKeyCode(String keyCode) {
        if (!StringUtils.hasText(keyCode)) {
            throwRequired("keyCode");
        }
        if (!keyCode.equals(applicationProperties.getKeyCode())) {
            throwInvalid("keyCode");
        }
    }

    public void validatePagination(PaginatedRequest request) {
        if (request.getPage() == null || request.getSize() == null) {
            if (request.getPage() == null) {
                throwRequired("page");
            }
            if (request.getSize() == null) {
                throwRequired("size");
            }
            if (request.getPage() < 1) {
                throwInvalid("page");
            }
            if (request.getSize() < 1) {
                throwInvalid("size");
            }
        }
    }

    public void validateRequired(String string, String message) {
        if (!StringUtils.hasText(string)) {
            throwRequired(message);
        }
    }

    public void validateRequired(Boolean b, String message) {
        if (b == null) {
            throwRequired(message);
        }
    }

    public void validateRequired(Object object, String message) {
        if (object == null) {
            throwRequired(message);
        }
    }

    public void throwRequired(String message) {
        throw new CustomException(ResultCodeEnum.REQUIRED_VALUE.getCode(),
                ResultCodeEnum.REQUIRED_VALUE.getMessage() + message);
    }

    public void throwInvalid(String message) {
        throw new CustomException(ResultCodeEnum.INVALID_VALUE.getCode(),
                ResultCodeEnum.INVALID_VALUE.getMessage() + message);
    }

    public CustomException buildRequiredException(String message) {
        return new CustomException(ResultCodeEnum.REQUIRED_VALUE.getCode(),
                ResultCodeEnum.REQUIRED_VALUE.getMessage() + message);
    }

    public CustomException buildInvalidException(String message) {
        return new CustomException(ResultCodeEnum.INVALID_VALUE.getCode(),
                ResultCodeEnum.INVALID_VALUE.getMessage() + message);
    }

}
