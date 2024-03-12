package com.locke.babelrecords.controllers;

import com.locke.babelrecords.exceptions.InvalidPasswordException;
import com.locke.babelrecords.exceptions.ItemNotFoundException;
import com.locke.babelrecords.exceptions.UserAlreadyExistsException;
import com.locke.babelrecords.models.SpecField;
import com.locke.babelrecords.models.User;
import com.locke.babelrecords.services.FileService;
import com.locke.babelrecords.services.UserService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
    private UserService userService;
    private FileService fileService;

    @Autowired
    public UserController(UserService userService, FileService fileService) {
        this.userService = userService;
        this.fileService = fileService;
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

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> login(@RequestBody User user) throws ItemNotFoundException, InvalidPasswordException {
        User foundUser = userService.findByUserName(user.getUserName());
        Map<String, String> tokenRes = new HashMap<>();
        tokenRes.put("token", userService.validateCredentials(user.getPassword(), foundUser));
        return new ResponseEntity<>(tokenRes, HttpStatus.OK);
    }

    @PostMapping(value = "{id}/spec-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> postSpecFile(@PathParam("ids") String userId, @RequestPart("file") MultipartFile specFile, @RequestParam String specName) throws IOException {
        List<SpecField> parsedFile = fileService.parseSpecFile(specFile);
        fileService.uploadSpecFile(userId, specName, parsedFile);
        return new ResponseEntity<>(parsedFile, HttpStatus.CREATED);
    }

    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String queryItemNotFound(ItemNotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String userAlreadyExists(UserAlreadyExistsException e) { return e.getMessage(); }

    @ExceptionHandler(InvalidPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String invalidPassword(InvalidPasswordException e) { return e.getMessage(); }
}
