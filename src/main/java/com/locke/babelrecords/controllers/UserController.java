package com.locke.babelrecords.controllers;

import com.locke.babelrecords.exceptions.ItemNotFoundException;
import com.locke.babelrecords.exceptions.UserAlreadyExistsException;
import com.locke.babelrecords.models.User;
import com.locke.babelrecords.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User getById(@PathVariable String id) throws ItemNotFoundException {
        return userService.findById(id);
    }

    @GetMapping("/username/{username}")
    @ResponseStatus(HttpStatus.OK)
    public User getByUserName(@PathVariable String username) throws ItemNotFoundException {
        return userService.findByUserName(username);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAll() {
        return userService.findAll();
    }
    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    public void postUser(@RequestBody User user) throws UserAlreadyExistsException {
       userService.insertUser(user);
    }
    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String queryItemNotFound(ItemNotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String userAlreadyExists(UserAlreadyExistsException e) { return e.getMessage(); }
}
