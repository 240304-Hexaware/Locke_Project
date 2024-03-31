package com.locke.babelrecords.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.locke.babelrecords.exceptions.InvalidPasswordException;
import com.locke.babelrecords.exceptions.ItemNotFoundException;
import com.locke.babelrecords.exceptions.UserAlreadyExists;
import com.locke.babelrecords.exceptions.UserNotFoundException;
import com.locke.babelrecords.models.LoginResponse;
import com.locke.babelrecords.models.LoginToken;
import com.locke.babelrecords.models.Role;
import com.locke.babelrecords.models.User;
import com.locke.babelrecords.repositories.LoginTokenRepository;
import com.locke.babelrecords.repositories.RoleRepository;
import com.locke.babelrecords.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class AuthenticationService {
  private UserRepository userRepository;
  private RoleRepository roleRepository;

  private PasswordEncoder passwordEncoder;

  private AuthenticationManager authenticationManager;

  private TokenService tokenService;
  private LoginTokenRepository loginTokenRepository;

  public AuthenticationService(UserRepository userRepository, LoginTokenRepository loginTokenRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenService tokenService) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
    this.tokenService = tokenService;
    this.loginTokenRepository = loginTokenRepository;
  }

  public User registerUser(String username, String password) throws UserAlreadyExists {
    if ( userRepository.findByUsername(username).isPresent() ) throw new UserAlreadyExists();

    String encodedPassword = passwordEncoder.encode(password);
    Role role = roleRepository.findByAuthority("USER").get();
    List<Role> roles = new ArrayList<>();
    roles.add(role);

    return userRepository.save(new User(username, encodedPassword, roles));
  }

  public LoginResponse login(String username, String password) throws AuthenticationException {
    var auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    String token = tokenService.generateJwt(auth);
    return new LoginResponse(userRepository.findByUsername(username).get(), token);
  }

  public List<LoginToken> getAllTokens() {
    return loginTokenRepository.findByCreatedAtExists(true);
  }

  public String loginAndGetToken(User user, String password) throws InvalidPasswordException {
    String token = validateCredentials(password, user);
    loginTokenRepository.save(new LoginToken(user.getId(), token));
    return token;
  }

  public String getLoginToken(String id) throws ItemNotFoundException {
    return loginTokenRepository.findByUserId(id).orElseThrow(() -> new ItemNotFoundException("Token not found")).getToken();
  }

  public String validateCredentials(String password, User User) throws InvalidPasswordException {
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    if ( passwordEncoder.matches(password, User.getPassword()) ) {
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
      } catch ( JWTCreationException | NoSuchAlgorithmException e ) {
        return e.getMessage();
      }
    }

    throw new InvalidPasswordException("Passwords do not match.");
  }

  public boolean isAuthorized(String userId, String token) throws UserNotFoundException, NoSuchAlgorithmException {
    String storedToken = loginTokenRepository.findByUserId(userId).orElseThrow(UserNotFoundException::new).getToken();
    String splitToken = token.split(" ")[1];

    KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
    kpg.initialize(1024);
    KeyPair keyPair = kpg.generateKeyPair();

    try {
      Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) keyPair.getPublic(), (RSAPrivateKey) keyPair.getPrivate());
      JWTVerifier jwtVerifier = JWT.require(algorithm)
          .withIssuer("Locke")
          .build();

      DecodedJWT decodedJWT = jwtVerifier.verify(splitToken);
      return true;
    } catch ( JWTVerificationException e ) {
      // ToDo: this
    }

    return false;
  }

  public Role createRole(String role) {
    return this.roleRepository.save(new Role(role));
  }
}
