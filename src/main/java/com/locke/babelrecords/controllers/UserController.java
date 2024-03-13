package com.locke.babelrecords.controllers;

import com.locke.babelrecords.exceptions.InvalidPasswordException;
import com.locke.babelrecords.exceptions.ItemNotFoundException;
import com.locke.babelrecords.exceptions.ItemAlreadyExistsException;
import com.locke.babelrecords.models.User;
import com.locke.babelrecords.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public void postUser(@RequestBody User user) throws ItemAlreadyExistsException {
       userService.insertUser(user);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> login(@RequestBody User user) throws ItemNotFoundException, InvalidPasswordException {
        User foundUser = userService.findByUserName(user.getUserName());
        Map<String, String> tokenRes = new HashMap<>();
        tokenRes.put("token",
                userService.loginAndGetToken(foundUser, user.getPassword())
        );
        return new ResponseEntity<>(tokenRes, HttpStatus.OK);
    }



    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String queryItemNotFound(ItemNotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler(ItemAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String userAlreadyExists(ItemAlreadyExistsException e) { return e.getMessage(); }

    @ExceptionHandler(InvalidPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String invalidPassword(InvalidPasswordException e) { return e.getMessage(); }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String cannotParseFile(IOException e) { return  "Could not parse file."; }
}
