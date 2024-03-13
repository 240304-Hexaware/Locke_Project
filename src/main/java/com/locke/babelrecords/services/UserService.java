package com.locke.babelrecords.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.locke.babelrecords.exceptions.InvalidPasswordException;
import com.locke.babelrecords.exceptions.ItemNotFoundException;
import com.locke.babelrecords.exceptions.ItemAlreadyExistsException;
import com.locke.babelrecords.models.User;
import com.locke.babelrecords.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRep;

    @Autowired
    public UserService(UserRepository userRep) {
        this.userRep = userRep;
    }

    public User findById(String id) throws ItemNotFoundException{
        return userRep.findById(id).orElseThrow(() -> new ItemNotFoundException("Id not found"));
    }

    public User findByUserName(String userName) throws ItemNotFoundException {
        return userRep.findByUserName(userName).orElseThrow(() -> new ItemNotFoundException("userName not found"));
    }

    public List<User> findAll() {
        return userRep.findAll();
    }
    public void insertUser(User user) throws ItemAlreadyExistsException {
        Optional<User> existingUser = userRep.findByUserName(user.getUserName());
        if (existingUser.isPresent()) {
            throw new ItemAlreadyExistsException("User already exists.");
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRep.save(user);
    }
}
