package pe.edu.pucp.tesisrest.common.dto.base;

import lombok.Data;
import pe.edu.pucp.tesisrest.common.enums.ResultCodeEnum;

@Data
public class Response {
    private String code = ResultCodeEnum.OK.getCode();
    private String message = ResultCodeEnum.OK.getMessage();

    public void setValues(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
