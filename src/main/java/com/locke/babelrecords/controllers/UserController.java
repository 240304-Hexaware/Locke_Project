package com.locke.babelrecords.controllers;

import com.locke.babelrecords.exceptions.*;
import com.locke.babelrecords.models.FileField;
import com.locke.babelrecords.models.LoginToken;
import com.locke.babelrecords.models.Record;
import com.locke.babelrecords.models.User;
import com.locke.babelrecords.services.AuthenticationService;
import com.locke.babelrecords.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(
    origins = "http://localhost:4200",
    allowCredentials = "true",
    exposedHeaders = "Authorization"
)
@RequestMapping("api/v1/users")
public class UserController {
  private UserService userService;
  private AuthenticationService authenticationService;

  @Autowired
  public UserController(UserService userService, AuthenticationService authenticationService) {
    this.userService = userService;
    this.authenticationService = authenticationService;
  }

  @GetMapping("/id/{id}")
  @ResponseStatus(HttpStatus.OK)
  public User getById(@PathVariable String id) throws ItemNotFoundException {
    return userService.findById(id);
  }

  @GetMapping("/tokens")
  @ResponseStatus(HttpStatus.OK)
  public List<LoginToken> getTokens() {
    return authenticationService.getAllTokens();
  }

  @GetMapping("/username/{username}")
  @ResponseStatus(HttpStatus.OK)
  public User getByUserName(@PathVariable String username) throws ItemNotFoundException {
    return userService.findByUserName(username);
  }

  @GetMapping("/id/{id}/records")
  @ResponseStatus(HttpStatus.OK)
  public List<Record> getUserRecords(@PathVariable("id") String userId) {
    return userService.getUserRecords(userId);
  }

  @PutMapping("")
  @ResponseStatus(HttpStatus.OK)
  public User updateUser(@RequestBody User updatedUser) throws UserNotFoundException {
    return userService.updateUser(updatedUser);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public void deleteUser(@PathVariable("id") String userId) {
    userService.deleteUser(userId);
  }

  @PutMapping("/role/{id}/{role}")
  @ResponseStatus(HttpStatus.OK)
  public void changeUserRole(@PathVariable("id") String userId, @PathVariable("role") String newRole) throws UserNotFoundException, InvalidRoleException {
    userService.changeUserRole(userId, newRole);
  }

  @GetMapping("")
  @ResponseStatus(HttpStatus.OK)
  public List<User> getAll() {
    return userService.findAll();
  }

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.OK)
  public User postUser(@RequestBody User user) throws UserAlreadyExists {
    return userService.insertUser(new User(user.getUsername(), user.getPassword(), user.getRole()));
  }

  @PostMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  public User login(@RequestBody User user, HttpServletResponse response) throws ItemNotFoundException, InvalidPasswordException {
    User foundUser = userService.findByUserName(user.getUsername());
    String token = authenticationService.loginAndGetToken(foundUser, user.getPassword());
    response.addHeader("AUTHORIZATION", "bearer " + token);

    return foundUser;
  }


  @ExceptionHandler(ItemNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public String queryItemNotFound(ItemNotFoundException e) {
    return e.getMessage();
  }

  @ExceptionHandler(UserAlreadyExists.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public String userAlreadyExists(UserAlreadyExists e) {
    return e.getMessage();
  }

  @ExceptionHandler(InvalidPasswordException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String invalidPassword(InvalidPasswordException e) {
    return e.getMessage();
  }

  @ExceptionHandler(IOException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String cannotParseFile(IOException e) {
    return "Could not parse file.";
  }
}
