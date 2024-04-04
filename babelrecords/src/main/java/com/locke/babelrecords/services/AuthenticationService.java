package com.locke.babelrecords.services;

import com.locke.babelrecords.exceptions.*;
import com.locke.babelrecords.models.LoginResponse;
import com.locke.babelrecords.models.Role;
import com.locke.babelrecords.models.User;
import com.locke.babelrecords.repositories.RoleRepository;
import com.locke.babelrecords.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthenticationService {
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;

  private final PasswordEncoder passwordEncoder;

  private final AuthenticationManager authenticationManager;

  private final TokenService tokenService;

  public AuthenticationService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenService tokenService) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
    this.tokenService = tokenService;
  }

  public LoginResponse registerUser(String username, String password) throws UserAlreadyExists, RolesNotSetupException {
    if ( userRepository.findByUsername(username).isPresent() ) throw new UserAlreadyExists();

    String encodedPassword = passwordEncoder.encode(password);
    Role role = roleRepository.findByAuthority("USER").orElseThrow(RolesNotSetupException::new);
    List<Role> roles = new ArrayList<>();
    roles.add(role);

    var newUser = userRepository.save(new User(username, encodedPassword, roles));
    var auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    String token = tokenService.generateJwt(auth);
    return new LoginResponse(newUser, token);
  }

  public LoginResponse login(String username, String password) throws AuthenticationException {
    var auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    String token = tokenService.generateJwt(auth);
    return new LoginResponse(userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("How")), token);
  }

  public Role createRole(String role) {
    return this.roleRepository.save(new Role(role));
  }
}
