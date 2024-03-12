package com.locke.babelrecords.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.locke.babelrecords.exceptions.InvalidPasswordException;
import com.locke.babelrecords.exceptions.ItemNotFoundException;
import com.locke.babelrecords.exceptions.UserAlreadyExistsException;
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
    public void insertUser(User user) throws UserAlreadyExistsException {
        Optional<User> existingUser = userRep.findByUserName(user.getUserName());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("User already exists.");
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRep.save(user);
    }

    public String validateCredentials(String password, User User) throws InvalidPasswordException {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (passwordEncoder.matches(password, User.getPassword())) {
            try {
                KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
                kpg.initialize(1024);
                KeyPair keyPair = kpg.generateKeyPair();

                RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
                RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
                Algorithm algorithm = Algorithm.RSA256(rsaPublicKey, rsaPrivateKey);

                return JWT.create()
                        .withIssuer("Locke")
                        .sign(algorithm);
            } catch (JWTCreationException | NoSuchAlgorithmException e) {
                return e.getMessage();
            }
        }

        throw new InvalidPasswordException("Passwords do not match.");
    }
}
