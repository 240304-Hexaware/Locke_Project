package com.locke.babelrecords.services;

import com.locke.babelrecords.exceptions.*;
import com.locke.babelrecords.models.FileField;
import com.locke.babelrecords.models.ParsedFile;
import com.locke.babelrecords.models.User;
import com.locke.babelrecords.repositories.ParsedFileRepository;
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

  @Autowired
  public UserService(UserRepository userRepository, ParsedFileRepository parsedFileRepository) {
    this.userRepository = userRepository;
    this.parsedFileRepository = parsedFileRepository;
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

  public void insertUser(User user) throws UserAlreadyExists {
    Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
    if ( existingUser.isPresent() ) {
      throw new UserAlreadyExists();
    }

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
    String encodedPassword = encoder.encode(user.getPassword());
    user.setPassword(encodedPassword);
    userRepository.save(user);
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

  public List<List<FileField>> getUserRecords(String userId) {
    return parsedFileRepository.findByUserId(userId)
        .stream() // Each parsed file has an array of records
        .map(ParsedFile::getRecords) // Get all the arrays of records; gets one list of lists of records (which are lists)
        .flatMap(List::stream) // Merge them into a List of records (which are lists)
        .toList();
  }

  public void changeUserRole(String userId, String newRole) throws UserNotFoundException, InvalidRoleException {
    User foundUser = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    foundUser.setRole(newRole);

    userRepository.save(foundUser);
  }
}
