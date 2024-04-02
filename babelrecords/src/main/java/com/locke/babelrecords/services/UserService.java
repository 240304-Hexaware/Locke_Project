package com.locke.babelrecords.services;

import com.locke.babelrecords.exceptions.*;
import com.locke.babelrecords.models.ParsedFile;
import com.locke.babelrecords.models.Record;
import com.locke.babelrecords.models.Role;
import com.locke.babelrecords.models.User;
import com.locke.babelrecords.repositories.RecordRepository;
import com.locke.babelrecords.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
  private UserRepository userRepository;
  private RecordRepository recordRepository;

  private PasswordEncoder passwordEncoder;

  @Autowired
  public UserService(UserRepository userRepository, RecordRepository recordRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.recordRepository = recordRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public User findById(String id) throws ItemNotFoundException {
    return userRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Id not found"));
  }

  public User findByUserName(String username) throws ItemNotFoundException {
    return userRepository.findByUsername(username).orElseThrow(() -> new ItemNotFoundException("username not found"));
  }

  public List<User> findAll() {
    return userRepository.findAll();
  }

  public User updateUser(User updatedUser) throws UserNotFoundException {
    User prevUser = userRepository.findById(updatedUser.getId()).orElseThrow(UserNotFoundException::new);
    updatedUser.setPassword(this.passwordEncoder.encode(updatedUser.getPassword()));
    userRepository.save(updatedUser);
    return prevUser;
  }

  public void deleteUser(String userId) {
    userRepository.deleteById(userId);
  }

  public List<Record> getUserRecords(String userId) {
    User user = userRepository.findById(userId).orElseThrow();
    return recordRepository.findAllById(user.getRecordIds());
  }

  public void addSpecToUser(String userId, String specFileId) {
    User user = userRepository.findById(userId).orElseThrow();
    user.addSpecFile(specFileId);
    userRepository.save(user);
  }

  public void addParsedToUser(String userId, ParsedFile parsedFile) {
    User user = userRepository.findById(userId).orElseThrow();
    user.addParsedFile(parsedFile.getId());
    user.addRecords(parsedFile.getRecordIds());
    userRepository.save(user);
  }

  public void changeUserRole(String userId, Role newRole) throws UserNotFoundException, InvalidRoleException {
    User foundUser = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    foundUser.AddRole(newRole);

    userRepository.save(foundUser);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("username not found"));
  }
}
