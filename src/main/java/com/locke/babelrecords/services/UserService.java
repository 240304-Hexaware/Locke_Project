package com.locke.babelrecords.services;

import com.locke.babelrecords.exceptions.*;
import com.locke.babelrecords.models.ParsedFile;
import com.locke.babelrecords.models.Record;
import com.locke.babelrecords.models.User;
import com.locke.babelrecords.repositories.ParsedFileRepository;
import com.locke.babelrecords.repositories.RecordRepository;
import com.locke.babelrecords.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
  private UserRepository userRepository;
  private ParsedFileRepository parsedFileRepository;

  private RecordRepository recordRepository;

  @Autowired
  public UserService(UserRepository userRepository, ParsedFileRepository parsedFileRepository, RecordRepository recordRepository) {
    this.userRepository = userRepository;
    this.parsedFileRepository = parsedFileRepository;
    this.recordRepository = recordRepository;
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

  public User insertUser(User user) throws UserAlreadyExists {
    Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
    if ( existingUser.isPresent() ) {
      throw new UserAlreadyExists();
    }

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
    String encodedPassword = encoder.encode(user.getPassword());
    user.setPassword(encodedPassword);
    return userRepository.save(user);
  }

  public User updateUser(User updatedUser) throws UserNotFoundException {
    User prevUser = userRepository.findById(updatedUser.getId()).orElseThrow(UserNotFoundException::new);
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
    String encodedPassword = encoder.encode(updatedUser.getPassword());
    updatedUser.setPassword(encodedPassword);
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

  public void changeUserRole(String userId, String newRole) throws UserNotFoundException, InvalidRoleException {
    User foundUser = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    foundUser.setRole(newRole);

    userRepository.save(foundUser);
  }
}
