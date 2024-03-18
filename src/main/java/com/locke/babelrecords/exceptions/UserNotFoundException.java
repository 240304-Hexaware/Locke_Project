package com.locke.babelrecords.exceptions;

public class UserNotFoundException extends Exception {
  public UserNotFoundException() {
    super("Could not find requested username");
  }
}
