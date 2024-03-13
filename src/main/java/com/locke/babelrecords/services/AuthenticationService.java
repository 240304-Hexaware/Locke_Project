package com.locke.babelrecords.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.locke.babelrecords.exceptions.InvalidPasswordException;
import com.locke.babelrecords.exceptions.ItemNotFoundException;
import com.locke.babelrecords.models.LoginToken;
import com.locke.babelrecords.models.User;
import com.locke.babelrecords.repositories.LoginTokenRepository;
import com.locke.babelrecords.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Service
public class AuthenticationService {
    private UserRepository userRepository;
    private LoginTokenRepository loginTokenRepository;

    public AuthenticationService(UserRepository userRepository, LoginTokenRepository loginTokenRepository) {
        this.userRepository = userRepository;
        this.loginTokenRepository = loginTokenRepository;
    }

    public String loginAndGetToken(User user, String password) throws InvalidPasswordException {
        String token = validateCredentials(password, user);
        loginTokenRepository.save(new LoginToken(user.getId(), token));
        return token;
    }

    public String getLoginToken(String id) throws ItemNotFoundException {
        return userRepository.findById(id).orElseThrow().getLoginToken();
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
