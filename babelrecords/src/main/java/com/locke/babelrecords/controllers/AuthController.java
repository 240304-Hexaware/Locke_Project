package com.locke.babelrecords.controllers;

import com.locke.babelrecords.exceptions.RolesNotSetupException;
import com.locke.babelrecords.exceptions.UserAlreadyExists;
import com.locke.babelrecords.models.RegistrationRecord;
import com.locke.babelrecords.models.Role;
import com.locke.babelrecords.models.User;
import com.locke.babelrecords.services.AuthenticationService;
import com.locke.babelrecords.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
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
  public User postUser(@RequestBody RegistrationRecord data, HttpServletResponse response) throws UserAlreadyExists, RolesNotSetupException {
    var result = this.authenticationService.registerUser(data.username(), data.password());
    response.addHeader("Authorization", "bearer " + result.jwt());
    return result.user();
  }

  @PostMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  public User login(@RequestBody RegistrationRecord data, HttpServletResponse response) {
    var result = this.authenticationService.login(data.username(), data.password());
    response.addHeader("Authorization", "bearer " + result.jwt());
    return result.user();
  }

  @PostMapping("/role/{role}")
  public Role postRole(@PathVariable("role") String role) {
    return this.authenticationService.createRole(role);
  }
}
