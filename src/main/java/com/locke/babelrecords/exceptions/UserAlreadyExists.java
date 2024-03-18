package com.locke.babelrecords.exceptions;

public class UserAlreadyExists extends Exception {
  public UserAlreadyExists() {
    super("That user already exists in the database.");
  }
}
