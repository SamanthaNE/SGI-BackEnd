package pe.edu.pucp.tesisrest.common.service;

import pe.edu.pucp.tesisrest.common.dto.request.UserRequest;
import pe.edu.pucp.tesisrest.common.dto.response.UserResponse;

public interface UserService {
    UserResponse autheticateUser(UserRequest request);
    UserResponse registerUser(UserRequest request);
}
