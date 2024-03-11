package com.locke.babelrecords.controllers;

import com.locke.babelrecords.exceptions.ItemNotFoundException;
import com.locke.babelrecords.models.User;
import com.locke.babelrecords.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User getById(@PathVariable String id) throws ItemNotFoundException {
        return userService.findById(id);
    }

    @GetMapping("/users/username/{username}")
    @ResponseStatus(HttpStatus.OK)
    public User getByUserName(@PathVariable String username) throws ItemNotFoundException {
        return userService.findByUserName(username);
    }
    @PostMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public void postUser(@RequestParam User user) {
       userService.insertUser(user);
    }
    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String queryItemNotFound(ItemNotFoundException e) {
        return e.getMessage();
    }
}
