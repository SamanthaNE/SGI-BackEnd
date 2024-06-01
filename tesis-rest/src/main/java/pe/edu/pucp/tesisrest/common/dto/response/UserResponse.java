package pe.edu.pucp.tesisrest.common.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import pe.edu.pucp.tesisrest.common.dto.UserDto;
import pe.edu.pucp.tesisrest.common.dto.base.Response;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserResponse extends Response {
    private UserDto user;
}
