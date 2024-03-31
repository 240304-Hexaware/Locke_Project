package com.locke.babelrecords.controllers;

import com.locke.babelrecords.exceptions.UserAlreadyExists;
import com.locke.babelrecords.models.LoginResponse;
import com.locke.babelrecords.models.RegistrationRecord;
import com.locke.babelrecords.models.Role;
import com.locke.babelrecords.models.User;
import com.locke.babelrecords.services.AuthenticationService;
import com.locke.babelrecords.services.UserService;
import jakarta.websocket.server.PathParam;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.AuthenticationException;

@RestController
@CrossOrigin(
    origins = "http://localhost:4200",
    allowCredentials = "true",
    exposedHeaders = "Authorization"
)
@RequestMapping("api/v1/auth")
public class AuthController {
  UserService userService;
  AuthenticationService authenticationService;

  public AuthController(UserService userService, AuthenticationService authenticationService) {
    this.userService = userService;
    this.authenticationService = authenticationService;
  }

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.OK)
  public User postUser(@RequestBody RegistrationRecord data) throws UserAlreadyExists {
    return this.authenticationService.registerUser(data.username(), data.password());
  }

  @PostMapping("/login")
  public LoginResponse login(@RequestBody RegistrationRecord data) throws AuthenticationException {
    return this.authenticationService.login(data.username(), data.password());
  }

  @PostMapping("/role/{role}")
  public Role postRole(@PathVariable("role") String role) {
    return this.authenticationService.createRole(role);
  }
}
