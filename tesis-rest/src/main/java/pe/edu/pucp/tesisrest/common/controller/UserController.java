package pe.edu.pucp.tesisrest.common.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pe.edu.pucp.tesisrest.common.dto.request.UserRequest;
import pe.edu.pucp.tesisrest.common.dto.response.UserResponse;
import pe.edu.pucp.tesisrest.common.service.UserService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/users")
public class UserController {
    private final UserService userService;

    @PostMapping(value = "/login")
    @Operation(summary = "Login registered user")
    public UserResponse login(@RequestBody UserRequest request) {
        return userService.autheticateUser(request);
    }

    @PostMapping(value = "/register")
    @Operation(summary = "Register the password of a registered user")
    public UserResponse register(@RequestBody UserRequest request) {
        return userService.registerUser(request);
    }

}
